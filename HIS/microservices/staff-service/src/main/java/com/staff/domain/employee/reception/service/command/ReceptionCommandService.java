package com.staff.domain.employee.reception.service.command;

import com.staff.domain.employee.reception.dto.ReceptionRequestDTO;
import com.staff.domain.employee.reception.dto.ReceptionResponseDTO;

public interface ReceptionCommandService {

    String createReception(ReceptionRequestDTO receptionReq);

    ReceptionResponseDTO updateReception(String staffId, ReceptionRequestDTO receptionReq);

    void deleteReception(String staffId);
}
