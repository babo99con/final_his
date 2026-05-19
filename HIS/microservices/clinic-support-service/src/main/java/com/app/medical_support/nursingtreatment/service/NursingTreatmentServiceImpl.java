package com.app.medical_support.nursingtreatment.service;

import com.app.medical_support.integration.outbound.kafka.DownstreamOutcomeEventPublisher;
import com.app.medical_support.common.integration.claims.service.ClaimsCompletionStageService;
import com.app.medical_support.common.integration.reception.dto.OutpatientReceptionDTO;
import com.app.medical_support.common.integration.reception.service.ReceptionIntegrationService;
import com.app.medical_support.common.sequence.SequenceIdService;
import com.app.medical_support.common.sequence.SequenceIdType;
import com.app.medical_support.nursingtreatment.dto.*;
import com.app.medical_support.nursingtreatment.entity.MedicationRecordEntity;
import com.app.medical_support.nursingtreatment.entity.RecordEntity;
import com.app.medical_support.nursingtreatment.entity.TreatmentResultEntity;
import com.app.medical_support.nursingtreatment.exception.*;
import com.app.medical_support.nursingtreatment.mapper.MedicationRecordMapper;
import com.app.medical_support.nursingtreatment.mapper.RecordMapper;
import com.app.medical_support.nursingtreatment.mapper.TreatmentResultMapper;
import com.app.medical_support.nursingtreatment.mapstruct.RecordReqMapStruct;
import com.app.medical_support.nursingtreatment.mapstruct.RecordResMapStruct;
import com.app.medical_support.nursingtreatment.repository.MedicationRecordRepository;
import com.app.medical_support.nursingtreatment.repository.RecordRepository;
import com.app.medical_support.nursingtreatment.repository.TreatmentResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NursingTreatmentServiceImpl implements NursingTreatmentService {
    private static final DateTimeFormatter CHAR_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final RecordRepository recordRepository;
    private final RecordReqMapStruct recordReqMapStruct;
    private final RecordResMapStruct recordResMapStruct;
    private final RecordMapper recordMapper;
    private final MedicationRecordMapper medicationRecordMapper;
    private final TreatmentResultMapper treatmentResultMapper;
    private final MedicationRecordRepository medicationRecordRepository;
    private final TreatmentResultRepository treatmentResultRepository;
    private final ReceptionIntegrationService receptionIntegrationService;
    private final SequenceIdService sequenceIdService;
    private final DownstreamOutcomeEventPublisher downstreamOutcomeEventPublisher;
    private final ClaimsCompletionStageService claimsCompletionStageService;

    @Override
    public List<RecordResponseDTO> search(String searchType, String searchValue, String startDate, String endDate) {
        if (!"recordId".equals(searchType)
                && !"receptionId".equals(searchType)
                && !"nurseName".equals(searchType)
                && !"patientName".equals(searchType)
                && !"departmentName".equals(searchType)
                && !"createdAt".equals(searchType)) {
            throw new RecordSearchValidationException("지원하지 않는 검색 타입입니다: " + searchType);
        }
        return recordMapper.search(searchType, searchValue, startDate, endDate);


    }

    @Override
    public List<RecordResponseDTO> findRecordList() {
        return recordMapper.findRecordList();
    }

    @Override
    public RecordResponseDTO findRecordDetail(String id) {
        RecordResponseDTO recordResponseDTO = recordMapper.findRecordDetail(id);
        if (recordResponseDTO == null) {
            throw new RecordNotFoundException(id);
        }
        return recordResponseDTO;
    }

    @Override
    @Transactional
    public RecordDTO registerRecord(RecordCreateReqDTO recordRequestDTO) {
        validateReceptionRecordRequest(recordRequestDTO);

        RecordEntity entity = recordReqMapStruct.toEntity(recordRequestDTO);
        LocalDateTime now = LocalDateTime.now();
        entity.setRecordId(sequenceIdService.nextId(SequenceIdType.RECORD_ID));
        entity.setStatus("ACTIVE");
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        return recordResMapStruct.toDTO(recordRepository.save(entity));
    }

    @Override
    @Transactional
    public RecordDTO modifyRecord(String id, RecordUpdateDTO recordDTO) {
        RecordEntity saved = recordRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id));


        saved.setSystolicBp(recordDTO.getSystolicBp());
        saved.setDiastolicBp(recordDTO.getDiastolicBp());
        saved.setPulse(recordDTO.getPulse());
        saved.setRespiration(recordDTO.getRespiration());
        saved.setTemperature(recordDTO.getTemperature());
        saved.setSpo2(recordDTO.getSpo2());
        saved.setObservation(recordDTO.getObservation());
        saved.setPainScore(recordDTO.getPainScore());
        saved.setConsciousnessLevel(recordDTO.getConsciousnessLevel());
        saved.setInitialAssessment(recordDTO.getInitialAssessment());
        saved.setPastMedicalHistory(recordDTO.getPastMedicalHistory());
        saved.setStatus(normalizeStatus(recordDTO.getStatus()));
        saved.setReceptionId(recordDTO.getReceptionId());
        saved.setNursingId(recordDTO.getNursingId());
        saved.setNurseName(recordDTO.getNurseName());
        saved.setHeightCm(recordDTO.getHeightCm());
        saved.setWeightKg(recordDTO.getWeightKg());
        saved.setUpdatedAt(LocalDateTime.now());

        return recordResMapStruct.toDTO(recordRepository.save(saved));
    }

    @Override
    @Transactional
    public RecordDTO updateRecordStatus(String id, String status) {
        RecordEntity entity = recordRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(id));

        entity.setStatus(normalizeStatus(status));
        entity.setUpdatedAt(LocalDateTime.now());
        return recordResMapStruct.toDTO(recordRepository.save(entity));
    }

    @Override
    public List<MedicationRecordDTO> findMedicationRecordList() {
        return medicationRecordRepository.findAll().stream().map(this::toMedicationRecordDTO).toList();
    }

    @Override
    public List<MedicationRecordDTO> searchMedicationRecord(String patientName, String departmentName, String progressStatus, String startDate, String endDate) {
        return medicationRecordMapper.searchMedicationRecord(patientName, departmentName, progressStatus, startDate, endDate);
    }

    @Override
    public MedicationRecordDTO findMedicationRecordDetail(String id) {
        return toMedicationRecordDTO(medicationRecordRepository.findById(id)
                .orElseThrow(() -> new MedicationRecordNotFoundException(id)));
    }

    @Override
    @Transactional
    public MedicationRecordDTO registerMedicationRecord(MedicationRecordReqDTO medicationRecordDTO) {
        MedicationRecordEntity entity = new MedicationRecordEntity();
        entity.setMedicationRecordId(sequenceIdService.nextId(SequenceIdType.MEDICATION_RECORD_ID));

        entity.setMedicationId(medicationRecordDTO.getMedicationId());
        entity.setDoseNumber(medicationRecordDTO.getDoseNumber());
        entity.setDoseUnit(medicationRecordDTO.getDoseUnit());
        entity.setDoseKind(medicationRecordDTO.getDoseKind());
        entity.setStatus(normalizeStatus(medicationRecordDTO.getStatus()));
        entity.setProgressStatus(normalizeProgressStatus(medicationRecordDTO.getProgressStatus()));
        entity.setCreatedAt(LocalDateTime.now().format(CHAR_DATE_TIME_FORMATTER));
        entity.setPatientId(medicationRecordDTO.getPatientId());
        entity.setPatientName(medicationRecordDTO.getPatientName());
        entity.setDepartmentName(medicationRecordDTO.getDepartmentName());
        MedicationRecordDTO saved = toMedicationRecordDTO(medicationRecordRepository.save(entity));
        stageMedicationIfCompleted(null, saved);
        return saved;
    }

    @Override
    @Transactional
    public MedicationRecordDTO modifyMedicationRecord(String id, MedicationRecordUpdateDTO medicationRecordDTO) {
        MedicationRecordEntity entity = medicationRecordRepository.findById(id)
                .orElseThrow(() -> new MedicationRecordNotFoundException(id));
        String beforeProgressStatus = entity.getProgressStatus();
        if (medicationRecordDTO.getAdministeredAt() != null) {
            entity.setAdministeredAt(medicationRecordDTO.getAdministeredAt());
        }
        if (medicationRecordDTO.getDoseNumber() != null) {
            entity.setDoseNumber(medicationRecordDTO.getDoseNumber());
        }
        if (medicationRecordDTO.getDoseUnit() != null) {
            entity.setDoseUnit(medicationRecordDTO.getDoseUnit());
        }
        if (medicationRecordDTO.getDoseKind() != null) {
            entity.setDoseKind(medicationRecordDTO.getDoseKind());
        }
        if (medicationRecordDTO.getNursingId() != null) {
            entity.setNursingId(medicationRecordDTO.getNursingId());
        }
        if (medicationRecordDTO.getNurseName() != null) {
            entity.setNurseName(medicationRecordDTO.getNurseName());
        }
        if (hasText(medicationRecordDTO.getStatus())) {
            entity.setStatus(normalizeStatus(medicationRecordDTO.getStatus()));
        }
        entity.setProgressStatus(resolveProgressStatus(medicationRecordDTO.getProgressStatus(), entity.getProgressStatus()));
        if (medicationRecordDTO.getPatientId() != null) {
            entity.setPatientId(medicationRecordDTO.getPatientId());
        }
        if (medicationRecordDTO.getPatientName() != null) {
            entity.setPatientName(medicationRecordDTO.getPatientName());
        }
        if (medicationRecordDTO.getDepartmentName() != null) {
            entity.setDepartmentName(medicationRecordDTO.getDepartmentName());
        }
        MedicationRecordDTO saved = toMedicationRecordDTO(medicationRecordRepository.save(entity));
        stageMedicationIfCompleted(beforeProgressStatus, saved);
        return saved;
    }

    @Override
    @Transactional
    public MedicationRecordDTO updateMedicationRecordStatus(String id, String status) {
        MedicationRecordEntity entity = medicationRecordRepository.findById(id)
                .orElseThrow(() -> new MedicationRecordNotFoundException(id));
        entity.setStatus(normalizeStatus(status));
        MedicationRecordDTO saved = toMedicationRecordDTO(medicationRecordRepository.save(entity));
        return saved;
    }

    @Override
    public List<TreatmentResultDTO> findTreatmentResultList() {
        return treatmentResultRepository.findAll().stream().map(this::toTreatmentResultDTO).toList();
    }

    @Override
    public List<TreatmentResultDTO> searchTreatmentResult(String patientName, String departmentName, String progressStatus, String startDate, String endDate) {
        return treatmentResultMapper.searchTreatmentResult(patientName, departmentName, progressStatus, startDate, endDate);
    }

    @Override
    public TreatmentResultDTO findTreatmentResultDetail(String id) {
        return toTreatmentResultDTO(treatmentResultRepository.findById(id)
                .orElseThrow(() -> new TreatmentResultNotFoundException(id)));
    }

    @Override
    @Transactional
    public TreatmentResultDTO registerTreatmentResult(TreatmentResultCreateDTO treatmentResultDTO) {
        TreatmentResultEntity entity = new TreatmentResultEntity();
        entity.setTreatmentResultId(sequenceIdService.nextId(SequenceIdType.TREATMENT_RESULT_ID));
        entity.setProcedureResultId(treatmentResultDTO.getProcedureResultId());
        entity.setDetail(treatmentResultDTO.getDetail());
        entity.setStatus(normalizeStatus(treatmentResultDTO.getStatus()));
        entity.setProgressStatus(normalizeProgressStatus(treatmentResultDTO.getProgressStatus()));
        entity.setPatientId(treatmentResultDTO.getPatientId());
        entity.setPatientName(treatmentResultDTO.getPatientName());
        entity.setDepartmentName(treatmentResultDTO.getDepartmentName());
        TreatmentResultDTO saved = toTreatmentResultDTO(treatmentResultRepository.save(entity));
        stageTreatmentIfCompleted(null, saved);
        return saved;
    }

    @Override
    @Transactional
    public TreatmentResultDTO modifyTreatmentResult(String id, TreatmentResultUpdateDTO treatmentResultDTO) {
        TreatmentResultEntity entity = treatmentResultRepository.findById(id)
                .orElseThrow(() -> new TreatmentResultNotFoundException(id));
        String beforeProgressStatus = entity.getProgressStatus();
        if (treatmentResultDTO.getProcedureResultId() != null) {
            entity.setProcedureResultId(treatmentResultDTO.getProcedureResultId());
        }
        if (treatmentResultDTO.getNursingId() != null) {
            entity.setNursingId(treatmentResultDTO.getNursingId());
        }
        if (treatmentResultDTO.getNurseName() != null) {
            entity.setNurseName(treatmentResultDTO.getNurseName());
        }
        if (treatmentResultDTO.getDetail() != null) {
            entity.setDetail(treatmentResultDTO.getDetail());
        }
        if (hasText(treatmentResultDTO.getStatus())) {
            entity.setStatus(normalizeStatus(treatmentResultDTO.getStatus()));
        }
        entity.setProgressStatus(resolveProgressStatus(treatmentResultDTO.getProgressStatus(), entity.getProgressStatus()));
        if (treatmentResultDTO.getTreatmentAt() != null) {
            entity.setTreatmentAt(treatmentResultDTO.getTreatmentAt());
        }
        if (treatmentResultDTO.getPatientId() != null) {
            entity.setPatientId(treatmentResultDTO.getPatientId());
        }
        if (treatmentResultDTO.getPatientName() != null) {
            entity.setPatientName(treatmentResultDTO.getPatientName());
        }
        if (treatmentResultDTO.getDepartmentName() != null) {
            entity.setDepartmentName(treatmentResultDTO.getDepartmentName());
        }
        TreatmentResultDTO saved = toTreatmentResultDTO(treatmentResultRepository.save(entity));
        stageTreatmentIfCompleted(beforeProgressStatus, saved);
        return saved;
    }

    @Override
    @Transactional
    public TreatmentResultDTO updateTreatmentResultStatus(String id, String status) {
        TreatmentResultEntity entity = treatmentResultRepository.findById(id)
                .orElseThrow(() -> new TreatmentResultNotFoundException(id));
        entity.setStatus(normalizeStatus(status));
        TreatmentResultDTO saved = toTreatmentResultDTO(treatmentResultRepository.save(entity));
        return saved;
    }

    private MedicationRecordDTO toMedicationRecordDTO(MedicationRecordEntity entity) {
        MedicationRecordDTO dto = new MedicationRecordDTO();
        dto.setMedicationRecordId(entity.getMedicationRecordId());
        dto.setMedicationId(entity.getMedicationId());
        dto.setAdministeredAt(entity.getAdministeredAt());
        dto.setDoseNumber(entity.getDoseNumber());
        dto.setDoseUnit(entity.getDoseUnit());
        dto.setDoseKind(entity.getDoseKind());
        dto.setNursingId(entity.getNursingId());
        dto.setNurseName(entity.getNurseName());
        dto.setStatus(entity.getStatus());
        dto.setProgressStatus(entity.getProgressStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setPatientId(entity.getPatientId());
        dto.setPatientName(entity.getPatientName());
        dto.setDepartmentName(entity.getDepartmentName());
        return dto;
    }

    private TreatmentResultDTO toTreatmentResultDTO(TreatmentResultEntity entity) {
        TreatmentResultDTO dto = new TreatmentResultDTO();
        dto.setTreatmentResultId(entity.getTreatmentResultId());
        dto.setProcedureResultId(entity.getProcedureResultId());
        dto.setStatus(entity.getStatus());
        dto.setProgressStatus(entity.getProgressStatus());
        dto.setTreatmentAt(entity.getTreatmentAt());
        dto.setNursingId(entity.getNursingId());
        dto.setNurseName(entity.getNurseName());
        dto.setDetail(entity.getDetail());
        dto.setPatientId(entity.getPatientId());
        dto.setPatientName(entity.getPatientName());
        dto.setDepartmentName(entity.getDepartmentName());
        return dto;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private String normalizeStatus(String status) {
        if (!hasText(status)) {
            return "ACTIVE";
        }

        String trimmed = status.trim().toUpperCase();
        if ("Y".equals(trimmed)) {
            return "ACTIVE";
        }
        if ("N".equals(trimmed)) {
            return "INACTIVE";
        }

        return trimmed;
    }

    private String normalizeProgressStatus(String progressStatus) {
        if (!hasText(progressStatus)) {
            return "REQUESTED";
        }

        return progressStatus.trim().toUpperCase();
    }

    private String resolveProgressStatus(String newValue, String currentValue) {
        if (!hasText(newValue)) {
            return hasText(currentValue) ? normalizeProgressStatus(currentValue) : "REQUESTED";
        }

        return normalizeProgressStatus(newValue);
    }

    private void validateReceptionRecordRequest(RecordCreateReqDTO recordRequestDTO) {
        Long receptionId = recordRequestDTO.getReceptionId();
        if (receptionId == null) {
            throw new RecordReceptionValidationException("접수 정보 검증 실패: receptionId는 필수입니다.");
        }

        String requestPatientName = trimToNull(recordRequestDTO.getPatientName());
        String requestDepartmentName = trimToNull(recordRequestDTO.getDepartmentName());
        List<String> validationErrors = new ArrayList<>();

        if (requestPatientName == null) {
            validationErrors.add("환자명 값이 없습니다");
        }
        if (requestDepartmentName == null) {
            validationErrors.add("진료과 값이 없습니다");
        }
        if (!validationErrors.isEmpty()) {
            throw new RecordReceptionValidationException("접수 정보 검증 실패: " + String.join(", ", validationErrors));
        }

        OutpatientReceptionDTO receptionDetail;
        try {
            receptionDetail = receptionIntegrationService.findDetail(receptionId);
        } catch (ResponseStatusException ex) {
            if (HttpStatus.NOT_FOUND.equals(ex.getStatus())) {
                throw new RecordReceptionValidationException("접수 정보 검증 실패: 유효하지 않은 receptionId입니다. receptionId=" + receptionId);
            }
            throw new RecordReceptionLookupException(
                    ex.getStatus(),
                    firstNonBlank(ex.getReason(), "접수 상세 조회에 실패했습니다.")
            );
        }

        String actualPatientName = trimToNull(receptionDetail.getPatientName());
        String actualDepartmentName = trimToNull(receptionDetail.getDepartmentName());
        List<String> mismatchErrors = new ArrayList<>();

        if (!requestPatientName.equals(actualPatientName)) {
            mismatchErrors.add("환자명 불일치");
        }
        if (!requestDepartmentName.equals(actualDepartmentName)) {
            mismatchErrors.add("진료과 불일치");
        }

        if (!mismatchErrors.isEmpty()) {
            throw new RecordReceptionValidationException("접수 정보 검증 실패: " + String.join(", ", mismatchErrors));
        }
    }

    private void stageMedicationIfCompleted(String beforeProgressStatus, MedicationRecordDTO saved) {
        if (saved == null || !isCompleted(saved.getProgressStatus()) || isCompleted(beforeProgressStatus)) {
            return;
        }
        claimsCompletionStageService.stageMedicationCompleted(
                saved.getPatientId(),
                saved.getMedicationRecordId(),
                saved.getMedicationId()
        );
        downstreamOutcomeEventPublisher.publishMedicationRecordOutcome(saved);
    }

    private void stageTreatmentIfCompleted(String beforeProgressStatus, TreatmentResultDTO saved) {
        if (saved == null || !isCompleted(saved.getProgressStatus()) || isCompleted(beforeProgressStatus)) {
            return;
        }
        claimsCompletionStageService.stageTreatmentCompleted(
                saved.getPatientId(),
                saved.getTreatmentResultId(),
                saved.getDetail()
        );
        downstreamOutcomeEventPublisher.publishTreatmentResultOutcome(saved);
    }

    private boolean isCompleted(String progressStatus) {
        return "COMPLETED".equalsIgnoreCase(trimToNull(progressStatus));
    }

    private String trimToNull(String value) {
        if (!hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private String firstNonBlank(String first, String second) {
        String firstValue = trimToNull(first);
        if (firstValue != null) {
            return firstValue;
        }
        return trimToNull(second);
    }

}
