package kr.co.hospital.patients.patient.repository;

import kr.co.hospital.patients.patient.entity.InfoHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfoHistoryRepository extends JpaRepository<InfoHistoryEntity, Long> {
    List<InfoHistoryEntity> findByPatientIdOrderByChangedAtDesc(Long patientId);
}