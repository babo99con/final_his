package com.staff.domain.employee.reception.service.command;

import com.staff.common.exception.BusinessException;
import com.staff.common.exception.EntityNotFoundException;
import com.staff.domain.employee.basicInfo.enums.StaffRoleType;
import com.staff.domain.employee.basicInfo.repository.StaffRepository;
import com.staff.domain.employee.reception.dto.ReceptionRequestDTO;
import com.staff.domain.employee.reception.dto.ReceptionResponseDTO;
import com.staff.domain.employee.reception.entity.ReceptionEntity;
import com.staff.domain.employee.reception.mapstruct.ReceptionRequestStruct;
import com.staff.domain.employee.reception.repository.ReceptionRepository;
import com.staff.domain.employee.reception.service.query.ReceptionQueryService;
import com.staff.domain.employee.reception.validator.ReceptionValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class ReceptionCommandServiceImpl implements ReceptionCommandService {

    private final ReceptionRepository receptionRepository;
    private final ReceptionRequestStruct receptionRequestStruct;
    private final StaffRepository staffRepository;
    private final ReceptionValidator receptionValidator;
    private final ReceptionQueryService receptionQueryService;

    @Override
    public String createReception(ReceptionRequestDTO receptionReq) {
        receptionValidator.validateCreateRequest(receptionReq);
        if (!staffRepository.existsById(receptionReq.getStaffId())) {
            throw new BusinessException("공통 직원 정보가 없습니다. staffId=" + receptionReq.getStaffId());
        }
        ReceptionEntity reception = receptionRequestStruct.toEntity(receptionReq);

        reception.setShiftType(normalizeShiftType(receptionReq.getShiftType()));
        reception.setMultiTask(normalizeMultiTask(receptionReq.getMultiTask()));
        reception.setReceptionType(normalizeReceptionType(receptionReq.getReceptionType()));

        ReceptionEntity saved = receptionRepository.save(reception);
        return saved.getStaffId();
    }



    //수정
    @Override
    public ReceptionResponseDTO updateReception(String staffId, ReceptionRequestDTO receptionReq) {
        receptionValidator.validateUpdateRequest(staffId, receptionReq);
        ReceptionEntity reception = receptionRepository.findById(staffId)
                .orElseThrow(() -> new EntityNotFoundException("원무 정보를 찾을 수 없습니다. staffId=" + staffId));

        reception.setJobTypeCd(receptionReq.getJobTypeCd());
        reception.setDeskNo(receptionReq.getDeskNo());
        reception.setShiftType(normalizeShiftType(receptionReq.getShiftType()));
        reception.setStartDate(parseStartDate(receptionReq.getStartDate()));
        reception.setWindowArea(receptionReq.getWindowArea());
        reception.setMultiTask(normalizeMultiTask(receptionReq.getMultiTask()));
        reception.setRmk(receptionReq.getRmk());
        reception.setReceptionType(normalizeReceptionType(receptionReq.getReceptionType()));
        reception.setExtNo(receptionReq.getExtNo());
        receptionRepository.flush();
        return receptionQueryService.getReceptionDetail(staffId);
    }

    @Override
    public void deleteReception(String staffId) {
        if (!receptionRepository.existsById(staffId)) {
            throw new EntityNotFoundException("삭제할 원무 정보를 찾을 수 없습니다. staffId=" + staffId);
        }
        receptionRepository.deleteById(staffId);
    }

    private LocalDate parseStartDate(String startDate) {
        if (startDate == null || startDate.isBlank()) {
            return null;
        }
        return LocalDate.parse(startDate.trim().substring(0, 10));
    }

    private String normalizeShiftType(String shiftType) {
        return shiftType == null ? null : shiftType.trim().toUpperCase();
    }

    private String normalizeMultiTask(String multiTask) {
        if (multiTask == null || multiTask.isBlank()) {
            return null;
        }
        String normalized = multiTask.trim().toUpperCase();
        if ("Y".equals(normalized) || "가능".equals(multiTask.trim())) {
            return "가능";
        }
        if ("N".equals(normalized) || "불가".equals(multiTask.trim())) {
            return "불가";
        }
        return multiTask.trim();
    }

    private String normalizeReceptionType(String receptionType) {
        if (receptionType == null || receptionType.isBlank()) {
            return StaffRoleType.RECEPTION.name();
        }
        return receptionType.trim().toUpperCase();
    }
}
