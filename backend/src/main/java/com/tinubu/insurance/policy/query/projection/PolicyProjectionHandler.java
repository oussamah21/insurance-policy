package com.tinubu.insurance.policy.query.projection;


import com.tinubu.insurance.policy.command.event.PolicyCreatedEvent;
import com.tinubu.insurance.policy.command.event.PolicyUpdatedEvent;
import com.tinubu.insurance.policy.infrastructure.persistance.PolicyProjectionEntity;
import com.tinubu.insurance.policy.infrastructure.persistance.PolicyProjectionRepository;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
public class PolicyProjectionHandler {

    private final PolicyProjectionRepository policyProjectionRepository;

    public PolicyProjectionHandler(PolicyProjectionRepository policyProjectionRepository) {
        this.policyProjectionRepository = policyProjectionRepository;
    }

    @EventHandler
    public void on(PolicyCreatedEvent event) {

        PolicyProjectionEntity entity = PolicyProjectionEntity.builder()
                .aggregateId(event.aggregateId())
                .name(event.name())
                .status(event.status())
                .startDate(event.startDate())
                .endDate(event.endDate())
                .build();

        policyProjectionRepository.save(entity);
    }

    @EventHandler
    public void on(PolicyUpdatedEvent event) {

        policyProjectionRepository.findByAggregateId(event.aggregateId()).ifPresent(
                entity -> {
                    entity.setAggregateId(event.aggregateId());
                    entity.setName(event.name());
                    entity.setStatus(event.status());
                    entity.setStartDate(event.startDate());
                    entity.setEndDate(event.endDate());
                    policyProjectionRepository.save(entity);
                }
        );
    }
}
