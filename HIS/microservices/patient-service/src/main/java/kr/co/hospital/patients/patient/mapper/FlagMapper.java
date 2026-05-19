package kr.co.hospital.patients.patient.mapper;

import kr.co.hospital.patients.patient.entity.FlagEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FlagMapper {

    List<FlagEntity> search(
            @Param("type") String type,
            @Param("keyword") String keyword
    );
}