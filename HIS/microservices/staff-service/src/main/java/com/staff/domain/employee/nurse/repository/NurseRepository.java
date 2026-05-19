package com.staff.domain.employee.nurse.repository;

import com.staff.domain.employee.nurse.entity.NurseEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NurseRepository extends JpaRepository<NurseEntity, String> {

    @Modifying
    @Query("delete from NurseEntity n where n.staffId = :staffId")
    int deleteByStaffIdDirect(@Param("staffId") String staffId);
}