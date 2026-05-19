package com.staff.domain.employee.nurse.service.query;

import com.staff.domain.employee.nurse.dto.NurseResponseDTO;
import java.util.List;

public interface NurseQueryService {

    List<NurseResponseDTO> listNurses();
    /** 검색 조회 */
    List<NurseResponseDTO> searchNurses(String search, String searchType);

    NurseResponseDTO getNurseDetail(String staffId);


}
