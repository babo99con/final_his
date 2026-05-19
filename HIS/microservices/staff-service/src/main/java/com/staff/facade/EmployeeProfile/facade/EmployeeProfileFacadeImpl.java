package com.staff.facade.EmployeeProfile.facade;

import com.staff.domain.employee.basicInfo.validator.StaffCommonValidator;
import com.staff.domain.employee.doctor.dto.DoctorRequestDTO;
import com.staff.domain.employee.doctor.dto.DoctorResponseDTO;
import com.staff.domain.employee.doctor.service.command.DoctorCommandService;
import com.staff.domain.employee.doctor.service.query.DoctorQueryService;
import com.staff.domain.employee.doctor.validator.DoctorValidator;
import com.staff.domain.employee.nurse.dto.NurseRequestDTO;
import com.staff.domain.employee.nurse.dto.NurseResponseDTO;
import com.staff.domain.employee.nurse.service.command.NurseCommandService;
import com.staff.domain.employee.nurse.service.query.NurseQueryService;
import com.staff.domain.employee.nurse.validator.NurseValidator;
import com.staff.domain.employee.reception.dto.ReceptionRequestDTO;
import com.staff.domain.employee.reception.dto.ReceptionResponseDTO;
import com.staff.domain.employee.reception.service.command.ReceptionCommandService;
import com.staff.domain.employee.reception.service.query.ReceptionQueryService;
import com.staff.domain.employee.reception.validator.ReceptionValidator;
import com.staff.facade.EmployeeProfile.command.DoctorProfileCommand;
import com.staff.facade.EmployeeProfile.command.NurseProfileCommand;
import com.staff.facade.EmployeeProfile.command.ReceptionProfileCommand;
import com.staff.storage.minio.dto.UploadResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeProfileFacadeImpl implements EmployeeProfileFacade {

    private final DoctorQueryService doctorQueryService;
    private final DoctorCommandService doctorCommandService;
    private final NurseQueryService nurseQueryService;
    private final NurseCommandService nurseCommandService;
    private final ReceptionQueryService receptionQueryService;
    private final ReceptionCommandService receptionCommandService;
    private final StaffCommonValidator staffCommonValidator;
    private final DoctorValidator doctorValidator;
    private final NurseValidator nurseValidator;
    private final ReceptionValidator receptionValidator;


    //의사
    @Override
    public List<DoctorResponseDTO> listDoctors() {
        return doctorQueryService.listDoctors();
    }

    @Override
    public List<DoctorResponseDTO> searchDoctors(String search, String searchType) {
        return doctorQueryService.searchDoctors(search, searchType);
    }

    @Override
    public DoctorResponseDTO getDoctorDetail(String staffId) {
        doctorValidator.validateDoctorId(staffId);
        return doctorQueryService.getDoctorDetail(staffId);
    }

    @Override
    @Transactional
    public DoctorResponseDTO updateDoctor(String staffId, DoctorRequestDTO doctorReq) {
        doctorValidator.validateDoctorId(staffId);
        DoctorProfileCommand command = DoctorProfileCommand.from(staffId, doctorReq);
        return doctorCommandService.updateDoctor(command.staffId(), command.toRequestDTO());
    }

    @Override
    @Transactional
    public UploadResDTO uploadDoctorProfileImage(String staffId, MultipartFile file) {
        doctorValidator.validateDoctorId(staffId);
        staffCommonValidator.validateUploadFile(file);
        return doctorCommandService.uploadDoctorProfileImage(staffId, file);
    }

    
    
    //간호사
    @Override
    @Transactional(readOnly = true)
    public List<NurseResponseDTO> listNurses() {
        return nurseQueryService.listNurses();
    }

    @Override
    public List<NurseResponseDTO> searchNurses(String search, String searchType) {
        return nurseQueryService.searchNurses(search, searchType);
    }

    @Override
    public NurseResponseDTO getNurseDetail(String staffId) {
        nurseValidator.validateNurseId(staffId);
        return nurseQueryService.getNurseDetail(staffId);
    }

    @Override
    @Transactional
    public NurseResponseDTO updateNurse(String staffId, NurseRequestDTO nurseReq) {
        NurseProfileCommand command = NurseProfileCommand.from(staffId, nurseReq);
        return nurseCommandService.updateNurse(command.staffId(), command.toRequestDTO());
    }

    @Override
    @Transactional
    public UploadResDTO uploadNurseProfileImage(String staffId, MultipartFile file) {
        nurseValidator.validateNurseId(staffId);
        staffCommonValidator.validateUploadFile(file);
        return nurseCommandService.uploadNurseProfileImage(staffId, file);
    }


    
    
    
    //원무과
    @Override
    @Transactional(readOnly = true)
    public List<ReceptionResponseDTO> listReceptions() {
        return receptionQueryService.listReceptions();
    }

    @Override
    public List<ReceptionResponseDTO> searchReceptions(String search, String searchType) {
        return receptionQueryService.searchReceptions(search, searchType);
    }

    @Override
    public ReceptionResponseDTO getReceptionDetail(String staffId) {
        receptionValidator.validateReceptionId(staffId);
        return receptionQueryService.getReceptionDetail(staffId);
    }

    @Override
    @Transactional
    public ReceptionResponseDTO updateReception(String staffId, ReceptionRequestDTO requestDTO) {
        ReceptionProfileCommand command = ReceptionProfileCommand.from(staffId, requestDTO);
        return receptionCommandService.updateReception(command.staffId(), command.toRequestDTO());
    }
    @Override
    @Transactional
    public void deleteReception(String staffId) {
        receptionValidator.validateReceptionId(staffId);
        receptionCommandService.deleteReception(staffId);
    }

}
