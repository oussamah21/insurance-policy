package com.tinubu.insurance.policy.api;


import com.tinubu.insurance.policy.api.dto.PolicyResponse;
import com.tinubu.insurance.policy.api.exception.PolicyNotFoundException;
import com.tinubu.insurance.policy.command.CreatePolicyCommand;
import com.tinubu.insurance.policy.command.UpdatePolicyCommand;
import com.tinubu.insurance.policy.query.queries.FindAllPoliciesQuery;
import com.tinubu.insurance.policy.query.queries.FindPolicyByIdQuery;
import com.tinubu.insurance.policy.infrastructure.persistance.PolicyProjectionEntity;
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


@RestController
@RequestMapping("/api/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;


    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody CreatePolicyCommand createPolicyCommand) {


        String aggregateId = UUID.randomUUID().toString();

     commandGateway.sendAndWait(new CreatePolicyCommand(
                aggregateId,
            createPolicyCommand.getName(),
            createPolicyCommand.getStatus(),
            createPolicyCommand.getStartDate(),
            createPolicyCommand.getEndDate()
        ));

     return ResponseEntity
             .status(HttpStatus.CREATED).build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Integer id,
            @Valid @RequestBody UpdatePolicyCommand updatePolicyCommand) {


         queryGateway.query(
                new FindPolicyByIdQuery(id),
                ResponseTypes.instanceOf(PolicyProjectionEntity.class)
        ).thenCompose(policy -> {

            updatePolicyCommand.setAggregateId(policy.getAggregateId());

            return commandGateway.send(updatePolicyCommand);
        }).join();

        return ResponseEntity
                .status(HttpStatus.OK).build();
    }

    @GetMapping
    public ResponseEntity<List<PolicyResponse>> fetchAll() {


       List<PolicyProjectionEntity> policies = queryGateway.query(
                new FindAllPoliciesQuery(),
                ResponseTypes.multipleInstancesOf(PolicyProjectionEntity.class)
        ).join();


        return ResponseEntity.status(HttpStatus.OK).body(policies.stream().map(PolicyResponse::from).toList());



    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> fetchById(
            @PathVariable Integer id) {


        Optional<PolicyProjectionEntity> policyOptional = queryGateway.query(new FindPolicyByIdQuery(id), ResponseTypes.optionalInstanceOf(PolicyProjectionEntity.class)).join();


        return policyOptional.map(e -> ResponseEntity.ok(PolicyResponse.from(e)))
                .orElseThrow(() -> new PolicyNotFoundException(id));
    }

}
