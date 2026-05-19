package kr.co.hospital.patients.patient.repository;

import kr.co.hospital.patients.patient.entity.RestrictionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestrictionRepository extends JpaRepository<RestrictionEntity, Long> {
}
