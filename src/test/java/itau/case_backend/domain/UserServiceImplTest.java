package itau.case_backend.domain;

import itau.case_backend.adapters.output.UserRepository;
import itau.case_backend.config.exception.EmailAlreadyExistsException;
import itau.case_backend.config.exception.UserNotFoundException;
import itau.case_backend.domain.dtos.UserDTO;
import itau.case_backend.domain.entities.User;
import itau.case_backend.ports.output.UserOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class UserServiceImplTest {
    private UserServiceImpl userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
        userService = new UserServiceImpl(userRepository);
    }


    @Test
    void When_CreatingUser_Expect_UserCreated() {
        UserDTO userDTO = new UserDTO("John Doe", "john@example.com", 30);

        User savedUser = userService.createUser(userDTO);

        assertNotNull(savedUser);
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals(30, savedUser.getAge());
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
        assertEquals("Alice Updated", updatedUser.getName());
        assertEquals("alice.updated@example.com", updatedUser.getEmail());
        assertEquals(26, updatedUser.getAge());
    }


    @Test
    void When_UpdatingNonExistentUser_Expect_UserNotFoundException() {
        UserDTO updatedUserDTO = new UserDTO("Alice Updated", "alice.updated@example.com", 26);

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(9999, updatedUserDTO));
    }


}