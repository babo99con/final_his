package com.staff.domain.employee.reception.service.query;

import com.staff.domain.employee.reception.dto.ReceptionResponseDTO;

import java.util.List;

public interface ReceptionQueryService {

    List<ReceptionResponseDTO> listReceptions();

    List<ReceptionResponseDTO> searchReceptions(String search, String searchType);

    ReceptionResponseDTO getReceptionDetail(String staffId);
}
