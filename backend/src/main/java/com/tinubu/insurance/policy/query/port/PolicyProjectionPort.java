package com.tinubu.insurance.policy.query.port;

import com.tinubu.insurance.policy.query.model.PolicyProjectionDto;

import java.util.List;
import java.util.Optional;

public interface PolicyProjectionPort {
    PolicyProjectionDto save(PolicyProjectionDto policyProjectionDto);
    Optional<PolicyProjectionDto> findById(Integer id);
    List<PolicyProjectionDto> findAll();
    Optional<PolicyProjectionDto> findByAggregateId(String aggregateId);
}