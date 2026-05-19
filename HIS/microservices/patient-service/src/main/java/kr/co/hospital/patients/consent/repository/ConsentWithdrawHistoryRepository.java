package kr.co.hospital.patients.consent.repository;

import kr.co.hospital.patients.consent.entity.ConsentWithdrawHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsentWithdrawHistoryRepository
        extends JpaRepository<ConsentWithdrawHistoryEntity, Long> {

    List<ConsentWithdrawHistoryEntity> findByPatientIdOrderByCreatedAtDesc(Long patientId);
}
