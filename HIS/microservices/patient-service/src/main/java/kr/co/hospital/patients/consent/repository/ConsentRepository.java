package kr.co.hospital.patients.consent.repository;

import kr.co.hospital.patients.consent.entity.ConsentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsentRepository extends JpaRepository<ConsentEntity, Long> {
    List<ConsentEntity> findByPatientIdOrderByCreatedAtDesc(Long patientId);
    Optional<ConsentEntity> findByConsentIdAndPatientId(Long consentId, Long patientId);
    boolean existsByConsentIdAndPatientId(Long consentId, Long patientId);

    @Query("""
            select c from ConsentEntity c
            where (c.createdAt >= :start and c.createdAt < :end)
               or (c.updatedAt >= :start and c.updatedAt < :end)
            order by coalesce(c.updatedAt, c.createdAt) desc
            """)
    List<ConsentEntity> findByCreatedAtOrUpdatedAtToday(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}
