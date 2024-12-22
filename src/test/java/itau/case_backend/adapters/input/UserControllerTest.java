package itau.case_backend.adapters.input;

import itau.case_backend.adapters.output.UserRepository;
import itau.case_backend.config.exception.EmailAlreadyExistsException;
import itau.case_backend.config.exception.UserNotFoundException;
import itau.case_backend.domain.dtos.UserDTO;
import itau.case_backend.domain.entities.User;
import itau.case_backend.ports.input.UserInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
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
    void When_ExistingUser_Expect_ReturnUser() throws Exception {
        when(userInputPort.getUserById(1)).thenReturn(new User(1, "John Doe", "john.doe@example.com", 25));

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(25));
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
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(25));

        verify(userInputPort, times(1)).createUser(any(UserDTO.class));
    }

    @Test
    void When_ValidUpdateData_Expect_UpdateUserSuccessfully() throws Exception {
        User updatedUser = new User(1, "John Smith", "john.smith@example.com", 26);

        when(userInputPort.updateUser(eq(1L), any(UserDTO.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Smith\",\"email\":\"john.smith@example.com\",\"age\":26}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.email").value("john.smith@example.com"))
                .andExpect(jsonPath("$.age").value(26));
    }

    @Test
    void When_EmailAlreadyExists_Expect_ReturnConflictError() throws Exception {
        UserDTO userDTO = new UserDTO("John Smith", "smith.doe@example.com", 26);

        when(userInputPort.updateUser(eq(1L), any(UserDTO.class)))
                .thenThrow(new EmailAlreadyExistsException(userDTO.getEmail()));

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Smith\",\"email\":\"smith.doe@example.com\",\"age\":26}"))
                .andExpect(status().isConflict())  // Espera o status HTTP 409
                .andExpect(jsonPath("$.errors[0]").value("O e-mail fornecido (smith.doe@example.com) já está cadastrado em nosso sistema. Por favor, insira um e-mail diferente."));
    }

    @Test
    void When_NonExistentUser_Expect_ReturnNotFoundErrorOnUpdate() throws Exception {
        UserDTO userDTO = new UserDTO("John Smith", "smith.doe@example.com", 26);

        when(userInputPort.updateUser(eq(1L), any(UserDTO.class)))
                .thenThrow(new UserNotFoundException(userDTO.toEntity().getId()));

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Smith\",\"email\":\"smith.doe@example.com\",\"age\":26}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errors[0]").value("Usuário com id 0 não encontrado."));
    }



    @Test
    void When_ValidUserId_Expect_DeleteUserSuccessfully() throws Exception {
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());

        verify(userInputPort, times(1)).deleteUser(1);
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
        String partialUpdateData = "{\"name\": \"\"}"; // Invalid name

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(partialUpdateData))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("O nome deve ter pelo menos 1 caractere"));
    }
}
