package com.tinubu.insurance.policy.api;


import com.tinubu.insurance.policy.api.dto.PolicyResponse;
import com.tinubu.insurance.policy.api.exception.PolicyNotCreatedException;
import com.tinubu.insurance.policy.api.exception.PolicyNotFoundException;
import com.tinubu.insurance.policy.api.exception.PolicyNotUpdatedException;
import com.tinubu.insurance.policy.command.CreatePolicyCommand;
import com.tinubu.insurance.policy.command.UpdatePolicyCommand;
import com.tinubu.insurance.policy.query.model.PolicyProjectionDto;
import com.tinubu.insurance.policy.query.queries.FindAllPoliciesQuery;
import com.tinubu.insurance.policy.query.queries.FindPolicyByIdQuery;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionException;


@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;


    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreatePolicyCommand createPolicyCommand) {


        String aggregateId = UUID.randomUUID().toString();

        try {
            commandGateway.sendAndWait(new CreatePolicyCommand(
                    aggregateId,
                    createPolicyCommand.name(),
                    createPolicyCommand.status(),
                    createPolicyCommand.startDate(),
                    createPolicyCommand.endDate()
            ));
        } catch (Exception e) {
            throw new PolicyNotCreatedException();
        }

     return ResponseEntity
             .status(HttpStatus.CREATED).build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdatePolicyCommand updatePolicyCommand) {

        try {
            queryGateway.query(
                    new FindPolicyByIdQuery(id),
                    ResponseTypes.optionalInstanceOf(PolicyProjectionDto.class)
            ).thenCompose(policy -> {

                if (policy.isEmpty()) {
                    throw new PolicyNotFoundException(id);
                }

                var command = new UpdatePolicyCommand(
                        policy.get().aggregateId(),
                        updatePolicyCommand.name(),
                        updatePolicyCommand.status(),
                        updatePolicyCommand.startDate(),
                        updatePolicyCommand.endDate()
                );

                return commandGateway.send(command);
            }).join();
        } catch (CompletionException e) {

            throw new PolicyNotUpdatedException();
        }

        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<PolicyResponse>> fetchAll() {


       List<PolicyProjectionDto> policies = queryGateway.query(
                new FindAllPoliciesQuery(),
                ResponseTypes.multipleInstancesOf(PolicyProjectionDto.class)
        ).join();


        return ResponseEntity.status(HttpStatus.OK).body(policies.stream().map(PolicyResponse::from).toList());



    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> fetchById(
            @PathVariable Integer id) {


        Optional<PolicyProjectionDto> policyOptional = queryGateway.query(new FindPolicyByIdQuery(id), ResponseTypes.optionalInstanceOf(PolicyProjectionDto.class)).join();


        return policyOptional.map(e -> ResponseEntity.ok(PolicyResponse.from(e)))
                .orElseThrow(() -> new PolicyNotFoundException(id));
    }

}
