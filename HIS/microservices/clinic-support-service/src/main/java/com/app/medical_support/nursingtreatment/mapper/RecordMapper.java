package com.app.medical_support.nursingtreatment.mapper;

import com.app.medical_support.nursingtreatment.dto.RecordResponseDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface  RecordMapper {
    List<RecordResponseDTO> search (
            @Param("searchType") String searchType,
            @Param("searchValue") String searchValue,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    List<RecordResponseDTO> findRecordList();

    RecordResponseDTO findRecordDetail(@Param("recordId") String recordId);
}
