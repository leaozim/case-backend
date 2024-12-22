package itau.case_backend.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para atualização parcial de um usuário.
 *
 * <p>Permite a atualização de um ou mais campos de um usuário existente.</p>
 *
 * <ul>
 *   <li><b>name:</b> Deve conter pelo menos 2 caractere.</li>
 *   <li><b>email:</b> Deve estar em um formato válido de e-mail.</li>
 *   <li><b>age:</b> Deve ser maior que 0.</li>
 * </ul>
 */
public class UserPartialUpdateDTO {

    @Size(min = 1, message = "O nome deve ter pelo menos 1 caractere")
    private String name;

    @Size(min = 1, message = "O e-mail não pode estar vazio")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    @Min(value = 1, message = "A idade deve ser maior que 0")
    private Integer age;

    /**
     * Construtor padrão.
     */
    public UserPartialUpdateDTO() {}

    /**
     * Construtor com todos os campos.
     *
     * @param name Nome do usuário.
     * @param email E-mail do usuário.
     * @param age Idade do usuário.
     */
    public UserPartialUpdateDTO(String name, String email, Integer age) {
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
}
