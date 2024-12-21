package itau.case_backend.config.exception;

/**
 * Exceção personalizada para indicar que um e-mail já está cadastrado no sistema.
 *
 * Esta exceção é utilizada para lançar uma mensagem clara e específica ao cliente
 * quando uma tentativa de cadastro é feita com um e-mail já existente.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    /**
     * Construtor da exceção.
     *
     * @param email O e-mail que já está cadastrado.
     */
    public EmailAlreadyExistsException(String email) {
        super(String.format("O e-mail fornecido (%s) já está cadastrado em nosso sistema. Por favor, insira um e-mail diferente.", email));
    }

}
