package kr.co.hospital.patients.patient.repository;

import kr.co.hospital.patients.patient.entity.MemoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<MemoEntity, Long> {
}
