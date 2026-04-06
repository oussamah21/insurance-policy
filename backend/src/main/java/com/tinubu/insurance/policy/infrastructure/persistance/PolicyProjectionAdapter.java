package com.tinubu.insurance.policy.infrastructure.persistance;

import com.tinubu.insurance.policy.query.model.PolicyProjectionDto;
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
    public PolicyProjectionDto save(PolicyProjectionDto policyProjectionDto) {
        PolicyProjectionEntity entity = policyProjectionDto.id() == null
                ? toNewEntity(policyProjectionDto)
                : toExistingEntity(policyProjectionDto);
        return from(repository.save(entity));
    }

    @Override
    public Optional<PolicyProjectionDto> findById(Integer id) {
        return repository.findById(id).map(this::from);
    }

    @Override
    public List<PolicyProjectionDto> findAll() {
        return repository.findAll().stream().map(this::from).toList();
    }

    @Override
    public Optional<PolicyProjectionDto> findByAggregateId(String aggregateId) {
        return repository.findByAggregateId(aggregateId).map(this::from);
    }

    private PolicyProjectionDto from(PolicyProjectionEntity entity) {
        return new PolicyProjectionDto(
                entity.getId(),
                entity.getAggregateId(),
                entity.getName(),
                entity.getStatus(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private PolicyProjectionEntity toNewEntity(PolicyProjectionDto dto) {
        return PolicyProjectionEntity.builder()
                .aggregateId(dto.aggregateId())
                .name(dto.name())
                .status(dto.status())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .build();
    }

    private PolicyProjectionEntity toExistingEntity(PolicyProjectionDto dto) {
        return PolicyProjectionEntity.builder()
                .id(dto.id())
                .aggregateId(dto.aggregateId())
                .name(dto.name())
                .status(dto.status())
                .startDate(dto.startDate())
                .endDate(dto.endDate())
                .createdAt(dto.createdAt())
                .build();
    }
}