package itau.case_backend.adapters.input;

import itau.case_backend.domain.entities.User;
import itau.case_backend.ports.input.UserInputPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
class UserAdapterInTest {

    private MockMvc mockMvc;

    @Mock
    private UserInputPort userInputPort;

    @InjectMocks
    private UserAdapterIn userAdapterIn;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userAdapterIn).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userInputPort.getAllUsers()).thenReturn(List.of(new User(1, "John Doe", "john.doe@example.com", 25)));

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].age").value(25));
    }

    @Test
    void testGetUserById_UserFound() throws Exception {
        when(userInputPort.getUserById(1)).thenReturn(new User(1, "John Doe", "john.doe@example.com", 25));

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void testGetUserById_UserNotFound() throws Exception {
        when(userInputPort.getUserById(1)).thenReturn(null);

        mockMvc.perform(get("/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado."));
    }

    @Test
    void testCreateUser() throws Exception {
        User newUser = new User(1, "John Doe", "john.doe@example.com", 25);
        when(userInputPort.createUser(any(User.class))).thenReturn(newUser);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\",\"age\":25}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void testUpdateUser() throws Exception {
        User updatedUser = new User(1, "John Smith", "john.smith@example.com", 26);

        when(userInputPort.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Smith\",\"email\":\"john.smith@example.com\",\"age\":26}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.email").value("john.smith@example.com"))
                .andExpect(jsonPath("$.age").value(26));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        User updatedUser = new User(1, "John Smith", "john.smith@example.com", 26);
        when(userInputPort.updateUser(1, updatedUser)).thenReturn(null);

        mockMvc.perform(put("/user/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Smith\",\"email\":\"john.smith@example.com\",\"age\":26}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Usuário não encontrado."));
    }

    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/user/1"))
                .andExpect(status().isNoContent());

        verify(userInputPort, times(1)).deleteUser(1);
    }


}
