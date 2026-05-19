package com.app.medical_support.diagnosticexecution.repository;

import com.app.medical_support.diagnosticexecution.entity.TestExecutionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestExecutionRepository extends JpaRepository<TestExecutionEntity, String> {
    List<TestExecutionEntity> findByExecutionType(String executionType);
    List<TestExecutionEntity> findByExecutionTypeAndProgressStatus(String executionType, String progressStatus);

}
