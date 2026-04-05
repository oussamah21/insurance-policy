package com.tinubu.insurance.policy.query.projection;

import com.tinubu.insurance.policy.command.event.PolicyCreatedEvent;
import com.tinubu.insurance.policy.command.event.PolicyUpdatedEvent;
import com.tinubu.insurance.policy.command.model.PolicyStatus;
import com.tinubu.insurance.policy.infrastructure.persistance.PolicyProjectionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;


class PolicyProjectionHandlerTest {

    @Test
    void shouldHandlePolicyCreatedEvent() {

        PolicyProjectionRepository repository = Mockito.mock(PolicyProjectionRepository.class);
        PolicyProjectionHandler projection = new PolicyProjectionHandler(repository);

        PolicyCreatedEvent event = new PolicyCreatedEvent(
                "agg-1",
                "Policy",
                PolicyStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        projection.on(event);

        Mockito.verify(repository).save(Mockito.any());
    }

    @Test
    void shouldHandlePolicyUpdatedEvent() {

        PolicyProjectionRepository repository = Mockito.mock(PolicyProjectionRepository.class);
        PolicyProjectionHandler projection = new PolicyProjectionHandler(repository);

        PolicyCreatedEvent createdEvent = new PolicyCreatedEvent(
                "agg-1",
                "Policy",
                PolicyStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        projection.on(createdEvent);

        Mockito.verify(repository,Mockito.times(1)).save(Mockito.any());
        Mockito.verify(repository,Mockito.times(0)).findByAggregateId(Mockito.any());

        PolicyUpdatedEvent updatedEvent = new PolicyUpdatedEvent(
                "agg-1",
                "Policy",
                PolicyStatus.INACTIVE,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        projection.on(updatedEvent);

        Mockito.verify(repository,Mockito.times(1)).findByAggregateId(Mockito.any());
        Mockito.verify(repository,Mockito.times(1)).save(Mockito.any());
    }
}