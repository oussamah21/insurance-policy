package com.tinubu.insurance.policy.command.event;

import com.tinubu.insurance.policy.command.model.PolicyStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;


public record PolicyUpdatedEvent(
        String aggregateId,
        String name,
        PolicyStatus status,
        LocalDate startDate,
        LocalDate endDate
) {}
