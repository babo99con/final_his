package com.staff.domain.employee.nurse.service.command;

import com.staff.domain.employee.nurse.dto.NurseRequestDTO;
import com.staff.domain.employee.nurse.dto.NurseResponseDTO;
import com.staff.storage.minio.dto.UploadResDTO;
import org.springframework.web.multipart.MultipartFile;

public interface NurseCommandService {

    String createNurse(NurseRequestDTO nurseReq);

    NurseResponseDTO updateNurse(String staffId, NurseRequestDTO nurseReq);

    NurseResponseDTO deleteNurse(String staffId);

    UploadResDTO uploadNurseProfileImage(String staffId, MultipartFile file);
}
