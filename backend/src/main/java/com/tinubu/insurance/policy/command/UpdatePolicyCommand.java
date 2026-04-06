package com.tinubu.insurance.policy.command;

import com.tinubu.insurance.policy.command.enums.PolicyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;

public record UpdatePolicyCommand(
        @TargetAggregateIdentifier String aggregateId,
        @NotBlank @NotNull String name,
        @NotNull PolicyStatus status,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {}
