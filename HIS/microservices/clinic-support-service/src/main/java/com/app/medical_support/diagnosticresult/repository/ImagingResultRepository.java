package com.app.medical_support.diagnosticresult.repository;

import com.app.medical_support.diagnosticresult.dto.ImagingResultDTO;
import com.app.medical_support.diagnosticresult.entity.ImagingResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ImagingResultRepository extends JpaRepository<ImagingResultEntity, String> {
    boolean existsByImagingExamId(String imagingExamId);
    Optional<ImagingResultEntity> findByImagingExamId(String imagingExamId);

    @Query("""
            select new com.app.medical_support.diagnosticresult.dto.ImagingResultDTO(
                r.imagingResultId,
                r.imagingExamId,
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
                r.readingDetail,
                r.confirmedAt,
                r.status,
                r.progressStatus,
                r.createdAt,
                r.updatedAt,
                r.completedAt
            )
            from ImagingResultEntity r
            join com.app.medical_support.diagnosticexecution.entity.ImagingEntity e
              on r.imagingExamId = e.imagingExamId
            """)
    List<ImagingResultDTO> findImagingResultResponseList();

    @Query("""
            select new com.app.medical_support.diagnosticresult.dto.ImagingResultDTO(
                r.imagingResultId,
                r.imagingExamId,
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
                r.readingDetail,
                r.confirmedAt,
                r.status,
                r.progressStatus,
                r.createdAt,
                r.updatedAt,
                r.completedAt
            )
            from ImagingResultEntity r
            join com.app.medical_support.diagnosticexecution.entity.ImagingEntity e
              on r.imagingExamId = e.imagingExamId
            where r.imagingResultId = :id
            """)
    Optional<ImagingResultDTO> findImagingResultResponseDetail(@Param("id") String id);
}
