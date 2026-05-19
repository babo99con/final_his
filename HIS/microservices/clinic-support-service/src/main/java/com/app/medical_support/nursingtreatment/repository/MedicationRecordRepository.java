package com.app.medical_support.nursingtreatment.repository;

import com.app.medical_support.nursingtreatment.entity.MedicationRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRecordRepository extends JpaRepository<MedicationRecordEntity, String> {
}
