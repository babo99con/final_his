package kr.co.hospital.patients.insurance.repository;

import kr.co.hospital.patients.insurance.entity.InsuranceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface InsuranceRepository extends JpaRepository<InsuranceEntity, Long> {

    @Query("""
            select i from InsuranceEntity i
            where i.patientId = :patientId
              and i.activeYn = true
              and (i.startDate is null or i.startDate <= :today)
              and (i.endDate is null or i.endDate >= :today)
            order by i.startDate desc nulls last, i.createdAt desc
            """)
    List<InsuranceEntity> findValidByPatientId(
            @Param("patientId") Long patientId,
            @Param("today") LocalDate today
    );

    @Query("""
            select i from InsuranceEntity i
            where (i.createdAt >= :start and i.createdAt < :end)
               or (i.updatedAt >= :start and i.updatedAt < :end)
            order by coalesce(i.updatedAt, i.createdAt) desc
            """)
    List<InsuranceEntity> findByCreatedAtOrUpdatedAtToday(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
