package com.example.businesscard.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.businesscard.dto.BusinessCardRequest;
import com.example.businesscard.dto.BusinessCardResponse;
import com.example.businesscard.exception.ResourceNotFoundException;
import com.example.businesscard.service.BusinessCardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BusinessCardController.class)
class BusinessCardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BusinessCardService service;

    @Test
    void createCardShouldReturnCreated() throws Exception {
        BusinessCardRequest request = new BusinessCardRequest("John Doe", "Acme", "Engineer", "john@example.com", "+1234567890", "123 St");
        BusinessCardResponse response = new BusinessCardResponse(1L, "John Doe", "Acme", "Engineer", "john@example.com", "+1234567890", "123 St");
        when(service.createCard(any(BusinessCardRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    void getCardShouldReturnCard() throws Exception {
        BusinessCardResponse response = new BusinessCardResponse(1L, "John Doe", "Acme", "Engineer", "john@example.com", "+1234567890", "123 St");
        when(service.getCardById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/cards/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getCardShouldReturnNotFound() throws Exception {
        when(service.getCardById(1L)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/api/cards/1"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getCardsShouldReturnList() throws Exception {
        BusinessCardResponse response = new BusinessCardResponse(1L, "John Doe", "Acme", "Engineer", "john@example.com", "+1234567890", "123 St");
        when(service.getAllCards(null, null)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/cards"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getCardsShouldApplyFilter() throws Exception {
        BusinessCardResponse response = new BusinessCardResponse(1L, "John Doe", "Acme", "Engineer", "john@example.com", "+1234567890", "123 St");
        when(service.getAllCards("John", null)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/cards").param("name", "John"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].fullName").value("John Doe"));
    }

    @Test
    void updateCardShouldReturnUpdated() throws Exception {
        BusinessCardRequest request = new BusinessCardRequest("John Doe", "Acme", "Engineer", "john@example.com", "+1234567890", "123 St");
        BusinessCardResponse response = new BusinessCardResponse(1L, "John Doe", "Acme", "Engineer", "john@example.com", "+1234567890", "123 St");
        when(service.updateCard(eq(1L), any(BusinessCardRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/cards/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteCardShouldReturnNoContent() throws Exception {
        doNothing().when(service).deleteCard(1L);

        mockMvc.perform(delete("/api/cards/1"))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
    }

    @Test
    void createCardShouldReturnBadRequestWhenValidationFails() throws Exception {
        BusinessCardRequest request = new BusinessCardRequest("", "Acme", "Engineer", "invalid-email", "123", "123 St");

        mockMvc.perform(post("/api/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}
