package com.tinubu.insurance.policy.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tinubu.insurance.policy.command.enums.PolicyStatus;
import com.tinubu.insurance.policy.infrastructure.persistance.PolicyProjectionEntity;
import com.tinubu.insurance.policy.query.model.PolicyProjectionDto;
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
    public static PolicyResponse from(PolicyProjectionDto e) {
        return PolicyResponse.builder()
                .id(e.id())
                .name(e.name())
                .status(e.status())
                .startDate(e.startDate())
                .endDate(e.endDate())
                .createdAt(e.createdAt())
                .updatedAt(e.updatedAt())
                .build();
    }
}
