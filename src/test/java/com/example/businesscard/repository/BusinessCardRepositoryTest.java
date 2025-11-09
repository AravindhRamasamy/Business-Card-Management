package com.example.businesscard.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.businesscard.domain.BusinessCard;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BusinessCardRepositoryTest {

    @Autowired
    private BusinessCardRepository repository;

    @Test
    @DisplayName("Should save and retrieve business card by id")
    void shouldSaveAndFindById() {
        BusinessCard card = new BusinessCard(null, "John Doe", "Acme Corp", "Engineer", "john.doe@example.com", "+1234567890", "123 Main St");
        BusinessCard saved = repository.save(card);

        assertThat(saved.getId()).isNotNull();
        assertThat(repository.findById(saved.getId())).isPresent();
    }

    @Test
    @DisplayName("Should find cards by full name containing ignoring case")
    void shouldFindByFullNameContainingIgnoreCase() {
        repository.save(new BusinessCard(null, "Alice Smith", "Acme", "Manager", "alice@example.com", "+1234567890", "123 Road"));
        repository.save(new BusinessCard(null, "Bob Johnson", "Beta", "Analyst", "bob@example.com", "+1234567891", "456 Road"));

        List<BusinessCard> results = repository.findByFullNameContainingIgnoreCase("alice");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFullName()).isEqualTo("Alice Smith");
    }

    @Test
    @DisplayName("Should find cards by company containing ignoring case")
    void shouldFindByCompanyContainingIgnoreCase() {
        repository.save(new BusinessCard(null, "Carol White", "Gamma Corp", "Designer", "carol@example.com", "+1234567892", "789 Road"));
        repository.save(new BusinessCard(null, "Dan Brown", "Delta Inc", "Consultant", "dan@example.com", "+1234567893", "101 Road"));

        List<BusinessCard> results = repository.findByCompanyContainingIgnoreCase("gamma");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getCompany()).isEqualTo("Gamma Corp");
    }

    @Test
    @DisplayName("Should enforce full name not null constraint")
    void shouldEnforceFullNameConstraint() {
        BusinessCard invalid = new BusinessCard(null, null, "Acme", "Manager", "alice@example.com", "+1234567890", "123 Road");

        assertThatThrownBy(() -> repository.saveAndFlush(invalid))
            .isInstanceOf(ConstraintViolationException.class);
    }
}
