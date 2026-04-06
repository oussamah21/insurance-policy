package com.tinubu.insurance.policy.query.projection;


import com.tinubu.insurance.policy.command.event.PolicyCreatedEvent;
import com.tinubu.insurance.policy.command.event.PolicyUpdatedEvent;
import com.tinubu.insurance.policy.query.model.PolicyProjectionDto;
import com.tinubu.insurance.policy.query.port.PolicyProjectionPort;
import lombok.RequiredArgsConstructor;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PolicyProjectionHandler {

    private final PolicyProjectionPort policyProjectionPort;

    @EventHandler
    public void on(PolicyCreatedEvent event) {
        policyProjectionPort.save(new PolicyProjectionDto(
                null,
                event.aggregateId(),
                event.name(),
                event.status(),
                event.startDate(),
                event.endDate(),
                null,
                null
        ));
    }

    @EventHandler
    public void on(PolicyUpdatedEvent event) {
        policyProjectionPort.findByAggregateId(event.aggregateId()).ifPresent(existing ->
                policyProjectionPort.save(new PolicyProjectionDto(
                        existing.id(),
                        event.aggregateId(),
                        event.name(),
                        event.status(),
                        event.startDate(),
                        event.endDate(),
                        existing.createdAt(),
                        null
                ))
        );
    }
}