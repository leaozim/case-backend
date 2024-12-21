package itau.case_backend.config.exception;

/**
 * Exceção personalizada que indica erro ao recuperar usuários.
 * <p>
 * Esta exceção é uma subclasse de {@link RuntimeException} e é lançada quando há falhas ao tentar recuperar dados de usuários.
 * </p>
 */
public class UsersRetrievalException extends RuntimeException {

    /**
     * Constrói uma nova {@code UsersRetrievalException} com a mensagem padrão:
     * "Erro ao recuperar usuários".
     */
    public UsersRetrievalException() {
        super("Erro ao recuperar usuários");
    }

    /**
     * Constrói uma nova {@code UsersRetrievalException} com a mensagem padrão
     * e a causa especificada.
     *
     * @param cause A causa (exceção) que origina este erro.
     */
    public UsersRetrievalException(Throwable cause) {
        super("Erro ao recuperar usuários", cause);
    }
}
