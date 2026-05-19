package com.staff.domain.employee.nurse.service.operation;

import com.staff.storage.minio.dto.UploadResDTO;
import org.springframework.web.multipart.MultipartFile;

public interface NurseProfileImageService {
    UploadResDTO uploadProfileImage(String staffId, MultipartFile file);
}
