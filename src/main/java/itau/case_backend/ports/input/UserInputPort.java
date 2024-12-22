package itau.case_backend.ports.input;

import itau.case_backend.domain.dtos.UserDTO;
import itau.case_backend.domain.dtos.UserPartialUpdateDTO;
import itau.case_backend.domain.entities.User;
import java.util.List;

/**
 * Define os métodos de entrada para operações relacionadas a usuários.
 * <p>
 * Esta interface especifica os métodos que são usados para buscar, criar, atualizar e excluir usuários no sistema.
 * </p>
 */
public interface UserInputPort {

    /**
     * Busca todos os usuários cadastrados.
     *
     * @return uma lista de {@link User} contendo todos os usuários
     */
    List<User> getAllUsers();

    /**
     * Busca um usuário pelo ID.
     *
     * @param id o identificador único do usuário
     * @return o usuário correspondente ao ID, ou {@code null} se não encontrado
     */
    User getUserById(long id);

    /**
     * Cria um novo usuário com base nos dados fornecidos.
     *
     * @param userDTO o objeto {@link UserDTO} contendo os dados necessários para criar o usuário
     * @return o {@link User} criado com o ID gerado e outros dados persistidos
     * @throws IllegalArgumentException se os dados do usuário forem inválidos
     */
    User createUser(UserDTO userDTO);

    /**
     * Atualiza os dados de um usuário existente.
     *
     * @param id o identificador único do usuário
     * @param userDTO o objeto {@link UserDTO} contendo os novos dados do usuário
     * @return o {@link User} atualizado
     */
    User updateUser(long id, UserDTO userDTO);

    /**
     * Atualiza parcialmente os dados de um usuário existente.
     *
     * @param id o identificador único do usuário
     * @param updatedUserDTO o objeto {@link UserPartialUpdateDTO} contendo os dados atualizados
     * @return o {@link User} atualizado com os dados parciais
     */
    User updatePartialUser(long id, UserPartialUpdateDTO updatedUserDTO);

    /**
     * Remove um usuário pelo ID.
     *
     * @param id o identificador único do usuário a ser removido
     */
    void deleteUser(long id);
}
