package com.staff.facade.EmployeeCreate.facade;

import com.staff.domain.employee.basicInfo.dto.StaffRequestDTO;
import com.staff.domain.employee.basicInfo.service.command.StaffCommonService;
import com.staff.domain.employee.doctor.dto.DoctorRequestDTO;
import com.staff.domain.employee.doctor.entity.DoctorEntity;
import com.staff.domain.employee.doctor.service.command.DoctorCommandService;
import com.staff.domain.employee.doctor.validator.DoctorValidator;
import com.staff.domain.employee.nurse.dto.NurseRequestDTO;
import com.staff.domain.employee.nurse.service.command.NurseCommandService;
import com.staff.domain.employee.nurse.validator.NurseValidator;
import com.staff.domain.employee.reception.dto.ReceptionRequestDTO;
import com.staff.domain.employee.reception.service.command.ReceptionCommandService;
import com.staff.domain.employee.reception.validator.ReceptionValidator;
import com.staff.facade.EmployeeCreate.command.BasicInfoCreate;
import com.staff.facade.EmployeeCreate.command.DoctorCreate;
import com.staff.facade.EmployeeCreate.command.NurseCreate;
import com.staff.facade.EmployeeCreate.command.ReceptionCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeCreateFacadeImpl implements EmployeeCreateFacade {

    private final StaffCommonService staffCommonService;
    private final DoctorValidator doctorValidator;
    private final NurseValidator nurseValidator;
    private final ReceptionValidator receptionValidator;

    private final StaffCommonService staffCommandService;

    private final DoctorCommandService doctorCommandService;

    private final NurseCommandService nurseCommandService;

    private final ReceptionCommandService receptionCommandService;

    //의사공통생성 트랙잭션
    @Override
    @Transactional
    public String createDoctor(DoctorRequestDTO doctorReq) {


        // 1. 전체 요청 검증 (면허만 예외처리)
        doctorValidator.validateCreateRequest(doctorReq);

        // 2. 공통 직원 생성용으로 변환 (레코드 참조)
        BasicInfoCreate basicInfoCreate = BasicInfoCreate.from(doctorReq);
        StaffRequestDTO staffRequestDTO = basicInfoCreate.toRequestDTO();


        // 3. 공통 직원 먼저 insert (jpa)
        staffCommandService.createStaff(staffRequestDTO);

        // 4. 의사 상세 생성용으로 변환
        DoctorCreate doctorCreate = DoctorCreate.from(doctorReq);

        // 5. 의사 상세 insert
        return doctorCommandService.createDoctor(doctorCreate.toRequestDTO());


    }


    //간호사공통생성 트랙잭션
    @Override
    @Transactional
    public String createNurse(NurseRequestDTO nurseReq) {

        //검증
        nurseValidator.validateCreateRequest(nurseReq);

        // 2. 공통 직원 생성용으로 변환
        BasicInfoCreate basicInfoCreate = BasicInfoCreate.from(nurseReq);
        StaffRequestDTO staffRequestDTO = basicInfoCreate.toRequestDTO();

        // 3. 공통 직원 먼저 insert
        staffCommandService.createStaff(staffRequestDTO);

        NurseCreate command = NurseCreate.from(nurseReq);

        return nurseCommandService.createNurse(command.toRequestDTO());


    }

    //원무공통생성 트랙잭션
    @Override
    @Transactional
    public String createReception(ReceptionRequestDTO receptionReq) {

        //검증
        receptionValidator.validateCreateRequest(receptionReq);

        // 2. 공통 직원 생성용으로 변환
        BasicInfoCreate basicInfoCreate = BasicInfoCreate.from(receptionReq);
        StaffRequestDTO staffRequestDTO = basicInfoCreate.toRequestDTO();

        // 3. 공통 직원 먼저 insert
        staffCommandService.createStaff(staffRequestDTO);

        ReceptionCreate command = ReceptionCreate.from(receptionReq);

        return receptionCommandService.createReception(command.toRequestDTO());
    }
}
