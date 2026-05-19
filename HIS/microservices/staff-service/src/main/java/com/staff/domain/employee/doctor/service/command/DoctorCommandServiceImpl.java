package com.staff.domain.employee.doctor.service.command;

import com.staff.common.exception.EntityNotFoundException;
import com.staff.common.exception.BusinessException;
import com.staff.domain.employee.basicInfo.dto.StaffResponseDTO;
import com.staff.domain.employee.basicInfo.dto.StaffUpdateRequestDTO;
import com.staff.domain.employee.basicInfo.entity.StaffEntity;
import com.staff.domain.employee.basicInfo.enums.StaffRoleType;
import com.staff.domain.employee.basicInfo.repository.StaffRepository;
import com.staff.domain.employee.basicInfo.validator.StaffCommonValidator;
import com.staff.domain.employee.doctor.dto.DoctorRequestDTO;
import com.staff.domain.employee.doctor.dto.DoctorResponseDTO;
import com.staff.domain.employee.doctor.entity.DoctorEntity;
import com.staff.domain.employee.doctor.mapstruct.DoctorReqMapStruct;
import com.staff.domain.employee.doctor.repository.DoctorRepository;
import com.staff.domain.employee.doctor.service.operation.DoctorProfileImageService;
import com.staff.domain.employee.doctor.service.query.DoctorQueryService;
import com.staff.domain.employee.doctor.validator.DoctorValidator;
import com.staff.storage.minio.dto.UploadResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorCommandServiceImpl implements DoctorCommandService {
    private final DoctorRepository doctorRepository;
    private final DoctorReqMapStruct doctorReqMapStruct;

    private final StaffRepository staffRepository;
    private final DoctorValidator doctorValidator;

    private final DoctorProfileImageService doctorProfileImageService;
    private final DoctorQueryService doctorQueryService;

    private final StaffCommonValidator staffCommonValidator;

    //⚙️ 추가
    @Override
    public String createDoctor(DoctorRequestDTO doctorReq) {


        //💡(유효성 ) 필수값 처리
        doctorValidator.validateCreateRequest(doctorReq);

        //❗
        if (!staffRepository.existsById(doctorReq.getStaffId())) {
            throw new BusinessException("공통 직원 정보가 없습니다. staffId=" + doctorReq.getStaffId());
        }
        DoctorEntity doctor = doctorReqMapStruct.toEntity(doctorReq);

        //의사번호 필수
        doctor.setLicenseNo(staffCommonValidator.requireText(doctorReq.getLicenseNo(),"의사번호는 필수입니다"));


        //📦(enums) 이넘 공통타입 삽입 (의사)
        doctor.setDoctorType(StaffRoleType.DOCTOR.name());

        DoctorEntity saved = doctorRepository.save(doctor);

        return saved.getStaffId();

    }

        //⚙️ 업데이트
        @Override
        public DoctorResponseDTO updateDoctor(String staffId, DoctorRequestDTO doctorReq) {

        //❗
        DoctorEntity doctor = doctorRepository.findById(staffId).orElseThrow(() ->
                new EntityNotFoundException("의사 정보를 찾을 수 없습니다. staffId=" + staffId));

        //💡(유효성 )낫널 예외처리
        doctorValidator.validateUpdateRequest(staffId, doctorReq);

        //의사번호 필수
        doctor.setLicenseNo(staffCommonValidator.requireText(doctorReq.getLicenseNo(),"의사번호는 필수입니다"));

        doctor.setEducation(doctorReq.getEducation());
        doctor.setCareerDetail(doctorReq.getCareerDetail());

        //진로과목
        doctor.setSpecialtyId(doctorReq.getSpecialtyId());

        //프로필소개
        doctor.setProfileSummary(doctorReq.getProfileSummary());
        doctor.setExtNo(doctorReq.getExtNo());

        doctorRepository.flush();

        return doctorQueryService.getDoctorDetail(staffId);

    }






    //⚙️ (영구)삭제 (지금은 사용 안하는중)
    @Override
    public void deleteDoctor(String staffId) {
        // 💡.(유효성) 경로값 해당유저 자체 먼저 검증
        doctorValidator.validateDoctorId(staffId);

        //❗
        DoctorEntity doctor = doctorRepository.findById(staffId).orElseThrow(() ->
                new EntityNotFoundException("삭제할 의사 정보를 찾을 수 없습니다. staffId=" + staffId));




        //삭제
        doctorRepository.delete(doctor);
        

    }


    //🧠 업로드  (퍼사드로 연결 중)
    @Override
    public UploadResDTO uploadDoctorProfileImage(String staffId, MultipartFile file) {
        return doctorProfileImageService.uploadProfileImage(staffId, file); }
}
