package com.example.businesscard.service;

import com.example.businesscard.dto.BusinessCardRequest;
import com.example.businesscard.dto.BusinessCardResponse;
import java.util.List;

public interface BusinessCardService {

    BusinessCardResponse createCard(BusinessCardRequest request);

    BusinessCardResponse getCardById(Long id);

    List<BusinessCardResponse> getAllCards(String name, String company);

    BusinessCardResponse updateCard(Long id, BusinessCardRequest request);

    void deleteCard(Long id);
}
