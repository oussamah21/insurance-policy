package com.tinubu.insurance.policy.command.aggregate;

import com.tinubu.insurance.policy.command.CreatePolicyCommand;
import com.tinubu.insurance.policy.command.UpdatePolicyCommand;
import com.tinubu.insurance.policy.command.event.PolicyCreatedEvent;
import com.tinubu.insurance.policy.command.event.PolicyUpdatedEvent;
import com.tinubu.insurance.policy.command.enums.PolicyStatus;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;


class PoliciesAggregateTest {
    private AggregateTestFixture<PoliciesAggregate> fixture;

    @BeforeEach
    void setUp() {
        fixture = new AggregateTestFixture<>(PoliciesAggregate.class);
    }

    @Test
    void shouldCreatePolicy() {
        String id = UUID.randomUUID().toString();

        fixture.givenNoPriorActivity()
                .when(new CreatePolicyCommand(
                        id,
                        "Test Policy",
                        PolicyStatus.ACTIVE,
                        LocalDate.now(),
                        LocalDate.now().plusDays(10)
                ))
                .expectSuccessfulHandlerExecution()
                .expectEvents(new PolicyCreatedEvent(
                        id,
                        "Test Policy",
                        PolicyStatus.ACTIVE,
                        LocalDate.now(),
                        LocalDate.now().plusDays(10)
                ));
    }

    @Test
    void shouldUpdatePolicy() {
        String id = UUID.randomUUID().toString();

        fixture.given(new PolicyCreatedEvent(
                        id,
                        "Old",
                        PolicyStatus.ACTIVE,
                        LocalDate.now(),
                        LocalDate.now().plusDays(5)
                ))
                .when(new UpdatePolicyCommand(
                        id,
                        "Updated",
                        PolicyStatus.INACTIVE,
                        LocalDate.now(),
                        LocalDate.now().plusDays(10)
                ))
                .expectEvents(new PolicyUpdatedEvent(
                        id,
                        "Updated",
                        PolicyStatus.INACTIVE,
                        LocalDate.now(),
                        LocalDate.now().plusDays(10)
                ));
    }
}