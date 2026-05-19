package com.staff.domain.employee.doctor.service.command;

import com.staff.domain.employee.doctor.dto.DoctorRequestDTO;
import com.staff.domain.employee.doctor.dto.DoctorResponseDTO;
import com.staff.storage.minio.dto.UploadResDTO;
import org.springframework.web.multipart.MultipartFile;

public interface DoctorCommandService {


    //⚙️ 추가
    String createDoctor(DoctorRequestDTO doctorReq);

    //⚙️ 업데이트
    DoctorResponseDTO updateDoctor(String staffId, DoctorRequestDTO doctorReq);

    //⚙️ 삭제
    void deleteDoctor(String staffId);

    //🧠 업로드
    UploadResDTO uploadDoctorProfileImage(String staffId, MultipartFile file);
}
