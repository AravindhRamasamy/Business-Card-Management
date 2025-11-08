package com.example.businesscard.mapper;

import com.example.businesscard.domain.BusinessCard;
import com.example.businesscard.dto.BusinessCardRequest;
import com.example.businesscard.dto.BusinessCardResponse;
import org.springframework.stereotype.Component;

@Component
public class BusinessCardMapper {

    public BusinessCard toEntity(BusinessCardRequest request) {
        if (request == null) {
            return null;
        }
        return new BusinessCard(null, request.getFullName(), request.getCompany(), request.getJobTitle(), request.getEmail(),
            request.getPhone(), request.getAddress());
    }

    public void updateEntity(BusinessCardRequest request, BusinessCard entity) {
        if (request == null || entity == null) {
            return;
        }
        entity.setFullName(request.getFullName());
        entity.setCompany(request.getCompany());
        entity.setJobTitle(request.getJobTitle());
        entity.setEmail(request.getEmail());
        entity.setPhone(request.getPhone());
        entity.setAddress(request.getAddress());
    }

    public BusinessCardResponse toResponse(BusinessCard entity) {
        if (entity == null) {
            return null;
        }
        return new BusinessCardResponse(entity.getId(), entity.getFullName(), entity.getCompany(), entity.getJobTitle(),
            entity.getEmail(), entity.getPhone(), entity.getAddress());
    }
}
