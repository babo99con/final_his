package com.app.medical_support.diagnosticexecution.repository;

import com.app.medical_support.diagnosticexecution.entity.PhysiologicalEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhysiologicalRepository extends JpaRepository<PhysiologicalEntity, String> {
    boolean existsByTestExecutionId(String testExecutionId);
}
