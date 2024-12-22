package itau.case_backend.adapters.output;

import itau.case_backend.domain.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    void When_SavingMultipleUsers_Expect_ReturnAllUsers() {
        User user1 = new User(0, "John Doe", "john@example.com", 30);
        User user2 = new User(0, "Jane Smith", "jane@example.com", 25);

        userRepository.saveUser(user1);
        userRepository.saveUser(user2);

        List<User> users = userRepository.findAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("John Doe", users.get(0).getName());
        assertEquals("Jane Smith", users.get(1).getName());
    }

    @Test
    void When_SavingNoUsers_Expect_EmptyUserList() {
        List<User> users = userRepository.findAllUsers();

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }

    @Test
    void When_UserExists_Expect_ReturnUserById() {
        User user = new User(0, "John Doe", "john@example.com", 30);
        userRepository.saveUser(user);

        Optional<User> foundUser = userRepository.findUserById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
    }

    @Test
    void When_UserDoesNotExist_Expect_ReturnEmptyOptional() {
        User user = new User(0, "John Doe", "john@example.com", 30);
        userRepository.saveUser(user);

        Optional<User> foundUser = userRepository.findUserById(999L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void When_SavingNewUser_Expect_ReturnSavedUserWithId() {
        User user = new User(0, "John Doe", "john@example.com", 30);

        User savedUser = userRepository.saveUser(user);

        assertNotNull(savedUser);
        assertEquals(1, savedUser.getId());
        assertEquals("John Doe", savedUser.getName());
    }

    @Test
    void When_UpdatingUser_Expect_UserUpdatedSuccessfully() {
        User user = new User(0, "John Doe", "john@example.com", 30);
        User savedUser = userRepository.saveUser(user);

        savedUser.setName("John Smith");
        userRepository.saveUser(savedUser);

        Optional<User> updatedUser = userRepository.findUserById(savedUser.getId());

        assertTrue(updatedUser.isPresent());
        assertEquals("John Smith", updatedUser.get().getName());
    }

    @Test
    void When_DeletingUserById_Expect_UserRemoved() {
        User user = new User(0, "John Doe", "john@example.com", 30);
        User savedUser = userRepository.saveUser(user);
        Optional<User> savedUserCheck = userRepository.findUserById(savedUser.getId());
        assertTrue(savedUserCheck.isPresent());

        userRepository.deleteUserById(savedUser.getId());

        Optional<User> deletedUser = userRepository.findUserById(savedUser.getId());
        assertFalse(deletedUser.isPresent());

    }

    @Test
    void When_DeletingNonexistentUserById_Expect_NoChangeInUserList() {
        userRepository.saveUser(new User(0, "John Doe", "john@example.com", 30));
        List<User> usersBeforeDeletion = userRepository.findAllUsers();
        int initialSize = usersBeforeDeletion.size();

        userRepository.deleteUserById(999L);

        List<User> usersAfterDeletion = userRepository.findAllUsers();
        assertEquals(initialSize, usersAfterDeletion.size());
        assertTrue(usersAfterDeletion.stream().anyMatch(user -> user.getId() == 1L));

    }
}
