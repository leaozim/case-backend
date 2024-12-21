package itau.case_backend.domain;

import itau.case_backend.domain.entities.User;
import itau.case_backend.ports.output.UserOutputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class UserServiceImplTest {
    @Mock
    private UserOutputPort userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_ShouldReturnCreatedUser() {
        User user = new User(0, "John Doe", "john.doe@example.com", 25);
        when(userRepository.saveUser(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("John Doe", createdUser.getName());
        assertEquals("john.doe@example.com", createdUser.getEmail());
        assertEquals(25, createdUser.getAge());

        verify(userRepository, times(1)).saveUser(user);
    }

    @Test
    public void testCreateUser_ShouldThrowException_WhenEmailExists() {
        User user = new User(0, "John Doe", "john.doe@example.com", 25);
        when(userRepository.findAllUsers()).thenReturn(List.of(user));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(user);
        });

        assertEquals("O e-mail fornecido já está cadastrado em nosso sistema. Por favor, insira um e-mail diferente.", thrown.getMessage());
    }

    @Test
    public void testGetUserById_ShouldReturnUser() {
        User user = new User(1, "John Doe", "john.doe@example.com", 25);
        when(userRepository.findUserById(1)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(1);

        assertNotNull(foundUser);
        assertEquals("John Doe", foundUser.getName());
    }


    @Test
    public void testUpdateUser_ShouldReturnUpdatedUser() {
        User existingUser = new User(1, "John Doe", "john.doe@example.com", 25);
        User updatedUser = new User(1, "John Smith", "john.smith@example.com", 26);
        when(userRepository.findUserById(1)).thenReturn(Optional.of(existingUser));
        when(userRepository.saveUser(any(User.class))).thenReturn(updatedUser);

        User result = userService.updateUser(1, updatedUser);

        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertEquals("john.smith@example.com", result.getEmail());
        assertEquals(26, result.getAge());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).saveUser(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(updatedUser.getName(), savedUser.getName());
        assertEquals(updatedUser.getEmail(), savedUser.getEmail());
        assertEquals(updatedUser.getAge(), savedUser.getAge());
    }



    @Test
    public void testDeleteUser_ShouldDeleteUser() {
        User user = new User(1, "John Doe", "john.doe@example.com", 25);
        when(userRepository.findUserById(1)).thenReturn(Optional.of(user));

        userService.deleteUser(1);

        verify(userRepository, times(1)).deleteUserById(1);
    }
}
