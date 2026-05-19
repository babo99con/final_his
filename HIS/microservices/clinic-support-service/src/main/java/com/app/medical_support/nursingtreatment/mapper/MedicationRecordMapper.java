package com.app.medical_support.nursingtreatment.mapper;

import com.app.medical_support.nursingtreatment.dto.MedicationRecordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MedicationRecordMapper {

    List<MedicationRecordDTO> searchMedicationRecord(
            @Param("patientName") String patientName,
            @Param("departmentName") String departmentName,
            @Param("progressStatus") String progressStatus,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );
}
