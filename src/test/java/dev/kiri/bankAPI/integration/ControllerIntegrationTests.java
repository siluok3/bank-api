package dev.kiri.bankAPI.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerIntegrationTests {
    @Autowired private MockMvc mvc;

    @Test
    void itShouldHandleATransaction() throws Exception {
        mvc.perform(post("/api/v1/users")
                        .content("{\"username\": \"user1\", \"email\": \"user1@gmail.com\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(post("/api/v1/users")
                .content("{\"username\": \"user2\", \"email\": \"user2@gmail.com\"}")
                .contentType(MediaType.APPLICATION_JSON));

        mvc.perform(get("/api/v1/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists());

        mvc.perform(post("/api/v1/accounts/1")).andExpect(MockMvcResultMatchers.status().isOk());
        mvc.perform(post("/api/v1/accounts/2")).andExpect(MockMvcResultMatchers.status().isOk());

        mvc.perform(get("/api/v1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.balance").exists());

        mvc.perform(post("/api/v1/accounts/1/deposit")
                        .content("{\"amount\": 100}")
                        .contentType("application/json"))
                .andExpect(status().isOk());
        mvc.perform(post("/api/v1/accounts/2/deposit").content("{\"amount\": 50}")
                .contentType("application/json"))
                .andExpect(status().isOk());

        mvc.perform(post("/api/v1/transactions")
                .content("{\"fromAccount\": 1, \"toAccount\": 2, \"amount\": 50}")
                .contentType("application/json"))
                .andExpect(status().isOk());

        mvc.perform(get("/api/v1/transactions/account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sendTransactions").exists())
                .andExpect(jsonPath("$.receivedTransactions").exists());
        mvc.perform(get("/api/v1/transactions/account/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sendTransactions").exists())
                .andExpect(jsonPath("$.receivedTransactions").exists());
    }
}
