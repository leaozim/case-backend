package itau.case_backend.ports.output;

import itau.case_backend.domain.entities.User;
import java.util.List;
import java.util.Optional;

/**
 * Define os métodos de saída para operações relacionadas a usuários.
 */
public interface UserOutputPort {

    /**
     * Recupera todos os usuários armazenados.
     *
     * @return uma lista de usuários
     */
    List<User> findAllUsers();

    /**
     * Recupera um usuário pelo ID.
     *
     * @param id o identificador único do usuário
     * @return um Optional contendo o usuário, se encontrado
     */
    Optional<User> findUserById(long id);

    /**
     * Salva um novo usuário ou atualiza um existente.
     *
     * @param user o objeto do usuário a ser salvo
     * @return o usuário salvo
     */
    User saveUser(User user);

    /**
     * Remove um usuário pelo ID.
     *
     * @param id o identificador único do usuário a ser removido
     */
    void deleteUserById(long id);
}
