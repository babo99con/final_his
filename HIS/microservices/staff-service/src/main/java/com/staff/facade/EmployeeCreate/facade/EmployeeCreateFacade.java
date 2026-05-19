package com.staff.facade.EmployeeCreate.facade;

import com.staff.domain.employee.doctor.dto.DoctorRequestDTO;
import com.staff.domain.employee.nurse.dto.NurseRequestDTO;
import com.staff.domain.employee.reception.dto.ReceptionRequestDTO;

public interface EmployeeCreateFacade {

    String createDoctor(DoctorRequestDTO doctorReq);

    String createNurse(NurseRequestDTO nurseReq);

    String createReception(ReceptionRequestDTO receptionReq);
}
