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
    void testFindAllUsers() {
        User user1 = new User(0, "John Doe", "john@example.com", 30);
        User user2 = new User(0, "Jane Doe", "jane@example.com", 25);

        userRepository.saveUser(user1);
        userRepository.saveUser(user2);

        List<User> users = userRepository.findAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("John Doe", users.get(0).getName());
        assertEquals("Jane Doe", users.get(1).getName());
    }

    @Test
    void testFindUserById() {
        User user = new User(0, "John Doe", "john@example.com", 30);
        userRepository.saveUser(user);

        Optional<User> foundUser = userRepository.findUserById(user.getId());

        assertTrue(foundUser.isPresent());
        assertEquals("John Doe", foundUser.get().getName());
    }

    @Test
    void testFindUserByIdNotFound() {
        Optional<User> foundUser = userRepository.findUserById(999L);

        assertFalse(foundUser.isPresent());
    }

    @Test
    void testSaveUser() {
        User user = new User(0, "John Doe", "john@example.com", 30);

        User savedUser = userRepository.saveUser(user);

        assertNotNull(savedUser);
        assertEquals(1, savedUser.getId()); // ID gerado deve ser 1 (primeiro ID)
        assertEquals("John Doe", savedUser.getName());
    }

    @Test
    void testUpdateUser() {
        User user = new User(0, "John Doe", "john@example.com", 30);
        User savedUser = userRepository.saveUser(user);

        savedUser.setName("John Smith");
        userRepository.saveUser(savedUser);

        Optional<User> updatedUser = userRepository.findUserById(savedUser.getId());

        assertTrue(updatedUser.isPresent());
        assertEquals("John Smith", updatedUser.get().getName());
    }

    @Test
    void testDeleteUserById() {
        User user = new User(0, "John Doe", "john@example.com", 30);
        User savedUser = userRepository.saveUser(user);

        userRepository.deleteUserById(savedUser.getId());

        Optional<User> deletedUser = userRepository.findUserById(savedUser.getId());

        assertFalse(deletedUser.isPresent());
    }

    @Test
    void testDeleteUserByIdNotFound() {
        userRepository.deleteUserById(999L);

        List<User> users = userRepository.findAllUsers();
        assertEquals(0, users.size());  // Não há usuários para excluir
    }
}
