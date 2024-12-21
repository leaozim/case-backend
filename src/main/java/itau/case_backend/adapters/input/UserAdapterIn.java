package itau.case_backend.adapters.input;

import itau.case_backend.domain.UserServiceImpl;
import itau.case_backend.domain.entities.User;
import itau.case_backend.ports.input.UserInputPort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Adapter class that handles incoming user-related requests and delegates 
 * them to the input port.
 */
@RestController
@RequestMapping("/user")
public class UserAdapterIn {

    @Autowired
    private final UserInputPort userInputPort;

    /**
     * Constructs the UserAdapterIn with the specified input port.
     *
     * @param userInputPort the input port for user operations.
     */
    public UserAdapterIn(UserInputPort userInputPort) {
        this.userInputPort = userInputPort;
    }

    /**
     * Retrieves all users.
     *
     * @return a {@link ResponseEntity} containing the list of all users or an error message.
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userInputPort.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao recuperar usuários: " + e.getMessage());
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve.
     * @return a {@link ResponseEntity} containing the user or an error message.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id) {
        try {
            User user = userInputPort.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao recuperar usuário: " + e.getMessage());
        }
    }

    /**
     * Creates a new user.
     *
     * @param user the user to create.
     * @return a {@link ResponseEntity} containing the created user or an error message.
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userInputPort.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    /**
     * Updates an existing user.
     *
     * @param id   the ID of the user to update.
     * @param user the updated user data.
     * @return a {@link ResponseEntity} containing the updated user or an error message.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable long id, @RequestBody User user) {
        try {
            User updatedUser = userInputPort.updateUser(id, user);
            if (updatedUser != null) {
                return ResponseEntity.ok(updatedUser);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete.
     * @return a {@link ResponseEntity} indicating the result of the deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable long id) {
        try {
            userInputPort.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao remover usuário: " + e.getMessage());
        }
    }
}
