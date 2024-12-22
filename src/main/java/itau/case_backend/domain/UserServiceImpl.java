package itau.case_backend.domain;

import itau.case_backend.config.exception.EmailAlreadyExistsException;
import itau.case_backend.config.exception.UserNotFoundException;
import itau.case_backend.config.exception.UsersRetrievalException;
import itau.case_backend.domain.dtos.UserDTO;
import itau.case_backend.domain.dtos.UserPartialUpdateDTO;
import itau.case_backend.domain.entities.User;
import itau.case_backend.ports.input.UserInputPort;
import itau.case_backend.ports.output.UserOutputPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Serviço que implementa a lógica de negócios para o gerenciamento de usuários.
 * Esta classe serve como a porta de entrada (input port) para a aplicação.
 * Os dados são armazenados temporariamente e não há persistência duradoura, sendo utilizado um repositório (output port).
 *
 * <p>Esta classe gerencia as operações de criação, atualização, exclusão e consulta de usuários.</p>
 *
 * @see UserInputPort
 * @see UserOutputPort
 */
@Service
public class UserServiceImpl implements UserInputPort {

    private final UserOutputPort userRepository;

    public UserServiceImpl(UserOutputPort userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retorna todos os usuários cadastrados.
     *
     * @return Lista de usuários.
     * @throws UsersRetrievalException Se nenhum usuário for encontrado.
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAllUsers();
        if (users.isEmpty()) throw new UsersRetrievalException();
        return users;
    }

    /**
     * Retorna um usuário pelo ID.
     *
     * @param id ID do usuário.
     * @return Usuário correspondente.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     */
    @Override
    public User getUserById(long id) {
        return userRepository.findUserById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Cria um novo usuário.
     *
     * @param userDTO Dados do novo usuário.
     * @return Usuário criado.
     * @throws EmailAlreadyExistsException Se o e-mail já estiver em uso.
     */
    @Override
    public User createUser(UserDTO userDTO) {
        if (userRepository.findAllUsers().stream()
                .anyMatch(u -> u.getEmail().equals(userDTO.getEmail()))) {
            throw new EmailAlreadyExistsException(userDTO.getEmail());
        }
        return userRepository.saveUser(userDTO.toEntity());
    }

    /**
     * Atualiza completamente um usuário pelo ID.
     *
     * @param id      ID do usuário.
     * @param userDTO Novos dados do usuário.
     * @return Usuário atualizado.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     * @throws EmailAlreadyExistsException Se o e-mail já estiver em uso.
     */
    @Override
    public User updateUser(long id, UserDTO userDTO) {
        if (userRepository.findAllUsers().stream()
                .anyMatch(u -> u.getEmail().equals(userDTO.getEmail()))) {
            throw new EmailAlreadyExistsException(userDTO.getEmail());
        }
        return userRepository.findUserById(id)
                .map(user -> {
                    user.setName(userDTO.getName());
                    user.setEmail(userDTO.getEmail());
                    user.setAge(userDTO.getAge());
                    return userRepository.saveUser(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Atualiza parcialmente um usuário pelo ID.
     *
     * @param id              ID do usuário.
     * @param updatedUserDTO  Dados parciais para atualização.
     * @return Usuário atualizado.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     * @throws EmailAlreadyExistsException Se o e-mail já estiver em uso.
     */
    @Override
    public User updatePartialUser(long id, UserPartialUpdateDTO updatedUserDTO) {
        if (userRepository.findAllUsers().stream()
                .anyMatch(u -> u.getEmail().equals(updatedUserDTO.getEmail()))) {
            throw new EmailAlreadyExistsException(updatedUserDTO.getEmail());
        }
        return userRepository.findUserById(id)
                .map(user -> {
                    Optional.ofNullable(updatedUserDTO.getName()).ifPresent(user::setName);
                    Optional.ofNullable(updatedUserDTO.getEmail()).ifPresent(user::setEmail);
                    Optional.ofNullable(updatedUserDTO.getAge()).ifPresent(user::setAge);
                    return userRepository.saveUser(user);
                })
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    /**
     * Exclui um usuário pelo ID.
     *
     * @param id ID do usuário.
     * @throws UserNotFoundException Se o usuário não for encontrado.
     */
    @Override
    public void deleteUser(long id) {
        if (!userRepository.findUserById(id).isPresent()) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteUserById(id);
    }
}
