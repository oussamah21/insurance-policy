package com.tinubu.insurance.policy.query.projection;

import com.tinubu.insurance.policy.command.event.PolicyCreatedEvent;
import com.tinubu.insurance.policy.command.event.PolicyUpdatedEvent;
import com.tinubu.insurance.policy.command.enums.PolicyStatus;
import com.tinubu.insurance.policy.infrastructure.persistance.PolicyProjectionRepository;
import com.tinubu.insurance.policy.query.model.PolicyProjectionDto;
import com.tinubu.insurance.policy.query.port.PolicyProjectionPort;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Optional;


class PolicyProjectionHandlerTest {

    @Test
    void shouldHandlePolicyCreatedEvent() {

        PolicyProjectionPort port = Mockito.mock(PolicyProjectionPort.class);
        PolicyProjectionHandler projection = new PolicyProjectionHandler(port);

        PolicyCreatedEvent event = new PolicyCreatedEvent(
                "agg-1",
                "Policy",
                PolicyStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        projection.on(event);

        Mockito.verify(port).save(Mockito.any());
    }

    @Test
    void shouldHandlePolicyUpdatedEvent() {

        PolicyProjectionPort port = Mockito.mock(PolicyProjectionPort.class);
        PolicyProjectionHandler projection = new PolicyProjectionHandler(port);

        PolicyProjectionDto existing = new PolicyProjectionDto(
                1,
                "agg-1",
                "Policy",
                PolicyStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                LocalDate.now(),
                LocalDate.now()
        );

        Mockito.when(port.findByAggregateId("agg-1")).thenReturn(Optional.of(existing));

        PolicyUpdatedEvent updatedEvent = new PolicyUpdatedEvent(
                "agg-1",
                "Policy",
                PolicyStatus.INACTIVE,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        projection.on(updatedEvent);

        Mockito.verify(port, Mockito.times(1)).findByAggregateId("agg-1");
        Mockito.verify(port, Mockito.times(1)).save(Mockito.any());
    }
}