package itau.case_backend.ports.input;

import itau.case_backend.domain.entities.User;
import java.util.List;

/**
 * Define os métodos de entrada para operações relacionadas a usuários.
 */
public interface UserInputPort {

    /**
     * Busca todos os usuários cadastrados.
     *
     * @return uma lista de usuários
     */
    List<User> getAllUsers();

    /**
     * Busca um usuário pelo ID.
     *
     * @param id o identificador único do usuário
     * @return o usuário correspondente ao ID, ou null se não encontrado
     */
    User getUserById(long id);

    /**
     * Cria um novo usuário.
     *
     * @param user o objeto contendo os dados do novo usuário
     * @return o usuário criado com seu ID gerado
     */
    User createUser(User user);

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id o identificador único do usuário
     * @param user o objeto contendo os novos dados do usuário
     * @return o usuário atualizado
     */
    User updateUser(long id, User user);

    /**
     * Remove um usuário pelo ID.
     *
     * @param id o identificador único do usuário a ser removido
     */
    void deleteUser(long id);
}