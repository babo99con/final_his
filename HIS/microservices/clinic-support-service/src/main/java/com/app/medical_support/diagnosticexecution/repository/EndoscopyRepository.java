package com.app.medical_support.diagnosticexecution.repository;

import com.app.medical_support.diagnosticexecution.entity.EndoscopyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EndoscopyRepository extends JpaRepository<EndoscopyEntity, String> {
    boolean existsByTestExecutionId(String testExecutionId);
}
