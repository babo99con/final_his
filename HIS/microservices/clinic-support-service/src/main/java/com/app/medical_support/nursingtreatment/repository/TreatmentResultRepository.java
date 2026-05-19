package com.app.medical_support.nursingtreatment.repository;

import com.app.medical_support.nursingtreatment.entity.TreatmentResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreatmentResultRepository extends JpaRepository<TreatmentResultEntity, String> {
}
