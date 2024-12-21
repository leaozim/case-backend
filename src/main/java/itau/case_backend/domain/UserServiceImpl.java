package itau.case_backend.domain;

import itau.case_backend.domain.entities.User;
import itau.case_backend.ports.input.UserInputPort;
import itau.case_backend.ports.output.UserOutputPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço que implementa a lógica de negócios para gerenciamento de usuários.
 * Esta classe serve como a porta de entrada (input port) para a aplicação.
 * Os dados são armazenados temporariamente em um HashMap, sem persistência duradoura.
 */
@Service
public class UserServiceImpl implements UserInputPort {

    private final UserOutputPort userRepository;

    /**
     * Construtor para injeção de dependência do repositório de usuários.
     *
     * @param userRepository a implementação da porta de saída (output port) para persistência de dados
     */
    public UserServiceImpl(UserOutputPort userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Recupera todos os usuários.
     *
     * @return lista de todos os usuários cadastrados
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    /**
     * Recupera um usuário pelo ID.
     *
     * @param id o ID do usuário a ser recuperado
     * @return o usuário correspondente ao ID, ou null caso não encontrado
     */
    @Override
    public User getUserById(long id) {
        return userRepository.findUserById(id).orElse(null);
    }

    /**
     * Cria um novo usuário.
     *
     * @param user o usuário a ser criado
     * @return o usuário criado
     */
    @Override
    public User createUser(User user) {
        validateUser(user);
        if (userRepository.findAllUsers().stream().anyMatch(existingUser -> existingUser.getEmail().equals(user.getEmail()))) {
            throw new IllegalArgumentException("O e-mail fornecido já está cadastrado em nosso sistema. Por favor, insira um e-mail diferente.");
        }
        return userRepository.saveUser(user);
    }

    /**
     * Atualiza as informações de um usuário existente.
     *
     * @param id o ID do usuário a ser atualizado
     * @param updatedUser o objeto com as novas informações do usuário
     * @return o usuário atualizado, ou null caso o usuário não exista
     */
    @Override
    public User updateUser(long id, User updatedUser) {
        return userRepository.findUserById(id)
                .map(existingUser -> {
                    Optional.ofNullable(updatedUser.getName()).ifPresent(existingUser::setName);
                    Optional.ofNullable(updatedUser.getEmail()).ifPresent(existingUser::setEmail);
                    Optional.ofNullable(updatedUser.getAge()).ifPresent(existingUser::setAge);
                    return userRepository.saveUser(existingUser);
                })
                .orElse(null);
    }

    /**
     * Exclui um usuário pelo ID.
     *  * <p>
     *  * Verifica se o usuário existe. Se não, lança {@link IllegalArgumentException}.
     *  * Caso contrário, o usuário é removido do repositório.
     *  * </p>
     *
     * @param id o ID do usuário a ser excluído
     * @throws IllegalArgumentException se o usuário não for encontrado
     */
    @Override
    public void deleteUser(long id) {
        Optional<User> existingUser = userRepository.findUserById(id);
        if (!existingUser.isPresent()) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        userRepository.deleteUserById(id);
    }


    private void validateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new IllegalArgumentException("O nome do usuário é obrigatório.");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("O e-mail do usuário é obrigatório.");
        }
        if (user.getAge() == null) {
            throw new IllegalArgumentException("É necessário inserir a idade do usuário para realizar o cadastro.");
        }
        if (user.getAge() <= 0) {
            throw new IllegalArgumentException("A idade do usuário é obrigatória.");
        }
    }


}
