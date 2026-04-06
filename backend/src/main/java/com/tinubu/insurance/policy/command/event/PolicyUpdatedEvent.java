package com.tinubu.insurance.policy.command.event;

import com.tinubu.insurance.policy.command.enums.PolicyStatus;

import java.time.LocalDate;


public record PolicyUpdatedEvent(
        String aggregateId,
        String name,
        PolicyStatus status,
        LocalDate startDate,
        LocalDate endDate
) {}
