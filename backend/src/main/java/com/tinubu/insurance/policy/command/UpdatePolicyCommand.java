package com.tinubu.insurance.policy.command;

import com.tinubu.insurance.policy.command.model.PolicyStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdatePolicyCommand {

    @TargetAggregateIdentifier
    private String aggregateId;
    @NotBlank
    @NotNull
    private String name;
    @NotNull
    private PolicyStatus status;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
}
