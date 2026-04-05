package com.tinubu.insurance.policy.query.handler;

import com.tinubu.insurance.policy.infrastructure.persistance.PolicyProjectionEntity;
import com.tinubu.insurance.policy.infrastructure.persistance.PolicyProjectionRepository;
import com.tinubu.insurance.policy.query.port.PolicyProjectionPort;
import com.tinubu.insurance.policy.query.queries.FindAllPoliciesQuery;
import com.tinubu.insurance.policy.query.queries.FindPolicyByAggregateIdQuery;
import com.tinubu.insurance.policy.query.queries.FindPolicyByIdQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
@Slf4j
@RequiredArgsConstructor
public class PolicyQueryHandler {

    private final PolicyProjectionPort policyProjectionPort;


    @QueryHandler
    public List<PolicyProjectionEntity> handle(FindAllPoliciesQuery query) {
        return policyProjectionPort.findAll();
    }

    @QueryHandler
    public Optional<PolicyProjectionEntity> handle(FindPolicyByIdQuery query) {
        return policyProjectionPort.findById(query.id());
    }

    @QueryHandler
    public Optional<PolicyProjectionEntity> handle(FindPolicyByAggregateIdQuery query) {
        return policyProjectionPort.findByAggregateId(query.aggregateId());
    }
}
