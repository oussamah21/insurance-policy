package com.tinubu.insurance.policy.infrastructure.persistance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PolicyProjectionRepository extends JpaRepository<PolicyProjectionEntity, Integer> {


    Optional<PolicyProjectionEntity> findByAggregateId(String aggregateId);
}
