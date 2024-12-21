package itau.case_backend.config.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe que centraliza o tratamento de exceções da aplicação.
 *
 * Esta classe utiliza a anotação @ControllerAdvice para interceptar exceções
 * lançadas pelos controladores e retornar respostas consistentes ao cliente.
 */
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Trata exceções de validação em métodos de entrada com argumentos inválidos.
     *
     * @param ex Exceção de argumentos inválidos (MethodArgumentNotValidException).
     * @param headers Cabeçalhos da requisição.
     * @param status Código de status HTTP associado ao erro.
     * @param request Objeto WebRequest com informações da requisição.
     * @return ResponseEntity contendo a mensagem de erro formatada.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());

        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(status, errors);

        return new ResponseEntity<>(apiErrorMessage, apiErrorMessage.getStatus());
    }

    /**
     * Trata exceções de usuário não encontrado.
     *
     * @param exception Exceção do tipo UserNotFoundException.
     * @param request Objeto WebRequest com informações da requisição.
     * @return ResponseEntity contendo a mensagem de erro formatada.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(
            UserNotFoundException exception, WebRequest request) {

        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
    }

    /**
     * Trata exceções de e-mail já existente.
     *
     * @param exception Exceção do tipo EmailAlreadyExistsException.
     * @param request Objeto WebRequest com informações da requisição.
     * @return ResponseEntity contendo a mensagem de erro formatada.
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException exception, WebRequest request) {

        ApiErrorMessage apiErrorMessage = new ApiErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());

        return new ResponseEntity<>(apiErrorMessage, new HttpHeaders(), apiErrorMessage.getStatus());
    }

}
