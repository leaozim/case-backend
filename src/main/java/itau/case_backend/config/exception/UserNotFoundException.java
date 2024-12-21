package itau.case_backend.config.exception;

/**
 * Exceção personalizada para indicar que um usuário não foi encontrado no sistema.
 *
 * Esta exceção é lançada quando uma operação busca um usuário com um ID inexistente.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Construtor da exceção.
     *
     * @param id O ID do usuário que não foi encontrado.
     */
    public UserNotFoundException(Long id) {
        super(String.format("Usuário com id %d não encontrado.", id));
    }

}
