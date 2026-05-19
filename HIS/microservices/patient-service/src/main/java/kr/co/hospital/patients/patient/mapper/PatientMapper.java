package kr.co.hospital.patients.patient.mapper;

import kr.co.hospital.patients.patient.entity.PatientEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PatientMapper {

    List<PatientEntity> search(
            @Param("type") String type,
            @Param("keyword") String keyword
    );
    List<PatientEntity> searchMulti(
            @Param("name") String name,
            @Param("birthDate") String birthDate,
            @Param("phone") String phone
    );

    List<PatientEntity> identifyStrong(
            @Param("name") String name,
            @Param("birthDate") String birthDate,
            @Param("phoneDigits") String phoneDigits
    );

    List<PatientEntity> identifyNameBirth(
            @Param("name") String name,
            @Param("birthDate") String birthDate
    );

    List<PatientEntity> identifyNamePhone(
            @Param("name") String name,
            @Param("phoneDigits") String phoneDigits
    );

    List<PatientEntity> identifyBirthPhone(
            @Param("birthDate") String birthDate,
            @Param("phoneDigits") String phoneDigits
    );
}

