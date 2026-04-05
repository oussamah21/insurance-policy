package com.tinubu.insurance.policy.query.port;

import com.tinubu.insurance.policy.infrastructure.persistance.PolicyProjectionEntity;
import java.util.List;
import java.util.Optional;

public interface PolicyProjectionPort {
    PolicyProjectionEntity save(PolicyProjectionEntity entity);
    Optional<PolicyProjectionEntity> findById(Integer id);
    List<PolicyProjectionEntity> findAll();
    Optional<PolicyProjectionEntity> findByAggregateId(String aggregateId);
}