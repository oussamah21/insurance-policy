package com.tinubu.insurance.policy.query.handler;

import com.tinubu.insurance.policy.infrastructure.persistance.PolicyProjectionEntity;
import com.tinubu.insurance.policy.infrastructure.persistance.PolicyProjectionRepository;
import com.tinubu.insurance.policy.query.queries.FindAllPoliciesQuery;
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

    private final PolicyProjectionRepository policyProjectionRepository;


    @QueryHandler
    public List<PolicyProjectionEntity> handle(FindAllPoliciesQuery query) {
        return policyProjectionRepository.findAll();
    }

    @QueryHandler
    public Optional<PolicyProjectionEntity> handle(FindPolicyByIdQuery query) {
        return policyProjectionRepository.findById(query.id());
    }
}
