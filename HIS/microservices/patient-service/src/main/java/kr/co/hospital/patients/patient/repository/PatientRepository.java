package kr.co.hospital.patients.patient.repository;

import kr.co.hospital.patients.patient.entity.PatientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<PatientEntity, Long> {
    List<PatientEntity> findAllByStatusCodeNot(String statusCode);
}
