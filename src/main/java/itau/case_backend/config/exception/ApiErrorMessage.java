package itau.case_backend.config.exception;

import org.springframework.http.HttpStatusCode;
import java.util.List;

/**
 * Classe que representa mensagens de erro retornadas pela API.
 *
 * Esta classe encapsula o código de status HTTP e uma lista de mensagens de erro,
 * proporcionando uma resposta consistente em caso de falhas.
 */
public class ApiErrorMessage {

    private HttpStatusCode status;
    private List<String> errors;

    /**
     * Construtor para criação de uma mensagem de erro com múltiplos detalhes.
     *
     * @param status o código de status HTTP do erro.
     * @param errors a lista de mensagens detalhando os erros.
     */
    public ApiErrorMessage(HttpStatusCode status, List<String> errors) {
        this.status = status;
        this.errors = errors;
    }

    /**
     * Construtor para criação de uma mensagem de erro com um único detalhe.
     *
     * @param status o código de status HTTP do erro.
     * @param error a mensagem descrevendo o erro.
     */
    public ApiErrorMessage(HttpStatusCode status, String error) {
        this.status = status;
        this.errors = List.of(error);
    }

    /**
     * Obtém o código de status HTTP do erro.
     *
     * @return o código de status HTTP.
     */
    public HttpStatusCode getStatus() {
        return status;
    }

    /**
     * Define o código de status HTTP do erro.
     *
     * @param status o código de status HTTP a ser definido.
     */
    public void setStatus(HttpStatusCode status) {
        this.status = status;
    }

    /**
     * Obtém a lista de mensagens de erro.
     *
     * @return a lista de mensagens de erro.
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Define a lista de mensagens de erro.
     *
     * @param errors a lista de mensagens a ser definida.
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
