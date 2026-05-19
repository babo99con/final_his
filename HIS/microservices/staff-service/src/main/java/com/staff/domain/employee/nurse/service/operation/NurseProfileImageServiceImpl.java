package com.staff.domain.employee.nurse.service.operation;

import com.staff.common.exception.EntityNotFoundException;
import com.staff.domain.employee.basicInfo.validator.StaffCommonValidator;
import com.staff.domain.employee.nurse.entity.NurseEntity;
import com.staff.domain.employee.nurse.repository.NurseRepository;
import com.staff.domain.employee.nurse.validator.NurseValidator;
import com.staff.storage.minio.dto.UploadResDTO;
import com.staff.storage.minio.service.MinioStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class NurseProfileImageServiceImpl implements NurseProfileImageService {
    private final NurseRepository nurseRepository;
    private final MinioStorageService minioStorageService;
    private final NurseValidator nurseValidator;
    private final StaffCommonValidator staffCommonValidator;
    @Override
    public UploadResDTO uploadProfileImage(String staffId, MultipartFile file) {
        nurseValidator.validateNurseId(staffId); staffCommonValidator.validateUploadFile(file);
        UploadResDTO uploaded = minioStorageService.upload(file, "nurse/nurse-" + staffId + "/profile");
        NurseEntity nurse = nurseRepository.findById(staffId).orElseThrow(() -> new EntityNotFoundException("간호사 정보가 없습니다. staffId=" + staffId));
        nurse.setNurseFileUrl(uploaded.getFileUrl()); nurseRepository.flush(); return uploaded;
    }
}
