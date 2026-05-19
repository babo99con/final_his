package com.staff.domain.employee.doctor.service.query;

import com.staff.domain.employee.doctor.dto.DoctorResponseDTO;
import java.util.List;

public interface DoctorQueryService {
    //전체조회
    List<DoctorResponseDTO> listDoctors();

    /** 검색 조회 */
    List<DoctorResponseDTO> searchDoctors(String search, String searchType);

    //단건조회
    DoctorResponseDTO getDoctorDetail(String staffId);
}
