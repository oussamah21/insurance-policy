package com.tinubu.insurance.policy.command.aggregate;


import com.tinubu.insurance.policy.command.CreatePolicyCommand;
import com.tinubu.insurance.policy.command.UpdatePolicyCommand;
import com.tinubu.insurance.policy.command.event.PolicyCreatedEvent;
import com.tinubu.insurance.policy.command.event.PolicyUpdatedEvent;
import com.tinubu.insurance.policy.command.enums.PolicyStatus;
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

        validateCoverageDates(createPolicyCommand.startDate(), createPolicyCommand.endDate());

        apply(new PolicyCreatedEvent(
                createPolicyCommand.aggregateId(),
                createPolicyCommand.name(),
                createPolicyCommand.status(),
                createPolicyCommand.startDate(),
                createPolicyCommand.endDate()
        ));
    }

    @CommandHandler
    public void handle(UpdatePolicyCommand updatePolicyCommand) {

        validateCoverageDates(updatePolicyCommand.startDate(), updatePolicyCommand.endDate());

        apply(new PolicyUpdatedEvent(
                updatePolicyCommand.aggregateId(),
                updatePolicyCommand.name(),
                updatePolicyCommand.status(),
                updatePolicyCommand.startDate(),
                updatePolicyCommand.endDate()
        ));
    }


    @EventSourcingHandler
    public void on(PolicyCreatedEvent policyCreatedEvent) {
        this.aggregateId = policyCreatedEvent.aggregateId();
        this.name        = policyCreatedEvent.name();
        this.status      = policyCreatedEvent.status();
        this.startDate   = policyCreatedEvent.startDate();
        this.endDate     = policyCreatedEvent.endDate();
    }

    @EventSourcingHandler
    public void on(PolicyUpdatedEvent policyUpdatedEvent) {
        this.aggregateId = policyUpdatedEvent.aggregateId();
        this.name      = policyUpdatedEvent.name();
        this.status    = policyUpdatedEvent.status();
        this.startDate = policyUpdatedEvent.startDate();
        this.endDate   = policyUpdatedEvent.endDate();
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
