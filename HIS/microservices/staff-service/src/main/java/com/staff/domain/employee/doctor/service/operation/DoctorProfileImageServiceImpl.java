package com.staff.domain.employee.doctor.service.operation;

import com.staff.common.exception.EntityNotFoundException;
import com.staff.domain.employee.basicInfo.validator.StaffCommonValidator;
import com.staff.domain.employee.doctor.entity.DoctorEntity;
import com.staff.domain.employee.doctor.repository.DoctorRepository;
import com.staff.domain.employee.doctor.validator.DoctorValidator;
import com.staff.storage.minio.dto.UploadResDTO;
import com.staff.storage.minio.service.MinioStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class DoctorProfileImageServiceImpl implements DoctorProfileImageService {
    private final DoctorRepository doctorRepository;
    private final MinioStorageService minioStorageService;
    private final DoctorValidator doctorValidator;
    private final StaffCommonValidator staffCommonValidator;


    @Override
    public UploadResDTO uploadProfileImage(String staffId, MultipartFile file) {

        doctorValidator.validateDoctorId(staffId);

        staffCommonValidator.validateUploadFile(file);

        UploadResDTO uploaded = minioStorageService.upload(file, "doctor/doctor-" + staffId + "/profile");

        DoctorEntity doctor = doctorRepository.findById(staffId).orElseThrow(() ->
                new EntityNotFoundException("의사 정보가 없습니다. staffId=" + staffId));

        doctor.setDoctorFileUrl(uploaded.getFileUrl()); doctorRepository.flush();
        return uploaded;
    }
}
