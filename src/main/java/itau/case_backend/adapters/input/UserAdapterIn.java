package itau.case_backend.adapters.input;

import itau.case_backend.domain.dtos.UserDTO;
import itau.case_backend.domain.dtos.UserPartialUpdateDTO;
import itau.case_backend.domain.entities.User;
import itau.case_backend.ports.input.UserInputPort;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gerenciar operações relacionadas a usuários.
 *
 * Este controlador atua como uma interface de entrada para manipulação de usuários,
 * delegando as operações à camada de serviço através do {@link UserInputPort}.
 */
@RestController
@RequestMapping("/user")
public class UserAdapterIn {

    @Autowired
    private final UserInputPort userInputPort;

    /**
     * Construtor para injeção de dependência.
     *
     * @param userInputPort Porta de entrada para operações de usuários.
     */
    public UserAdapterIn(UserInputPort userInputPort) {
        this.userInputPort = userInputPort;
    }

    /**
     * Recupera todos os usuários.
     *
     * @return Lista de todos os usuários cadastrados.
     */
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userInputPort.getAllUsers());
    }

    /**
     * Recupera um usuário pelo ID.
     *
     * @param id ID do usuário.
     * @return Usuário correspondente ao ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable long id) {
        return ResponseEntity.ok(userInputPort.getUserById(id));
    }


    /**
     * Cria um novo usuário.
     *
     * @param userDTO Dados do novo usuário.
     * @return Usuário criado com sucesso.
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userInputPort.createUser(userDTO));
    }

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id      ID do usuário a ser atualizado.
     * @param userDTO Dados atualizados do usuário.
     * @return Usuário atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @PathVariable long id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userInputPort.updateUser(id, userDTO));
    }

    /**
     * Atualiza parcialmente os dados de um usuário existente.
     *
     * @param id                   ID do usuário a ser atualizado.
     * @param userPartialUpdateDTO Campos a serem atualizados.
     * @return Usuário com os campos atualizados.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartialUser(@Valid @PathVariable long id, @RequestBody UserPartialUpdateDTO userPartialUpdateDTO) {
        return ResponseEntity.ok(userInputPort.updatePartialUser(id, userPartialUpdateDTO));
    }

    /**
     * Exclui um usuário pelo ID.
     *
     * @param id ID do usuário a ser excluído.
     * @return Confirmação de exclusão (sem conteúdo no corpo da resposta).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userInputPort.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
