package kr.co.hospital.patients.patient.mapper;

import kr.co.hospital.patients.patient.entity.MemoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemoMapper {

    List<MemoEntity> search(
            @Param("type") String type,
            @Param("keyword") String keyword
    );
}
