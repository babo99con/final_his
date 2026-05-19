package com.staff.domain.employee.basicInfo.repository;

import com.staff.domain.employee.basicInfo.entity.StaffEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffRepository extends JpaRepository<StaffEntity, String> {

    @Modifying
    @Query("delete from StaffEntity p where p.staffId = :staffId")
    int deleteByStaffIdDirect(@Param("staffId") String staffId);


}
