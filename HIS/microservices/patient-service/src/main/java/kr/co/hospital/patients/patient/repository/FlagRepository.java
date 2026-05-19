package kr.co.hospital.patients.patient.repository;

import kr.co.hospital.patients.patient.entity.FlagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlagRepository extends JpaRepository<FlagEntity, Long> {
}