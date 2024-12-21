package itau.case_backend.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import itau.case_backend.domain.entities.User;

/**
 * DTO (Data Transfer Object) para transferência e validação dos dados de um usuário.
 *
 * <p>Utilizado para validar e transferir dados de usuário entre as camadas da aplicação.
 * Garante que os dados estejam no formato correto antes de serem processados ou armazenados.</p>
 *
 * <ul>
 *   <li><b>name:</b> Não pode estar vazio.</li>
 *   <li><b>email:</b> Deve ser um formato válido e não pode estar vazio.</li>
 *   <li><b>age:</b> Deve ser maior que 0 e não pode ser nulo.</li>
 * </ul>
 *
 * @see User
 */
public class UserDTO {

    /** Nome do usuário. */
    @NotBlank(message = "O nome não pode estar vazio")
    private String name;

    /** E-mail do usuário. */
    @NotBlank(message = "O e-mail não pode estar vazio")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    /** Idade do usuário. */
    @NotNull(message = "A idade não pode ser nula")
    @Min(value = 1, message = "A idade deve ser maior que 0")
    private Integer age;

    /**
     * Construtor padrão.
     */
    public UserDTO() {}

    /**
     * Construtor com todos os atributos.
     *
     * @param name  Nome do usuário.
     * @param email E-mail do usuário.
     * @param age   Idade do usuário.
     */
    public UserDTO(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    /**
     * Retorna o nome do usuário.
     *
     * @return Nome do usuário.
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do usuário.
     *
     * @param name Nome do usuário.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retorna o e-mail do usuário.
     *
     * @return E-mail do usuário.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o e-mail do usuário.
     *
     * @param email E-mail do usuário.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Retorna a idade do usuário.
     *
     * @return Idade do usuário.
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Define a idade do usuário.
     *
     * @param age Idade do usuário.
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * Converte este DTO em uma entidade {@link User}.
     *
     * <p>Cria uma nova instância de {@link User} utilizando os dados contidos neste DTO.</p>
     *
     * @return Um objeto {@link User} representando o usuário.
     */
    public User toEntity() {
        return new User(0, this.name, this.email, this.age);
    }
}
