package com.staff.domain.employee.reception.repository;

import com.staff.domain.employee.reception.entity.ReceptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptionRepository extends JpaRepository<ReceptionEntity, String> {
}
