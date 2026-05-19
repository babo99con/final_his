package com.staff.domain.employee.nurse.controller;

import com.hms.util.api.ApiResponse;
import com.staff.facade.EmployeeProfile.facade.EmployeeProfileFacade;
import com.staff.storage.minio.dto.UploadResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/nurse")
public class NurseProfileController {
    private final EmployeeProfileFacade employeeProfileFacade;
    @PostMapping("/profile/upload/{staffId}")
    public ResponseEntity<ApiResponse<UploadResDTO>> uploadNurseProfileImage(
            @PathVariable String staffId, @RequestPart("NurseFile") MultipartFile file) {
        UploadResDTO upload = employeeProfileFacade.uploadNurseProfileImage(staffId, file);
        return ResponseEntity.ok(new ApiResponse<>(true, "간호사 프로필 이미지 업로드 완료", upload));
    }
}
