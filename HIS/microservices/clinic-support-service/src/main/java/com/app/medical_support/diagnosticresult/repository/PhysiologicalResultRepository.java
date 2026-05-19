package com.app.medical_support.diagnosticresult.repository;

import com.app.medical_support.diagnosticresult.dto.PhysiologicalResultDTO;
import com.app.medical_support.diagnosticresult.entity.PhysiologicalResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhysiologicalResultRepository extends JpaRepository<PhysiologicalResultEntity, String> {
    boolean existsByPhysiologicalExamId(String physiologicalExamId);
    Optional<PhysiologicalResultEntity> findByPhysiologicalExamId(String physiologicalExamId);

    @Query("""
            select new com.app.medical_support.diagnosticresult.dto.PhysiologicalResultDTO(
                r.physiologicalExamResultId,
                r.physiologicalExamId,
                e.testExecutionId,
                e.detailCode,
                e.patientId,
                e.patientName,
                e.departmentName,
                e.examEquipmentId,
                e.rawData,
                e.reportDocId,
                e.performerId,
                e.performerName,
                r.resultManagerId,
                r.resultManagerName,
                r.resultSummary,
                r.report,
                r.measuredItemCode,
                r.confirmedAt,
                r.status,
                r.progressStatus,
                r.createdAt,
                r.updatedAt,
                r.completedAt
            )
            from PhysiologicalResultEntity r
            join com.app.medical_support.diagnosticexecution.entity.PhysiologicalEntity e
              on r.physiologicalExamId = e.physiologicalExamId
            """)
    List<PhysiologicalResultDTO> findPhysiologicalResultResponseList();

    @Query("""
            select new com.app.medical_support.diagnosticresult.dto.PhysiologicalResultDTO(
                r.physiologicalExamResultId,
                r.physiologicalExamId,
                e.testExecutionId,
                e.detailCode,
                e.patientId,
                e.patientName,
                e.departmentName,
                e.examEquipmentId,
                e.rawData,
                e.reportDocId,
                e.performerId,
                e.performerName,
                r.resultManagerId,
                r.resultManagerName,
                r.resultSummary,
                r.report,
                r.measuredItemCode,
                r.confirmedAt,
                r.status,
                r.progressStatus,
                r.createdAt,
                r.updatedAt,
                r.completedAt
            )
            from PhysiologicalResultEntity r
            join com.app.medical_support.diagnosticexecution.entity.PhysiologicalEntity e
              on r.physiologicalExamId = e.physiologicalExamId
            where r.physiologicalExamResultId = :id
            """)
    Optional<PhysiologicalResultDTO> findPhysiologicalResultResponseDetail(@Param("id") String id);
}
