package com.example.businesscard.controller;

import com.example.businesscard.dto.BusinessCardRequest;
import com.example.businesscard.dto.BusinessCardResponse;
import com.example.businesscard.service.BusinessCardService;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
public class BusinessCardController {

    private static final Logger log = LoggerFactory.getLogger(BusinessCardController.class);

    private final BusinessCardService service;

    public BusinessCardController(BusinessCardService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<BusinessCardResponse> createCard(@Valid @RequestBody BusinessCardRequest request) {
        log.info("Received request to create business card for name={}", request.getFullName());
        BusinessCardResponse response = service.createCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessCardResponse> getCard(@PathVariable Long id) {
        log.debug("Received request to get business card id={}", id);
        BusinessCardResponse response = service.getCardById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<BusinessCardResponse>> getCards(@RequestParam(required = false) String name,
                                                               @RequestParam(required = false) String company) {
        log.debug("Received request to list business cards with name={} company={}", name, company);
        List<BusinessCardResponse> responses = service.getAllCards(name, company);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusinessCardResponse> updateCard(@PathVariable Long id,
                                                           @Valid @RequestBody BusinessCardRequest request) {
        log.info("Received request to update business card id={}", id);
        BusinessCardResponse response = service.updateCard(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        log.warn("Received request to delete business card id={}", id);
        service.deleteCard(id);
        return ResponseEntity.noContent().build();
    }
}
