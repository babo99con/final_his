package com.staff.facade.EmployeeProfile.facade;

import com.staff.domain.employee.doctor.dto.DoctorRequestDTO;
import com.staff.domain.employee.doctor.dto.DoctorResponseDTO;
import com.staff.domain.employee.nurse.dto.NurseRequestDTO;
import com.staff.domain.employee.nurse.dto.NurseResponseDTO;
import com.staff.domain.employee.reception.dto.ReceptionRequestDTO;
import com.staff.domain.employee.reception.dto.ReceptionResponseDTO;
import com.staff.storage.minio.dto.UploadResDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeProfileFacade {

    //의사
    List<DoctorResponseDTO> listDoctors();

    List<DoctorResponseDTO> searchDoctors(String search, String searchType);

    DoctorResponseDTO getDoctorDetail(String staffId);

    DoctorResponseDTO updateDoctor(String staffId, DoctorRequestDTO doctorReq);

    UploadResDTO uploadDoctorProfileImage(String staffId, MultipartFile file);



    //간호사
    List<NurseResponseDTO> listNurses();

    List<NurseResponseDTO> searchNurses(String search, String searchType);

    NurseResponseDTO getNurseDetail(String staffId);

    NurseResponseDTO updateNurse(String staffId, NurseRequestDTO requestDTO);

    UploadResDTO uploadNurseProfileImage(String staffId, MultipartFile file);



    //원무과
    List<ReceptionResponseDTO> listReceptions();

    List<ReceptionResponseDTO> searchReceptions(String search, String searchType);

    ReceptionResponseDTO getReceptionDetail(String staffId);

    ReceptionResponseDTO updateReception(String staffId, ReceptionRequestDTO requestDTO);

    void deleteReception(String staffId);
}
