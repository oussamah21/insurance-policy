package com.tinubu.insurance.policy.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tinubu.insurance.policy.command.model.PolicyStatus;
import com.tinubu.insurance.policy.infrastructure.persistance.PolicyProjectionEntity;
import lombok.Builder;

import java.time.LocalDate;


@Builder
public record PolicyResponse(

        Integer id,
        String name,
        PolicyStatus status,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate startDate,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate endDate,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate updatedAt
) {
    public static PolicyResponse from(PolicyProjectionEntity e) {
        return PolicyResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .status(e.getStatus())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();
    }
}
