package com.tinubu.insurance.policy.api;

import com.tinubu.insurance.policy.command.CreatePolicyCommand;
import com.tinubu.insurance.policy.command.enums.PolicyStatus;
import com.tinubu.insurance.policy.infrastructure.persistance.PolicyProjectionEntity;
import com.tinubu.insurance.policy.query.model.PolicyProjectionDto;
import com.tinubu.insurance.policy.query.queries.FindPolicyByIdQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseType;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PolicyControllerTest {

    private CommandGateway commandGateway;
    private QueryGateway queryGateway;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        commandGateway = mock(CommandGateway.class);
        queryGateway = mock(QueryGateway.class);

        PolicyController controller =
                new PolicyController(commandGateway, queryGateway);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }
    @Test
    void shouldCreatePolicy() throws Exception {

        when(commandGateway.sendAndWait(any()))
                .thenReturn(null);

        CreatePolicyCommand cmd = new CreatePolicyCommand(
                null,
                "Policy",
                PolicyStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        mockMvc.perform(post("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isCreated());

        verify(commandGateway, times(1)).sendAndWait(any());
    }

    @Test
    void shouldFetchPolicyById() throws Exception {

        PolicyProjectionDto policyProjectionDto = new PolicyProjectionDto(
                1,
                "aggregate-1",
                "Test Policy",
                PolicyStatus.ACTIVE,
                LocalDate.now(),
                LocalDate.now().plusYears(1),
                LocalDate.now(),
                LocalDate.now()
        );


        ResponseType<Optional<PolicyProjectionDto>> responseType =
                ResponseTypes.optionalInstanceOf(PolicyProjectionDto.class);

        when(queryGateway.query(any(), eq(responseType)))
                .thenReturn(CompletableFuture.completedFuture(Optional.of(policyProjectionDto)));


        mockMvc.perform(get("/api/policies/1"))
                .andExpect(status().isOk());

        verify(queryGateway, times(1)).query(
                any(FindPolicyByIdQuery.class),
                eq(ResponseTypes.optionalInstanceOf(PolicyProjectionDto.class))
        );
    }


}