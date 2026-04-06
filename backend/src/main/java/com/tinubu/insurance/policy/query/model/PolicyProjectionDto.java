package com.tinubu.insurance.policy.query.model;


import com.tinubu.insurance.policy.command.enums.PolicyStatus;

import java.time.LocalDate;

public record PolicyProjectionDto(
        Integer id,
        String aggregateId,
        String name,
        PolicyStatus status,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate createdAt,
        LocalDate updatedAt
) {}
