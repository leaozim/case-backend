package itau.case_backend.adapters.input;
import java.util.Collections;
import java.util.List;

import itau.case_backend.config.exception.EmailAlreadyExistsException;
import itau.case_backend.config.exception.UserNotFoundException;
import itau.case_backend.domain.dtos.UserDTO;
import itau.case_backend.domain.dtos.UserPartialUpdateDTO;
import itau.case_backend.domain.entities.User;
import itau.case_backend.ports.input.UserInputPort;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserInputPort userInputPort;

    @Test
    void When_ExistingUsers_Expect_ReturnUserList() throws Exception {
        User user = new User(1, "John Doe", "john.doe@example.com", 25);
        when(userInputPort.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(user.getName()))
                .andExpect(jsonPath("$[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$[0].age").value(user.getAge()));
    }

    @Test
    void When_NoUsersExist_Expect_EmptyList() throws Exception {
        when(userInputPort.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void When_ExistingUser_Expect_ReturnUser() throws Exception {
        User user = new User(1, "John Doe", "john.doe@example.com", 25);
        when(userInputPort.getUserById(1)).thenReturn(user);

        mockMvc.perform(get("/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.age").value(user.getAge()));
    }

    @Test
    void When_NonExistentUser_Expect_ReturnNotFoundError() throws Exception {
        long id = 1;
        when(userInputPort.getUserById(id)).thenThrow(new UserNotFoundException(id));

        mockMvc.perform(get("/users/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]").value("Usuário com id " + id + " não encontrado."));

    }

    @Test
    void When_ValidUserData_Expect_CreateUserSuccessfully() throws Exception {
        User newUser = new User(1, "John Doe", "john.doe@example.com", 25);

        when(userInputPort.createUser(any(UserDTO.class))).thenReturn(newUser);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"age\":25}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(newUser.getName()))
                .andExpect(jsonPath("$.email").value(newUser.getEmail()))
                .andExpect(jsonPath("$.age").value(newUser.getAge()));

        verify(userInputPort, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    void When_EmailAlreadyExists_Expect_CreateReturnConflictError() throws Exception {
        UserDTO userDTO = new UserDTO("John Smith", "john.doe@example.com", 26);

        when(userInputPort.createUser(any(UserDTO.class)))
                .thenThrow(new EmailAlreadyExistsException(userDTO.getEmail()));

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Smith\",\"email\":\"john.doe@example.com\",\"age\":26}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors[0]").value("O e-mail fornecido (john.doe@example.com) já está cadastrado em nosso sistema. Por favor, insira um e-mail diferente."));
    }


    @Test
    void When_ValidUpdateData_Expect_UpdateUserSuccessfully() throws Exception {
        User updatedUser = new User(1, "John Smith", "john.smith@example.com", 26);

        when(userInputPort.updateUser(eq(updatedUser.getId()), any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/" + updatedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Smith\",\"email\":\"john.smith@example.com\",\"age\":26}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedUser.getName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.age").value(updatedUser.getAge()));
    }

    @Test
    void When_EmailAlreadyExists_Expect_UpdateReturnConflictError() throws Exception {
        UserDTO userDTO = new UserDTO("John Smith", "smith.doe@example.com", 26);
        long id = 1;

        when(userInputPort.updateUser(eq(id), any(UserDTO.class)))
                .thenThrow(new EmailAlreadyExistsException(userDTO.getEmail()));

        mockMvc.perform(put("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Smith\",\"email\":\"smith.doe@example.com\",\"age\":26}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors[0]").value("O e-mail fornecido (smith.doe@example.com) já está cadastrado em nosso sistema. Por favor, insira um e-mail diferente."));
    }

    @Test
    void When_NonExistentUser_Expect_ReturnNotFoundErrorOnUpdate() throws Exception {
        UserDTO userDTO = new UserDTO("John Smith", "smith.doe@example.com", 26);
        long id = 1;

        when(userInputPort.updateUser(eq(id), any(UserDTO.class)))
                .thenThrow(new UserNotFoundException(userDTO.toEntity().getId()));

        mockMvc.perform(put("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Smith\",\"email\":\"smith.doe@example.com\",\"age\":26}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]").value("Usuário com id 0 não encontrado."));
    }


    @Test
    void When_EmailAlreadyExistsOnPartialUpdate_Expect_ReturnConflictError() throws Exception {
        String partialUpdateData = "{\"email\": \"john.doe@example.com\"}";
        long id = 1;

        when(userInputPort.partialUpdateUser(eq(id), any(UserPartialUpdateDTO.class)))
                .thenThrow(new EmailAlreadyExistsException("john.doe@example.com"));

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partialUpdateData))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errors").value("O e-mail fornecido (john.doe@example.com) já está cadastrado em nosso sistema. Por favor, insira um e-mail diferente."));
    }

    @Test
    void When_NonExistentUserOnPartialUpdate_Expect_ReturnNotFoundError() throws Exception {
        String partialUpdateData = "{\"name\": \"John Smith\", \"email\": \"john.smith@example.com\", \"age\": 30}";
        long id = 1;

        when(userInputPort.partialUpdateUser(eq(id), any(UserPartialUpdateDTO.class)))
                .thenThrow(new UserNotFoundException(id));

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partialUpdateData))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]").value("Usuário com id " + id + " não encontrado."));
    }

    @Test
    void When_ValidPartialUpdateData_Expect_UpdateUserSuccessfully() throws Exception {
        String partialUpdateData = "{\"name\": \"John Smith\", \"email\": \"john.smith@example.com\", \"age\": 30}";
        long id = 1;

        User updatedUser = new User(id, "John Smith", "john.smith@example.com", 30);

        when(userInputPort.partialUpdateUser(eq(id), any(UserPartialUpdateDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partialUpdateData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedUser.getName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.age").value(updatedUser.getAge()));

        verify(userInputPort, times(1)).partialUpdateUser(eq(id), any(UserPartialUpdateDTO.class));
    }
    @Test
    void When_UpdatingNameOnly_Expect_NameUpdatedSuccessfully() throws Exception {
        String partialUpdateData = "{\"name\": \"John Doe Updated\"}";
        long id = 1;

        User updatedUser = new User(id, "John Doe Updated", "john.doe@example.com", 25);

        when(userInputPort.partialUpdateUser(eq(id), any(UserPartialUpdateDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partialUpdateData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedUser.getName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.age").value(updatedUser.getAge()));

        verify(userInputPort, times(1)).partialUpdateUser(eq(id), any(UserPartialUpdateDTO.class));
    }

    @Test
    void When_UpdatingAgeOnly_Expect_AgeUpdatedSuccessfully() throws Exception {
        String partialUpdateData = "{\"age\": 30}";
        long id = 1;

        User updatedUser = new User(id, "John Doe", "john.doe@example.com", 30);

        when(userInputPort.partialUpdateUser(eq(id), any(UserPartialUpdateDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partialUpdateData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedUser.getName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.age").value(updatedUser.getAge()));

        verify(userInputPort, times(1)).partialUpdateUser(eq(id), any(UserPartialUpdateDTO.class));
    }

    @Test
    void When_UpdatingEmailOnly_Expect_EmailUpdatedSuccessfully() throws Exception {
        String partialUpdateData = "{\"email\": \"john.doe.updated@example.com\"}";
        long id = 1;

        User updatedUser = new User(id, "John Doe", "john.doe.updated@example.com", 25);

        when(userInputPort.partialUpdateUser(eq(id), any(UserPartialUpdateDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(patch("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partialUpdateData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedUser.getName()))
                .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
                .andExpect(jsonPath("$.age").value(updatedUser.getAge()));

        verify(userInputPort, times(1)).partialUpdateUser(eq(id), any(UserPartialUpdateDTO.class));
    }

    @Test
    void When_ValidUserId_Expect_DeleteUserSuccessfully() throws Exception {
        long id = 1;

        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isNoContent());

        verify(userInputPort, times(1)).deleteUser(id);
    }

    @Test
    void When_NonExistentUser_Expect_DeleteReturnNotFoundErrorOnUpdate() throws Exception {
        long id = 1;

        doThrow(new UserNotFoundException(id))
                .when(userInputPort).deleteUser(eq(id));

        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]").value("Usuário com id " + id + " não encontrado."));
    }


    @Test
    public void When_BlankName_Expect_ReturnValidationError() throws Exception {
        String invalidUserData = "{\"name\": \"\", \"email\": \"valid@example.com\", \"age\": 25}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("O nome não pode estar vazio"));
    }

    @Test
    public void When_InvalidEmail_Expect_ReturnValidationError() throws Exception {
        String invalidUserData = "{\"name\": \"John\", \"email\": \"invalid-email\", \"age\": 25}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("Formato de e-mail inválido"));
    }

    @Test
    public void When_AgeLessThanOne_Expect_ReturnValidationError() throws Exception {
        String invalidUserData = "{\"name\": \"John\", \"email\": \"john@example.com\", \"age\": 0}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("A idade deve ser maior que 0"));
    }

    @Test
    public void When_NullAge_Expect_ReturnValidationError() throws Exception {
        String invalidUserData = "{\"name\": \"John\", \"email\": \"john@example.com\", \"age\": null}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("A idade não pode ser nula"));
    }

    @Test
    public void When_BlankEmail_Expect_ReturnValidationError() throws Exception {
        String invalidUserData = "{\"name\": \"Valid Name\", \"email\": \"\", \"age\": 25}";

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidUserData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("O e-mail não pode estar vazio"));
    }

    @Test
    public void When_InvalidEmailOnPartialUpdate_Expect_ReturnValidationError() throws Exception {
        String partialUpdateData = "{\"email\": \"invalidemail\"}"; // Invalid email

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partialUpdateData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("Formato de e-mail inválido"));
    }

    @Test
    public void When_InvalidNameOnPartialUpdate_Expect_ReturnValidationError() throws Exception {
        String partialUpdateData = "{\"name\": \"\"}";

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partialUpdateData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("O nome deve ter pelo menos 1 caractere"));
    }

    @Test
    public void When_BlankEmailOnPartialUpdate_Expect_ReturnValidationError() throws Exception {
        String partialUpdateData = "{\"email\": \"\"}";

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partialUpdateData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("O e-mail não pode estar vazio"));
    }

}
