package com.example.businesscard.service;

import com.example.businesscard.domain.BusinessCard;
import com.example.businesscard.dto.BusinessCardRequest;
import com.example.businesscard.dto.BusinessCardResponse;
import com.example.businesscard.exception.ResourceNotFoundException;
import com.example.businesscard.mapper.BusinessCardMapper;
import com.example.businesscard.repository.BusinessCardRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BusinessCardServiceImpl implements BusinessCardService {

    private static final Logger log = LoggerFactory.getLogger(BusinessCardServiceImpl.class);

    private final BusinessCardRepository repository;
    private final BusinessCardMapper mapper;

    public BusinessCardServiceImpl(BusinessCardRepository repository, BusinessCardMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public BusinessCardResponse createCard(BusinessCardRequest request) {
        log.info("Creating business card for name={} company={}", request.getFullName(), request.getCompany());
        BusinessCard entity = mapper.toEntity(request);
        BusinessCard saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BusinessCardResponse getCardById(Long id) {
        log.debug("Fetching business card with id={}", id);
        BusinessCard entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Business card not found for id " + id));
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BusinessCardResponse> getAllCards(String name, String company) {
        log.debug("Listing business cards with filters name={} company={}", name, company);
        List<BusinessCard> cards;
        if (name != null && !name.isBlank()) {
            cards = repository.findByFullNameContainingIgnoreCase(name);
        } else if (company != null && !company.isBlank()) {
            cards = repository.findByCompanyContainingIgnoreCase(company);
        } else {
            cards = repository.findAll();
        }
        return cards.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public BusinessCardResponse updateCard(Long id, BusinessCardRequest request) {
        log.info("Updating business card id={}", id);
        BusinessCard existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Business card not found for id " + id));
        mapper.updateEntity(request, existing);
        BusinessCard saved = repository.save(existing);
        return mapper.toResponse(saved);
    }

    @Override
    public void deleteCard(Long id) {
        log.warn("Deleting business card id={}", id);
        BusinessCard existing = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Business card not found for id " + id));
        repository.delete(existing);
    }
}
