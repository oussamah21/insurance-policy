package com.tinubu.insurance.policy.infrastructure.persistance;

import com.tinubu.insurance.policy.query.port.PolicyProjectionPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PolicyProjectionAdapter implements PolicyProjectionPort {

    private final PolicyProjectionRepository repository;

    @Override
    public PolicyProjectionEntity save(PolicyProjectionEntity entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<PolicyProjectionEntity> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public List<PolicyProjectionEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PolicyProjectionEntity> findByAggregateId(String aggregateId) {
        return repository.findByAggregateId(aggregateId);
    }
}