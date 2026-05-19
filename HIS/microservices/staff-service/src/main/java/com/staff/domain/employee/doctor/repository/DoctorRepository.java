package com.staff.domain.employee.doctor.repository;

import com.staff.domain.employee.doctor.entity.DoctorEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DoctorRepository extends JpaRepository<DoctorEntity, String> {

    @Modifying
    @Query("delete from DoctorEntity d where d.staffId = :staffId")
    int deleteByStaffIdDirect(@Param("staffId") String staffId);

}
