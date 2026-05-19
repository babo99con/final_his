package kr.co.hospital.patients.patient.repository;

import kr.co.hospital.patients.patient.entity.FamilyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FamilyRepository extends JpaRepository<FamilyEntity, Long> {
    List<FamilyEntity> findAllByPatientIdOrderBySortOrderAsc(Long patientId);
}
