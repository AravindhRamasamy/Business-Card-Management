package com.example.businesscard.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.businesscard.domain.BusinessCard;
import com.example.businesscard.dto.BusinessCardRequest;
import com.example.businesscard.dto.BusinessCardResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusinessCardMapperTest {

    private BusinessCardMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BusinessCardMapper();
    }

    @Test
    void shouldMapRequestToEntity() {
        BusinessCardRequest request = new BusinessCardRequest("John Doe", "Acme", "Engineer", "john.doe@example.com", "+1234567890", "123 Street");

        BusinessCard entity = mapper.toEntity(request);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getFullName()).isEqualTo("John Doe");
        assertThat(entity.getCompany()).isEqualTo("Acme");
    }

    @Test
    void shouldUpdateEntityFromRequest() {
        BusinessCardRequest request = new BusinessCardRequest("Jane Doe", "Beta", "Manager", "jane.doe@example.com", "+1987654321", "456 Avenue");
        BusinessCard entity = new BusinessCard(1L, "Old Name", "Old Co", "Old Title", "old@example.com", "+1000000000", "Old Address");

        mapper.updateEntity(request, entity);

        assertThat(entity.getFullName()).isEqualTo("Jane Doe");
        assertThat(entity.getCompany()).isEqualTo("Beta");
        assertThat(entity.getEmail()).isEqualTo("jane.doe@example.com");
    }

    @Test
    void shouldMapEntityToResponse() {
        BusinessCard entity = new BusinessCard(2L, "Alice", "Gamma", "Designer", "alice@example.com", "+1234567890", "789 Blvd");

        BusinessCardResponse response = mapper.toResponse(entity);

        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getFullName()).isEqualTo("Alice");
        assertThat(response.getCompany()).isEqualTo("Gamma");
    }
}
