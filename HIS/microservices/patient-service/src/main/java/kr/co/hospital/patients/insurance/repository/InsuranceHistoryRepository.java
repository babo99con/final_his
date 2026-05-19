package kr.co.hospital.patients.insurancehistory.repository;

import kr.co.hospital.patients.insurancehistory.entity.InsuranceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceHistoryRepository extends JpaRepository<InsuranceHistoryEntity, Long> {
    List<InsuranceHistoryEntity> findByPatientIdOrderByChangedAtDesc(Long patientId);
}
