package com.app.medical_support.diagnosticexecution.repository;

import com.app.medical_support.diagnosticexecution.entity.ImagingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagingRepository extends JpaRepository<ImagingEntity, String> {
    boolean existsByTestExecutionId(String testExecutionId);
}
