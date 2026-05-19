package com.staff.domain.employee.nurse.service.command;

import com.staff.common.exception.BusinessException;
import com.staff.common.exception.EntityNotFoundException;
import com.staff.domain.employee.basicInfo.enums.StaffRoleType;
import com.staff.domain.employee.basicInfo.repository.StaffRepository;
import com.staff.domain.employee.nurse.dto.NurseRequestDTO;
import com.staff.domain.employee.nurse.dto.NurseResponseDTO;
import com.staff.domain.employee.nurse.entity.NurseEntity;
import com.staff.domain.employee.nurse.mapstruct.NurseRequestStruct;
import com.staff.domain.employee.nurse.repository.NurseRepository;
import com.staff.domain.employee.nurse.service.operation.NurseProfileImageService;
import com.staff.domain.employee.nurse.service.query.NurseQueryService;
import com.staff.domain.employee.nurse.validator.NurseValidator;
import com.staff.storage.minio.dto.UploadResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class NurseCommandServiceImpl implements NurseCommandService {
    private final NurseRepository nurseRepository;
    private final NurseRequestStruct nurseRequestStruct;
    private final StaffRepository staffRepository;
    private final NurseValidator nurseValidator;
    private final NurseProfileImageService nurseProfileImageService;
    private final NurseQueryService nurseQueryService;

    //추가
    @Override
    public String createNurse(NurseRequestDTO nurseReq) {
        nurseValidator.validateCreateRequest(nurseReq);
        if (!staffRepository.existsById(nurseReq.getStaffId())) {
            throw new BusinessException("공통 직원 정보가 없습니다. staffId=" + nurseReq.getStaffId());
        }
        NurseEntity nurse = nurseRequestStruct.toEntity(nurseReq);
        nurse.setNurseType(StaffRoleType.NURSE.name());
        NurseEntity saved = nurseRepository.save(nurse);
        return saved.getStaffId();
    }



    //수정
    @Override
    public NurseResponseDTO updateNurse(String staffId, NurseRequestDTO nurseReq) {
        nurseValidator.validateUpdateRequest(staffId, nurseReq);
        NurseEntity nurse = nurseRepository.findById(staffId).orElseThrow(() -> new EntityNotFoundException("간호사 정보를 찾을 수 없습니다. staffId=" + staffId));
        nurse.setLicenseNo(nurseReq.getLicenseNo());
        nurse.setShiftType(nurseReq.getShiftType());
        nurse.setEducation(nurseReq.getEducation());
        nurse.setCareerDetail(nurseReq.getCareerDetail());
        nurse.setExtNo(nurseReq.getExtNo());
        nurseRepository.flush();
        return nurseQueryService.getNurseDetail(staffId);
    }








    //삭제 (지금은 사용안하는중)
    @Override
    public NurseResponseDTO deleteNurse(String staffId) {
        NurseResponseDTO dto = nurseQueryService.getNurseDetail(staffId);
        if (!nurseRepository.existsById(staffId)) throw new EntityNotFoundException("삭제할 간호사를 찾을 수 없습니다. staffId=" + staffId);
        nurseRepository.deleteById(staffId); return dto;
    }




    //업로드
    @Override
    public UploadResDTO uploadNurseProfileImage(String staffId, MultipartFile file)
    { return nurseProfileImageService.uploadProfileImage(staffId, file); }
}
