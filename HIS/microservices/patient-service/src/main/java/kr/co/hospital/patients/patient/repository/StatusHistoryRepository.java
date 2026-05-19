package kr.co.hospital.patients.patient.repository;

import kr.co.hospital.patients.patient.entity.StatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusHistoryRepository extends JpaRepository<StatusHistoryEntity, Long> {
}