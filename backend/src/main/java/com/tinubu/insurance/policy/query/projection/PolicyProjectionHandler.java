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
                .aggregateId(event.getAggregateId())
                .name(event.getName())
                .status(event.getStatus())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .build();

        policyProjectionRepository.save(entity);
    }

    @EventHandler
    public void on(PolicyUpdatedEvent event) {

        policyProjectionRepository.findByAggregateId(event.getAggregateId()).ifPresent(
                entity -> {
                    entity.setAggregateId(event.getAggregateId());
                    entity.setName(event.getName());
                    entity.setStatus(event.getStatus());
                    entity.setStartDate(event.getStartDate());
                    entity.setEndDate(event.getEndDate());
                    policyProjectionRepository.save(entity);
                }
        );
    }
}
