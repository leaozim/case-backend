package itau.case_backend.adapters.output;

import itau.case_backend.domain.entities.User;
import org.springframework.stereotype.Repository;
import itau.case_backend.ports.output.UserOutputPort;
import java.util.*;

/**
 * Implementação do repositório de usuários, utilizando armazenamento em memória.
 * Esta classe serve como a porta de saída (output port) para persistência de dados.
 */
@Repository
public class UserRepository implements UserOutputPort {

    private final Map<Long, User> userMap = new HashMap<>();
    private long nextId = 1;

    /**
     * Recupera todos os usuários armazenados.
     *
     * @return lista de todos os usuários
     */
    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    /**
     * Recupera um usuário pelo ID.
     *
     * @param id o ID do usuário a ser recuperado
     * @return um Optional contendo o usuário, ou Optional.empty() se não encontrado
     */
    @Override
    public Optional<User> findUserById(long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    /**
     * Recupera um usuário pelo email.
     *
     * @param email o email do usuário a ser recuperado
     * @return um Optional contendo o usuário, ou Optional.empty() se não encontrado
     */
    public Optional<User> findUserByEmail(String email) {
        return userMap.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    /**
     * Salva ou atualiza um usuário no repositório.
     * Se o ID do usuário for 0, um novo ID é gerado automaticamente.
     *
     * @param user o usuário a ser salvo ou atualizado
     * @return o usuário salvo ou atualizado
     */
    @Override
    public User saveUser(User user) {
        if (user.getId() == 0) {
            user.setId(nextId++);
        }
        userMap.put(user.getId(), user);
        return user;
    }

    /**
     * Exclui um usuário pelo ID.
     *
     * @param id o ID do usuário a ser excluído
     */
    @Override
    public void deleteUserById(long id) {
        userMap.remove(id);
    }
}
