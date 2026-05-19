package com.app.medical_support.diagnosticresult.repository;

import com.app.medical_support.diagnosticresult.dto.SpecimenTestResultDTO;
import com.app.medical_support.diagnosticresult.entity.SpecimenTestResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpecimenTestResultRepository extends JpaRepository<SpecimenTestResultEntity, String> {
    boolean existsBySpecimenExamId(String specimenExamId);
    Optional<SpecimenTestResultEntity> findBySpecimenExamId(String specimenExamId);

    @Query("""
            select new com.app.medical_support.diagnosticresult.dto.SpecimenTestResultDTO(
                r.specimenExamResultId,
                r.specimenExamId,
                e.testExecutionId,
                e.detailCode,
                e.patientId,
                e.patientName,
                e.departmentName,
                e.performerId,
                e.performerName,
                r.resultManagerId,
                r.resultManagerName,
                r.resultItemCode,
                r.resultSummary,
                r.unit,
                r.referenceRange,
                r.judgement,
                r.confirmedAt,
                r.status,
                r.progressStatus,
                r.createdAt,
                r.updatedAt,
                r.completedAt
            )
            from SpecimenTestResultEntity r
            join com.app.medical_support.diagnosticexecution.entity.SpecimenEntity e
              on r.specimenExamId = e.specimenExamId
            """)
    List<SpecimenTestResultDTO> findSpecimenResultResponseList();

    @Query("""
            select new com.app.medical_support.diagnosticresult.dto.SpecimenTestResultDTO(
                r.specimenExamResultId,
                r.specimenExamId,
                e.testExecutionId,
                e.detailCode,
                e.patientId,
                e.patientName,
                e.departmentName,
                e.performerId,
                e.performerName,
                r.resultManagerId,
                r.resultManagerName,
                r.resultItemCode,
                r.resultSummary,
                r.unit,
                r.referenceRange,
                r.judgement,
                r.confirmedAt,
                r.status,
                r.progressStatus,
                r.createdAt,
                r.updatedAt,
                r.completedAt
            )
            from SpecimenTestResultEntity r
            join com.app.medical_support.diagnosticexecution.entity.SpecimenEntity e
              on r.specimenExamId = e.specimenExamId
            where r.specimenExamResultId = :id
            """)
    Optional<SpecimenTestResultDTO> findSpecimenResultResponseDetail(@Param("id") String id);
}
