package com.example.businesscard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.businesscard.dto.BusinessCardRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class BusinessCardApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullCrudFlowWithAuthentication() throws Exception {
        String loginBody = "{\"username\":\"admin\",\"password\":\"password\"}";
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginBody))
            .andExpect(status().isOk())
            .andReturn();
        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("token").asText();

        BusinessCardRequest createRequest = new BusinessCardRequest("John Doe", "Acme", "Engineer", "john@example.com", "+1234567890", "123 St");
        MvcResult createResult = mockMvc.perform(post("/api/cards")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
            .andExpect(status().isCreated())
            .andReturn();
        JsonNode createdNode = objectMapper.readTree(createResult.getResponse().getContentAsString());
        long id = createdNode.get("id").asLong();

        mockMvc.perform(get("/api/cards")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/cards/" + id)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(result -> {
                JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
                assertThat(node.get("id").asLong()).isEqualTo(id);
            });

        BusinessCardRequest updateRequest = new BusinessCardRequest("John Updated", "Acme", "Senior Engineer", "john.updated@example.com", "+1234567890", "456 Ave");
        mockMvc.perform(put("/api/cards/" + id)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(result -> {
                JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
                assertThat(node.get("fullName").asText()).isEqualTo("John Updated");
            });

        mockMvc.perform(delete("/api/cards/" + id)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/cards/" + id)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isNotFound());
    }
}
