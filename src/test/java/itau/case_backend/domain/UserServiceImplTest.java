package itau.case_backend.domain;

import itau.case_backend.adapters.output.UserRepository;
import itau.case_backend.config.exception.EmailAlreadyExistsException;
import itau.case_backend.config.exception.UserNotFoundException;
import itau.case_backend.domain.dtos.UserDTO;
import itau.case_backend.domain.dtos.UserPartialUpdateDTO;
import itau.case_backend.domain.entities.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class UserServiceImplTest {
    private UserServiceImpl userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        userService = new UserServiceImpl(userRepository);
    }


    @Test
    void When_GettingAllUsers_Expect_UsersReturned() {
        UserDTO userDTO1 = new UserDTO("Alice", "alice@example.com", 25);
        UserDTO userDTO2 = new UserDTO("Bob", "bob@example.com", 30);

        userService.createUser(userDTO1);
        userService.createUser(userDTO2);

        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());

        assertTrue(users.stream().anyMatch(user -> user.getName().equals("Alice")));
        assertTrue(users.stream().anyMatch(user -> user.getName().equals("Bob")));
    }

    @Test
    void When_GettingAllUsers_When_NoUsersExist_Expect_EmptyList() {
        List<User> users = userService.getAllUsers();

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void When_CreatingUser_Expect_UserCreated() {
        UserDTO userDTO = new UserDTO("John Doe", "john@example.com", 30);

        User savedUser = userService.createUser(userDTO);

        assertNotNull(savedUser);
        assertEquals(userDTO.getName(), savedUser.getName());
        assertEquals(userDTO.getEmail(), savedUser.getEmail());
        assertEquals(userDTO.getAge(), savedUser.getAge());
        assertNotEquals(0, savedUser.getId());

    }

    @Test
    void When_CreatingUserWithExistingEmail_Expect_EmailAlreadyExistsException() {
        UserDTO userDTO = new UserDTO("John Doe", "john@example.com", 30);

        userService.createUser(userDTO);

        UserDTO duplicateEmailDTO = new UserDTO("Jane Doe", "john@example.com", 25);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.createUser(duplicateEmailDTO));
    }

    @Test
    void When_GettingUserById_Expect_UserFound() {
        UserDTO userDTO = new UserDTO("Alice", "alice@example.com", 25);
        User savedUser = userService.createUser(userDTO);

        User foundUser = userService.getUserById(savedUser.getId());

        assertNotNull(foundUser);
        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals(savedUser.getName(), foundUser.getName());
        assertEquals(savedUser.getEmail(), foundUser.getEmail());
    }

    @Test
    void When_GettingUserByNonExistentId_Expect_UserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(9999));
    }

    @Test
    void When_UpdatingUser_Expect_UserUpdatedSuccessfully() {
        UserDTO userDTO = new UserDTO("Alice", "alice@example.com", 25);
        User savedUser = userService.createUser(userDTO);

        UserDTO updatedUserDTO = new UserDTO("Alice Updated", "alice.updated@example.com", 26);
        User updatedUser = userService.updateUser(savedUser.getId(), updatedUserDTO);

        assertNotNull(updatedUser);
        assertEquals(updatedUserDTO.getName(), updatedUser.getName());
        assertEquals(updatedUserDTO.getEmail(), updatedUser.getEmail());
        assertEquals(updatedUserDTO.getAge(), updatedUser.getAge());
    }


    @Test
    void When_UpdatingNonExistentUser_Expect_UserNotFoundException() {
        UserDTO updatedUserDTO = new UserDTO("Alice Updated", "alice.updated@example.com", 26);

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(9999, updatedUserDTO));
    }

    @Test
    void When_UpdatingUserWithExistingEmail_Expect_EmailAlreadyExistsException() {
        UserDTO userDTO = new UserDTO("Alice", "alice@example.com", 25);
        User savedUser = userService.createUser(userDTO);

        UserDTO anotherUserDTO = new UserDTO("Bob", "bob@example.com", 30);
        userService.createUser(anotherUserDTO);

        UserDTO updatedUserDTO = new UserDTO("Alice Updated", "bob@example.com", 26);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.updateUser(savedUser.getId(), updatedUserDTO));
    }

    @Test
    void When_PartialUpdatingUser_Expect_UserUpdatedSuccessfully() {
        UserDTO userDTO = new UserDTO("Alice", "alice@example.com", 25);
        User savedUser = userService.createUser(userDTO);

        UserPartialUpdateDTO partialUpdateDTO = new UserPartialUpdateDTO("Alice Updated", null, 26);
        User updatedUser = userService.partialUpdateUser(savedUser.getId(), partialUpdateDTO);

        assertNotNull(updatedUser);
        assertEquals(partialUpdateDTO.getName(), updatedUser.getName());
        assertEquals(savedUser.getEmail(), updatedUser.getEmail());
        assertEquals(partialUpdateDTO.getAge(), updatedUser.getAge());
    }

    @Test
    void When_PartialUpdatingUserWithExistingEmail_Expect_EmailAlreadyExistsException() {
        UserDTO userDTO = new UserDTO("Alice", "alice@example.com", 25);
        User savedUser = userService.createUser(userDTO);

        UserDTO anotherUserDTO = new UserDTO("Bob", "bob@example.com", 30);
        userService.createUser(anotherUserDTO);

        UserPartialUpdateDTO partialUpdateDTO = new UserPartialUpdateDTO(null, "bob@example.com", null);

        assertThrows(EmailAlreadyExistsException.class, () -> userService.partialUpdateUser(savedUser.getId(), partialUpdateDTO));
    }

    @Test
    void When_PartialUpdatingNonExistentUser_Expect_UserNotFoundException() {
        UserPartialUpdateDTO partialUpdateDTO = new UserPartialUpdateDTO("Updated Name", "updated@example.com", 28);

        assertThrows(UserNotFoundException.class, () -> userService.partialUpdateUser(9999, partialUpdateDTO));
    }

    @Test
    void When_DeletingUser_Expect_UserRemoved() {
        UserDTO userDTO = new UserDTO("John Doe", "john@example.com", 30);
        User savedUser = userService.createUser(userDTO);

        userService.deleteUser(savedUser.getId());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(savedUser.getId()));
    }

    @Test
    void When_DeletingNonExistentUser_Expect_UserNotFoundException() {
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(9999));
    }

}