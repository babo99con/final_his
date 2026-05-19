package com.app.medical_support.diagnosticexecution.repository;

import com.app.medical_support.diagnosticexecution.entity.PathologyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PathologyRepository extends JpaRepository<PathologyEntity, String> {
    boolean existsByTestExecutionId(String testExecutionId);
}
