package com.tinubu.insurance.policy.command.aggregate;


import com.tinubu.insurance.policy.command.CreatePolicyCommand;
import com.tinubu.insurance.policy.command.UpdatePolicyCommand;
import com.tinubu.insurance.policy.command.event.PolicyCreatedEvent;
import com.tinubu.insurance.policy.command.event.PolicyUpdatedEvent;
import com.tinubu.insurance.policy.command.model.PolicyStatus;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.time.LocalDate;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class PoliciesAggregate {

    @AggregateIdentifier
    private String aggregateId;
    private String name;
    private PolicyStatus status;
    private LocalDate startDate;
    private LocalDate endDate;

    public PoliciesAggregate() {
    }

    @CommandHandler
    public PoliciesAggregate(CreatePolicyCommand createPolicyCommand) {

        validateCoverageDates(createPolicyCommand.getStartDate(), createPolicyCommand.getEndDate());

        apply(new PolicyCreatedEvent(
                createPolicyCommand.getAggregateId(),
                createPolicyCommand.getName(),
                createPolicyCommand.getStatus(),
                createPolicyCommand.getStartDate(),
                createPolicyCommand.getEndDate()
        ));
    }

    @CommandHandler
    public void handle(UpdatePolicyCommand updatePolicyCommand) {

        validateCoverageDates(updatePolicyCommand.getStartDate(), updatePolicyCommand.getEndDate());

        apply(new PolicyUpdatedEvent(
                updatePolicyCommand.getAggregateId(),
                updatePolicyCommand.getName(),
                updatePolicyCommand.getStatus(),
                updatePolicyCommand.getStartDate(),
                updatePolicyCommand.getEndDate()
        ));
    }


    @EventSourcingHandler
    public void on(PolicyCreatedEvent policyCreatedEvent) {
        this.aggregateId = policyCreatedEvent.getAggregateId();
        this.name        = policyCreatedEvent.getName();
        this.status      = policyCreatedEvent.getStatus();
        this.startDate   = policyCreatedEvent.getStartDate();
        this.endDate     = policyCreatedEvent.getEndDate();
    }

    @EventSourcingHandler
    public void on(PolicyUpdatedEvent policyUpdatedEvent) {
        this.name      = policyUpdatedEvent.getName();
        this.status    = policyUpdatedEvent.getStatus();
        this.startDate = policyUpdatedEvent.getStartDate();
        this.endDate   = policyUpdatedEvent.getEndDate();
    }


    private void validateCoverageDates(LocalDate start, LocalDate end) {
        if (start != null && end != null && end.isBefore(start)) {
            throw new IllegalArgumentException(
                    "La date de fin de couverture (%s) ne peut pas être antérieure à la date de début (%s)."
                            .formatted(end, start)
            );
        }
    }
}
