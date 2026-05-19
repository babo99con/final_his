package com.app.medical_support.diagnosticresult.repository;

import com.app.medical_support.diagnosticresult.dto.PathologyResultDTO;
import com.app.medical_support.diagnosticresult.entity.PathologyResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PathologyResultRepository extends JpaRepository<PathologyResultEntity, String> {
    boolean existsByPathologyExamId(String pathologyExamId);
    Optional<PathologyResultEntity> findByPathologyExamId(String pathologyExamId);

    @Query("""
            select new com.app.medical_support.diagnosticresult.dto.PathologyResultDTO(
                r.pathologyExamResultId,
                r.pathologyExamId,
                e.testExecutionId,
                e.detailCode,
                e.patientId,
                e.patientName,
                e.departmentName,
                e.tissueStatus,
                e.collectionMethod,
                e.tissueSite,
                e.tissueType,
                e.collectedAt,
                e.reexamYn,
                e.performerId,
                e.performerName,
                r.resultManagerId,
                r.resultManagerName,
                r.resultSummary,
                r.judgedAt,
                r.confirmedAt,
                r.readerId,
                r.diagnosisName,
                r.status,
                r.progressStatus,
                r.createdAt,
                r.updatedAt,
                r.completedAt
            )
            from PathologyResultEntity r
            join com.app.medical_support.diagnosticexecution.entity.PathologyEntity e
              on r.pathologyExamId = e.pathologyExamId
            """)
    List<PathologyResultDTO> findPathologyResultResponseList();

    @Query("""
            select new com.app.medical_support.diagnosticresult.dto.PathologyResultDTO(
                r.pathologyExamResultId,
                r.pathologyExamId,
                e.testExecutionId,
                e.detailCode,
                e.patientId,
                e.patientName,
                e.departmentName,
                e.tissueStatus,
                e.collectionMethod,
                e.tissueSite,
                e.tissueType,
                e.collectedAt,
                e.reexamYn,
                e.performerId,
                e.performerName,
                r.resultManagerId,
                r.resultManagerName,
                r.resultSummary,
                r.judgedAt,
                r.confirmedAt,
                r.readerId,
                r.diagnosisName,
                r.status,
                r.progressStatus,
                r.createdAt,
                r.updatedAt,
                r.completedAt
            )
            from PathologyResultEntity r
            join com.app.medical_support.diagnosticexecution.entity.PathologyEntity e
              on r.pathologyExamId = e.pathologyExamId
            where r.pathologyExamResultId = :id
            """)
    Optional<PathologyResultDTO> findPathologyResultResponseDetail(@Param("id") String id);
}
