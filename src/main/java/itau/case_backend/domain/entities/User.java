package itau.case_backend.domain.entities;

/**
 * Representa um usuário no sistema com ID, nome, e-mail e idade.
 */
public class User {

    /** O identificador único do usuário. */
    private long id;

    /** O nome do usuário. */
    private String name;

    /** O e-mail do usuário. */
    private String email;

    /** A idade do usuário. */
    private Integer age;

    /**
     * Construtor para criar um novo usuário.
     *
     * @param id    o identificador único do usuário
     * @param name  o nome do usuário
     * @param email o e-mail do usuário
     * @param age   a idade do usuário
     */
    public User(long id, String name, String email, Integer age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }


    /**
     * Obtém o ID do usuário.
     *
     * @return o ID do usuário
     */
    public long getId() {
        return id;
    }

    /**
     * Define o ID do usuário.
     *
     * @param id o novo ID do usuário
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Obtém o nome do usuário.
     *
     * @return o nome do usuário
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do usuário.
     *
     * @param name o novo nome do usuário
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtém o e-mail do usuário.
     *
     * @return o e-mail do usuário
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define o e-mail do usuário.
     *
     * @param email o novo e-mail do usuário
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtém a idade do usuário.
     *
     * @return a idade do usuário
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Define a idade do usuário.
     *
     * @param age a nova idade do usuário
     */
    public void setAge(Integer age) {
        this.age = age;
    }
}
