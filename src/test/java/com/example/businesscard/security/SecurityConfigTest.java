package com.example.businesscard.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.businesscard.dto.BusinessCardResponse;
import com.example.businesscard.service.BusinessCardService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BusinessCardService businessCardService;

    @BeforeEach
    void setUp() {
        org.mockito.Mockito.when(businessCardService.getAllCards(null, null))
            .thenReturn(List.of(new BusinessCardResponse(1L, "John", "Acme", "Engineer", "john@example.com", "+123", "123 St")));
    }

    @Test
    void loginShouldBeAccessibleWithoutToken() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"password\"}"))
            .andExpect(status().isOk())
            .andReturn();

        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        assertThat(node.get("token").asText()).isNotBlank();
    }

    @Test
    void cardsEndpointShouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/cards"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void cardsEndpointShouldAllowAccessWithToken() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"admin\",\"password\":\"password\"}"))
            .andExpect(status().isOk())
            .andReturn();
        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString()).get("token").asText();

        mockMvc.perform(get("/api/cards").header("Authorization", "Bearer " + token))
            .andExpect(status().isOk());
    }
}
