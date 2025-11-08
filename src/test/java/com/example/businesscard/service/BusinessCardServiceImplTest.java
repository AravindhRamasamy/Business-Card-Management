package com.example.businesscard.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.businesscard.domain.BusinessCard;
import com.example.businesscard.dto.BusinessCardRequest;
import com.example.businesscard.dto.BusinessCardResponse;
import com.example.businesscard.exception.ResourceNotFoundException;
import com.example.businesscard.mapper.BusinessCardMapper;
import com.example.businesscard.repository.BusinessCardRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BusinessCardServiceImplTest {

    @Mock
    private BusinessCardRepository repository;

    @Mock
    private BusinessCardMapper mapper;

    @InjectMocks
    private BusinessCardServiceImpl service;

    private BusinessCardRequest request;
    private BusinessCard entity;
    private BusinessCardResponse response;

    @BeforeEach
    void setUp() {
        request = new BusinessCardRequest("John Doe", "Acme", "Engineer", "john@example.com", "+1234567890", "123 St");
        entity = new BusinessCard(1L, "John Doe", "Acme", "Engineer", "john@example.com", "+1234567890", "123 St");
        response = new BusinessCardResponse(1L, "John Doe", "Acme", "Engineer", "john@example.com", "+1234567890", "123 St");
    }

    @Test
    void createCardShouldSaveEntity() {
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        BusinessCardResponse result = service.createCard(request);

        assertThat(result).isEqualTo(response);
        verify(repository).save(entity);
    }

    @Test
    void getCardByIdShouldReturnResponseWhenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        BusinessCardResponse result = service.getCardById(1L);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void getCardByIdShouldThrowWhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getCardById(1L))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAllCardsShouldUseNameFilterWhenProvided() {
        when(repository.findByFullNameContainingIgnoreCase("John")).thenReturn(Collections.singletonList(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<BusinessCardResponse> results = service.getAllCards("John", null);

        assertThat(results).containsExactly(response);
        verify(repository, never()).findByCompanyContainingIgnoreCase(any());
    }

    @Test
    void getAllCardsShouldUseCompanyFilterWhenProvided() {
        when(repository.findByCompanyContainingIgnoreCase("Acme")).thenReturn(Collections.singletonList(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<BusinessCardResponse> results = service.getAllCards(null, "Acme");

        assertThat(results).containsExactly(response);
        verify(repository, never()).findAll();
    }

    @Test
    void getAllCardsShouldReturnAllWhenNoFilters() {
        when(repository.findAll()).thenReturn(Collections.singletonList(entity));
        when(mapper.toResponse(entity)).thenReturn(response);

        List<BusinessCardResponse> results = service.getAllCards(null, null);

        assertThat(results).containsExactly(response);
    }

    @Test
    void updateCardShouldSaveUpdatedEntity() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(response);

        BusinessCardResponse result = service.updateCard(1L, request);

        assertThat(result).isEqualTo(response);
        verify(mapper).updateEntity(eq(request), eq(entity));
        verify(repository).save(entity);
    }

    @Test
    void updateCardShouldThrowWhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCard(1L, request))
            .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).save(any());
    }

    @Test
    void deleteCardShouldDeleteWhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.deleteCard(1L);

        verify(repository).delete(entity);
    }

    @Test
    void deleteCardShouldThrowWhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteCard(1L))
            .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).delete(any());
    }
}
