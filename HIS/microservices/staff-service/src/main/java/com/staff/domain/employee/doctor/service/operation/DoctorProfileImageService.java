package com.staff.domain.employee.doctor.service.operation;

import com.staff.storage.minio.dto.UploadResDTO;
import org.springframework.web.multipart.MultipartFile;

public interface DoctorProfileImageService {
    UploadResDTO uploadProfileImage(String staffId, MultipartFile file);
}
