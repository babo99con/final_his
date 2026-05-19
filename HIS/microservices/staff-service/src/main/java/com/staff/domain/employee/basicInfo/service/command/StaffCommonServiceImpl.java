package com.staff.domain.employee.basicInfo.service.command;

import com.staff.common.exception.BusinessException;
import com.staff.domain.employee.basicInfo.dto.StaffRequestDTO;
import com.staff.domain.employee.basicInfo.dto.StaffResponseDTO;
import com.staff.domain.employee.basicInfo.dto.StaffUpdateRequestDTO;
import com.staff.domain.employee.basicInfo.entity.StaffEntity;
import com.staff.domain.employee.basicInfo.mapstruct.StaffReqMapStruct;
import com.staff.domain.employee.basicInfo.mapstruct.StaffResMapStruct;
import com.staff.domain.employee.basicInfo.repository.StaffRepository;
import com.staff.domain.employee.basicInfo.validator.StaffCommonValidator;
import com.staff.domain.employee.doctor.repository.DoctorRepository;
import com.staff.domain.employee.nurse.repository.NurseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.web.servlet.function.ServerResponse.status;

/**DB변경용 .*   공통은 아직 퍼사드로 안뺌*/
@Service
@RequiredArgsConstructor
@Transactional
public class StaffCommonServiceImpl implements StaffCommonService {

    private final StaffRepository staffRepository;
    private final StaffReqMapStruct staffReqMapStruct;
    private final StaffResMapStruct staffResMapStruct;  //예외처리 응답반환 할려고 썼음


    private final DoctorRepository doctorRepository;
    private final NurseRepository nurseRepository;

    private final StaffCommonValidator staffCommonValidator; //예외 처리용




                //추가
                @Override
                public StaffResponseDTO createStaff(StaffRequestDTO staffReq) {
                if (staffReq == null) {
                throw new BusinessException("직원 생성 요청이 없습니다.");
                }

                String staffId = staffCommonValidator.requireText(staffReq.getStaffId(), "STAFF_ID는 필수입니다.");
                if (staffRepository.existsById(staffId)) {
                throw new BusinessException("이미 존재하는 STAFF_ID 입니다. staffId=" + staffId);
                }
                //(액티브) (활동)처리값 예외처리 가져오기
                String normalizedStatus = staffCommonValidator.trimToNull(staffReq.getStatus());
                //다른곳에도 (의사 간호사 ) 사용하기 위함
                StaffRequestDTO normalized = StaffRequestDTO.builder().staffId(staffId)

                //빈문자열 공백 제거 예외처리
                .deptId(staffCommonValidator.requireText(staffReq.getDeptId(), "부서 ID는 필수입니다."))
                //빈문자열 공백 제거 예외처리
                .name(staffCommonValidator.requireText(staffReq.getName(), "직원 이름은 필수입니다."))

                //남은 문자열 공백 널로처리
                .phone(staffCommonValidator.trimToNull(staffReq.getPhone()))
                //남은 문자열 공백 널로처리
                .email(staffCommonValidator.trimToNull(staffReq.getEmail()))
                //남은 문자열 공백 널로처리
                .birthDate(staffCommonValidator.trimToNull(staffReq.getBirthDate()))
                //남은 문자열 공백 널로처리
                .genderCode(staffCommonValidator.trimToNull(staffReq.getGenderCode()))
                //남은 문자열 공백 널로처리
                .zipCode(staffCommonValidator.trimToNull(staffReq.getZipCode()))
                //남은 문자열 공백 널로처리
                .address1(staffCommonValidator.trimToNull(staffReq.getAddress1()))
                //남은 문자열 공백 널로처리
                .address2(staffCommonValidator.trimToNull(staffReq.getAddress2()))

                
                // 스테이트가 널이면 ACTIVE (액티브) (활동)처리
                .status(normalizedStatus == null ? "ACTIVE" : normalizedStatus)
                .build();


                 StaffEntity saved = staffRepository.save(staffReqMapStruct.toEntity(normalized));

                 return staffResMapStruct.toDTO(saved);
                 }



                //수정
                @Override
                public StaffResponseDTO updateStaff(String staffId, StaffUpdateRequestDTO requestDTO) {

                if (requestDTO == null) {
                throw new BusinessException("직원 수정 요청이 없습니다.");
                }

        String normalizedStaffId = staffCommonValidator.requireText(staffId, "수정할 STAFF_ID가 없습니다.");


        StaffEntity entity = staffRepository.findById(normalizedStaffId)
                .orElseThrow(() -> new BusinessException("수정할 직원 정보가 없습니다. staffId=" + normalizedStaffId));



        //빈문자열 공백 제거 예외처리
        entity.setDeptId(staffCommonValidator.requireText(requestDTO.getDeptId(), "부서 ID는 필수입니다."));
        //빈문자열 공백 제거 예외처리
        entity.setName(staffCommonValidator.requireText(requestDTO.getName(), "직원 이름은 필수입니다."));

        entity.setPhone(staffCommonValidator.trimToNull(requestDTO.getPhone()));

        entity.setEmail(staffCommonValidator.trimToNull(requestDTO.getEmail()));

        entity.setBirthDate(staffCommonValidator.trimToNull(requestDTO.getBirthDate()));

        entity.setGenderCode(staffCommonValidator.trimToNull(requestDTO.getGenderCode()));

        entity.setZipCode(staffCommonValidator.trimToNull(requestDTO.getZipCode()));

        entity.setAddress1(staffCommonValidator.trimToNull(requestDTO.getAddress1()));

        entity.setAddress2(staffCommonValidator.trimToNull(requestDTO.getAddress2()));

        entity.setStatus(staffCommonValidator.trimToNull(requestDTO.getStatus()));

        return staffResMapStruct.toDTO(entity);
        }




        //영구삭제
        @Override
        public StaffResponseDTO deleteStaff(String staffId) {



        String StaffId = staffCommonValidator.requireText(staffId, "삭제할 STAFF_ID가 없습니다.");
        StaffEntity entity = staffRepository.findById(StaffId)
                .orElseThrow(() -> new BusinessException("삭제할 직원 정보가 없습니다. staffId=" + StaffId));

        //자식부터 먼저 삭제
        doctorRepository.deleteByStaffIdDirect(staffId);
        nurseRepository.deleteByStaffIdDirect(staffId);

        //그후 부모삭제
        int deleted = staffRepository.deleteByStaffIdDirect(staffId);


        //나중에 케이스으로 고정시 이거 하나로삭제
        //employeeRepository.deleteById(staffId);

        if (deleted == 0) {
            throw new BusinessException("직원을 찾을 수 없습니다. staffId=" + staffId);
        }


        return  staffResMapStruct.toDTO(entity);
        }
        }
