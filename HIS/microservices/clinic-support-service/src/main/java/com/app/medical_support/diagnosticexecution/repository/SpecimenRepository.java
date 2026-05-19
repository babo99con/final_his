package com.app.medical_support.diagnosticexecution.repository;

import com.app.medical_support.diagnosticexecution.entity.SpecimenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecimenRepository extends JpaRepository<SpecimenEntity, String> {

    boolean existsByTestExecutionId(String testExecutionId);
    List<SpecimenEntity> findByTestExecutionId(String testExecutionId);
    List<SpecimenEntity> findBySpecimenType(String specimenType);
    List<SpecimenEntity> findBySpecimenStatus(String specimenStatus);
}
