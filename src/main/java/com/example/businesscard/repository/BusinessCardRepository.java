package com.example.businesscard.repository;

import com.example.businesscard.domain.BusinessCard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessCardRepository extends JpaRepository<BusinessCard, Long> {

    List<BusinessCard> findByFullNameContainingIgnoreCase(String fullName);

    List<BusinessCard> findByCompanyContainingIgnoreCase(String company);
}
