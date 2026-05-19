package com.app.medical_support.diagnosticresult.repository;

import com.app.medical_support.diagnosticresult.dto.EndoscopyResultDTO;
import com.app.medical_support.diagnosticresult.entity.EndoscopyResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EndoscopyResultRepository extends JpaRepository<EndoscopyResultEntity, String> {
    boolean existsByEndoscopyExamId(String endoscopyExamId);
    Optional<EndoscopyResultEntity> findByEndoscopyExamId(String endoscopyExamId);

    @Query("""
            select new com.app.medical_support.diagnosticresult.dto.EndoscopyResultDTO(
                r.endoscopyResultId,
                r.endoscopyExamId,
                e.testExecutionId,
                e.detailCode,
                e.patientId,
                e.patientName,
                e.departmentName,
                e.performerId,
                e.performerName,
                r.resultManagerId,
                r.resultManagerName,
                r.resultSummary,
                r.biopsyYn,
                r.confirmedAt,
                r.readerId,
                r.status,
                r.progressStatus,
                r.createdAt,
                r.updatedAt,
                r.completedAt
            )
            from EndoscopyResultEntity r
            join com.app.medical_support.diagnosticexecution.entity.EndoscopyEntity e
              on r.endoscopyExamId = e.endoscopyExamId
            """)
    List<EndoscopyResultDTO> findEndoscopyResultResponseList();

    @Query("""
            select new com.app.medical_support.diagnosticresult.dto.EndoscopyResultDTO(
                r.endoscopyResultId,
                r.endoscopyExamId,
                e.testExecutionId,
                e.detailCode,
                e.patientId,
                e.patientName,
                e.departmentName,
                e.performerId,
                e.performerName,
                r.resultManagerId,
                r.resultManagerName,
                r.resultSummary,
                r.biopsyYn,
                r.confirmedAt,
                r.readerId,
                r.status,
                r.progressStatus,
                r.createdAt,
                r.updatedAt,
                r.completedAt
            )
            from EndoscopyResultEntity r
            join com.app.medical_support.diagnosticexecution.entity.EndoscopyEntity e
              on r.endoscopyExamId = e.endoscopyExamId
            where r.endoscopyResultId = :id
            """)
    Optional<EndoscopyResultDTO> findEndoscopyResultResponseDetail(@Param("id") String id);
}
