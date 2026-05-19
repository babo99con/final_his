package kr.co.hospital.patients.patient.service;

import kr.co.hospital.patients.code.service.CodeValidationService;
import kr.co.hospital.patients.patient.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.hospital.patients.patient.entity.*;
import kr.co.hospital.patients.patient.exception.*;
import kr.co.hospital.patients.patient.mapper.*;
import kr.co.hospital.patients.patient.mapstruct.*;
import kr.co.hospital.patients.patient.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.CallableStatement;
import java.sql.Types;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PatientServiceImpl implements PatientService {
    private static final String GROUP_PATIENT_STATUS = "PATIENT_STATUS";
    private static final String GROUP_PATIENT_FLAG = "PATIENT_FLAG";
    private static final String GROUP_PATIENT_RESTRICTION = "PATIENT_RESTRICTION";

    private final PatientRepository patientRepository;
    private final FamilyRepository familyRepository;
    private final StatusHistoryRepository statusHistoryRepository;
    private final InfoHistoryRepository infoHistoryRepository;
    private final FlagRepository flagRepository;
    private final MemoRepository memoRepository;
    private final RestrictionRepository restrictionRepository;
    private final ObjectMapper objectMapper;
    private final PatientMapper patientMapper;
    private final FlagMapper flagMapper;
    private final MemoMapper memoMapper;
    private final RestrictionMapper restrictionMapper;
    private final StatusHistoryMapper statusHistoryMapper;
    private final PatientReqMapStruct patientReqMapStruct;
    private final PatientResMapStruct patientResMapStruct;
    private final FlagReqMapStruct flagReqMapStruct;
    private final FlagResMapStruct flagResMapStruct;
    private final MemoReqMapStruct memoReqMapStruct;
    private final MemoResMapStruct memoResMapStruct;
    private final RestrictionReqMapStruct restrictionReqMapStruct;
    private final RestrictionResMapStruct restrictionResMapStruct;
    private final StatusHistoryReqMapStruct statusHistoryReqMapStruct;
    private final StatusHistoryResMapStruct statusHistoryResMapStruct;
    private final CodeValidationService codeValidationService;
    private final JdbcTemplate jdbcTemplate;
    private static final Pattern NON_DIGIT = Pattern.compile("[^0-9]");
    private static final String PATIENT_NO_CALL_SQL =
            "{call SP_NEXT_NO(?, ?)}";

    @Override
    public List<PatientResDTO> findList() {
        log.info("Patient list");
        List<PatientEntity> entities = patientRepository.findAllByStatusCodeNot("INACTIVE");
        return patientResMapStruct.toDTOList(entities);
    }

    @Override
    @Cacheable(value = "PATIENT", key = "#id")
    public PatientResDTO findDetail(Long id) {
        log.info("Patient detail id={}", id);

        PatientEntity entity = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));

        PatientResDTO dto = patientResMapStruct.toDTO(entity);

        List<FamilyResDTO> families = findByPatientId(id);
        if (!families.isEmpty()) {
            FamilyResDTO primary = families.stream()
                    .filter(f -> Boolean.TRUE.equals(f.getIsPrimary()))
                    .findFirst()
                    .orElse(families.get(0));
            dto.setGuardianName(primary.getFamilyName());
            dto.setGuardianPhone(primary.getFamilyPhone());
            dto.setGuardianRelation(primary.getRelation());
        }

        return dto;
    }

    @Override
    @Transactional
    public PatientResDTO register(CreateReqDTO createreqDTO, MultipartFile file) {
        log.info("Register patient: {}", createreqDTO);

        PatientEntity entity = patientReqMapStruct.toEntity(createreqDTO);

        if (entity.getPatientNo() == null || entity.getPatientNo().isBlank()) {
            entity.setPatientNo(genPatientNo());
        }

        entity.setStatusCode("ACTIVE");
        entity.setIsVip(Boolean.TRUE.equals(createreqDTO.getIsVip()));

        if (entity.getIsForeigner() == null) {
            entity.setIsForeigner(Boolean.FALSE);
        }

        PatientEntity saved = patientRepository.save(entity);
        logInfoHistory("CREATE", null, saved, "system");
        logStatusHistory(saved.getPatientId(), "NEW", "ACTIVE", "Created", null);

        if (createreqDTO.getFamilies() != null && !createreqDTO.getFamilies().isEmpty()) {
            createForPatient(saved.getPatientId(), createreqDTO.getFamilies());
        }

        return patientResMapStruct.toDTO(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "PATIENT", key = "#id")
    public PatientResDTO modify(Long id, UpdateReqDTO updatereqDTO) {
        log.info("Modify patient id={}", id);

        PatientEntity saved = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));

        PatientEntity before = snapshot(saved);

        saved.setName(updatereqDTO.getName());
        saved.setEmail(updatereqDTO.getEmail());
        saved.setPhone(updatereqDTO.getPhone());
        saved.setGender(updatereqDTO.getGender());
        saved.setBirthDate(updatereqDTO.getBirthDate());
        saved.setRrn(updatereqDTO.getRrn());
        saved.setAddress(updatereqDTO.getAddress());
        saved.setAddressDetail(updatereqDTO.getAddressDetail());

        saved.setIsForeigner(updatereqDTO.getIsForeigner());
        saved.setNote(updatereqDTO.getNote());

        logInfoHistory("UPDATE", before, saved, "system");
        return patientResMapStruct.toDTO(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "PATIENT", key = "#id")
    public void remove(Long id) {
        log.info("Deactivate patient id={}", id);

        PatientEntity entity = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        String before = entity.getStatusCode();
        entity.setStatusCode("INACTIVE");
        if (!"INACTIVE".equals(before)) {
            logStatusHistory(entity.getPatientId(), before, "INACTIVE", "Deactivated", null);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "PATIENT", key = "#id")
    public PatientResDTO changeStatus(Long id, StatusChangeReqDTO statusChangeReqDTO) {
        log.info("Change status id={}", id);

        PatientEntity entity = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));

        String before = entity.getStatusCode();
        String after = statusChangeReqDTO.getStatusCode();
        codeValidationService.validateActiveCode(GROUP_PATIENT_STATUS, after, "statusCode");

        entity.setStatusCode(after);
        if (!after.equals(before)) {
            logStatusHistory(
                    entity.getPatientId(),
                    before,
                    after,
                    statusChangeReqDTO.getReason(),
                    statusChangeReqDTO.getChangedBy()
            );
        }

        return patientResMapStruct.toDTO(entity);
    }

    @Override
    public List<PatientResDTO> search(String type, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("keyword is required");
        }

        List<PatientEntity> entities = patientMapper.search(type, keyword.trim());
        return patientResMapStruct.toDTOList(entities);
    }
    @Override
    public List<PatientResDTO> searchMulti(String name, String birthDate, String phone) {
        if ((name == null || name.isBlank())
                && (birthDate == null || birthDate.isBlank())
                && (phone == null || phone.isBlank())) {
            throw new IllegalArgumentException("At least one search field is required");
        }
        List<PatientEntity> entities = patientMapper.searchMulti(
                name == null ? null : name.trim(),
                birthDate == null ? null : birthDate.trim(),
                phone == null ? null : phone.trim()
        );
        return patientResMapStruct.toDTOList(entities);
    }

    @Override
    public List<PatientIdentifyResDTO> identify(PatientIdentifyReqDTO reqDTO) {
        if (reqDTO == null) {
            throw new IllegalArgumentException("request is required");
        }

        String name = trimToNull(reqDTO.getName());
        String birthDate = trimToNull(reqDTO.getBirthDate());
        String phoneRaw = trimToNull(reqDTO.getPhone());
        String phoneDigits = normalizePhone(phoneRaw);

        if (name == null && birthDate == null && phoneDigits == null) {
            throw new IllegalArgumentException("At least one identify field is required");
        }

        Map<Long, String> matchLevelById = new LinkedHashMap<>();
        Map<Long, PatientResDTO> patientById = new LinkedHashMap<>();

        if (name != null && birthDate != null && phoneDigits != null) {
            addIdentifyResults(
                    matchLevelById,
                    patientById,
                    patientMapper.identifyStrong(name, birthDate, phoneDigits),
                    "STRONG"
            );
        }
        if (name != null && birthDate != null) {
            addIdentifyResults(
                    matchLevelById,
                    patientById,
                    patientMapper.identifyNameBirth(name, birthDate),
                    "MEDIUM"
            );
        }
        if (name != null && phoneDigits != null) {
            addIdentifyResults(
                    matchLevelById,
                    patientById,
                    patientMapper.identifyNamePhone(name, phoneDigits),
                    "MEDIUM"
            );
        }
        if (birthDate != null && phoneDigits != null) {
            addIdentifyResults(
                    matchLevelById,
                    patientById,
                    patientMapper.identifyBirthPhone(birthDate, phoneDigits),
                    "WEAK"
            );
        }
        if (name != null && birthDate == null && phoneDigits == null) {
            addIdentifyResults(
                    matchLevelById,
                    patientById,
                    patientMapper.search("name", name),
                    "WEAK"
            );
        }
        if (phoneRaw != null && name == null && birthDate == null) {
            addIdentifyResults(
                    matchLevelById,
                    patientById,
                    patientMapper.search("phone", phoneRaw),
                    "WEAK"
            );
        }
        if (birthDate != null && name == null && phoneDigits == null) {
            addIdentifyResults(
                    matchLevelById,
                    patientById,
                    patientMapper.search("birthDate", birthDate),
                    "WEAK"
            );
        }

        List<PatientIdentifyResDTO> result = new ArrayList<>();
        for (Map.Entry<Long, PatientResDTO> entry : patientById.entrySet()) {
            String level = matchLevelById.get(entry.getKey());
            result.add(new PatientIdentifyResDTO(level, entry.getValue()));
        }
        return result;
    }

    private PatientEntity snapshot(PatientEntity src) {
        PatientEntity copy = new PatientEntity();
        copy.setPatientId(src.getPatientId());
        copy.setPatientNo(src.getPatientNo());
        copy.setName(src.getName());
        copy.setGender(src.getGender());
        copy.setBirthDate(src.getBirthDate());
        copy.setRrn(src.getRrn());
        copy.setPhone(src.getPhone());
        copy.setEmail(src.getEmail());
        copy.setAddress(src.getAddress());
        copy.setAddressDetail(src.getAddressDetail());
        copy.setStatusCode(src.getStatusCode());
        copy.setIsVip(src.getIsVip());
        copy.setCreatedAt(src.getCreatedAt());
        copy.setUpdatedAt(src.getUpdatedAt());
        copy.setIsForeigner(src.getIsForeigner());
        copy.setNote(src.getNote());
        return copy;
    }

    private void logInfoHistory(String changeType, PatientEntity before, PatientEntity after, String changedBy) {
        PatientEntity base = after != null ? after : before;
        if (base == null) {
            return;
        }
        InfoHistoryEntity history = new InfoHistoryEntity();
        history.setPatientId(base.getPatientId());
        history.setChangeType(changeType);
        history.setBeforeData(toJson(before));
        history.setAfterData(toJson(after));
        history.setChangedBy(changedBy);
        infoHistoryRepository.save(history);
    }

    private String toJson(PatientEntity entity) {
        if (entity == null) return null;
        try {
            return objectMapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            log.warn("Patient info history JSON serialize failed", e);
            return null;
        }
    }
    private void logStatusHistory(
            Long patientId,
            String fromStatus,
            String toStatus,
            String reason,
            String changedBy
    ) {
        StatusHistoryEntity history = new StatusHistoryEntity();
        history.setPatientId(patientId);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setReason(reason);
        history.setChangedBy(changedBy);
        statusHistoryRepository.save(history);
    }

    private String genPatientNo() {
        String patientNo = jdbcTemplate.execute(
                PATIENT_NO_CALL_SQL,
                (CallableStatementCallback<String>) cs -> {
                    cs.setString(1, "PATIENT_NO");
                    cs.registerOutParameter(2, Types.VARCHAR);
                    cs.execute();
                    return cs.getString(2);
                }
        );

        if (patientNo == null || patientNo.isBlank()) {
            throw new IllegalStateException("SP_NEXT_NO returned empty value");
        }

        return patientNo;
    }
    @Override
    @Transactional
    @CacheEvict(value = "PATIENT", key = "#id")
    public PatientResDTO changeVip(Long id, Boolean isVip) {
        PatientEntity entity = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException(id));
        entity.setIsVip(Boolean.TRUE.equals(isVip));
        return patientResMapStruct.toDTO(entity);
    }

    private void addIdentifyResults(
            Map<Long, String> matchLevelById,
            Map<Long, PatientResDTO> patientById,
            List<PatientEntity> entities,
            String level
    ) {
        for (PatientEntity entity : entities) {
            Long id = entity.getPatientId();
            if (matchLevelById.containsKey(id)) {
                continue;
            }
            matchLevelById.put(id, level);
            patientById.put(id, patientResMapStruct.toDTO(entity));
        }
    }

    
    @Override
    @Transactional
    public void deleteByPatientId(Long patientId) {
        familyRepository.findAllByPatientIdOrderBySortOrderAsc(patientId).forEach(familyRepository::delete);
    }
    @Override
    public List<FamilyResDTO> findByPatientId(Long patientId) {
        return familyRepository.findAllByPatientIdOrderBySortOrderAsc(patientId).stream().map(this::familyToDTO).toList();
    }
    @Override
    @Transactional
    public List<FamilyResDTO> createForPatient(Long patientId, List<FamilyCreateReqDTO> families) {
        if (families == null || families.isEmpty()) return List.of();
        List<FamilyResDTO> result = new java.util.ArrayList<>();
        int order = 0;
        for (FamilyCreateReqDTO dto : families) {
            FamilyEntity entity = new FamilyEntity();
            entity.setPatientId(patientId);
            entity.setRelation(dto.getRelation() != null ? dto.getRelation().trim() : "");
            entity.setFamilyName(dto.getFamilyName() != null ? dto.getFamilyName().trim() : "");
            entity.setFamilyPhone(dto.getFamilyPhone() != null && !dto.getFamilyPhone().isBlank() ? dto.getFamilyPhone().trim() : null);
            entity.setBirthDate(dto.getBirthDate());
            entity.setIsPrimary(Boolean.TRUE.equals(dto.getIsPrimary()));
            entity.setSortOrder(++order);
            result.add(familyToDTO(familyRepository.save(entity)));
        }
        return result;
    }
    private FamilyResDTO familyToDTO(FamilyEntity e) {
        return new FamilyResDTO(e.getFamilyId(), e.getPatientId(), e.getRelation(), e.getFamilyName(), e.getFamilyPhone(), e.getBirthDate(), e.getIsPrimary(), e.getSortOrder());
    }

    @Override
    public List<FlagResDTO> findFlagList() {
        return flagResMapStruct.toDTOList(flagRepository.findAll());
    }

    @Override
    @Cacheable(value = "PATIENT_FLAG", key = "#id")
    public FlagResDTO findFlagDetail(Long id) {
        FlagEntity entity = flagRepository.findById(id).orElseThrow(() -> new FlagNotFoundException(id));
        return flagResMapStruct.toDTO(entity);
    }

    @Override
    @Transactional
    public FlagResDTO registerFlag(FlagCreateReqDTO createReqDTO) {
        codeValidationService.validateActiveCode(GROUP_PATIENT_FLAG, createReqDTO.getFlagType(), "flagType");
        FlagEntity entity = flagReqMapStruct.toEntity(createReqDTO);
        if (entity.getActiveYn() == null) entity.setActiveYn(Boolean.TRUE);
        return flagResMapStruct.toDTO(flagRepository.save(entity));
    }

    @Override
    @Transactional
    @CacheEvict(value = "PATIENT_FLAG", key = "#id")
    public FlagResDTO modifyFlag(Long id, FlagUpdateReqDTO updateReqDTO) {
        FlagEntity saved = flagRepository.findById(id).orElseThrow(() -> new FlagNotFoundException(id));
        if (updateReqDTO.getFlagType() != null) {
            codeValidationService.validateActiveCode(GROUP_PATIENT_FLAG, updateReqDTO.getFlagType(), "flagType");
            saved.setFlagType(updateReqDTO.getFlagType());
        }
        if (updateReqDTO.getActiveYn() != null) saved.setActiveYn(updateReqDTO.getActiveYn());
        if (updateReqDTO.getNote() != null) saved.setNote(updateReqDTO.getNote());
        return flagResMapStruct.toDTO(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "PATIENT_FLAG", key = "#id")
    public void removeFlag(Long id) {
        if (!flagRepository.existsById(id)) throw new FlagNotFoundException(id);
        flagRepository.deleteById(id);
    }

    @Override
    public List<FlagResDTO> searchFlags(String type, String keyword) {
        if (keyword == null || keyword.isBlank()) throw new IllegalArgumentException("keyword is required");
        return flagResMapStruct.toDTOList(flagMapper.search(type, keyword.trim()));
    }

    @Override
    public List<MemoResDTO> findMemoList() {
        return memoResMapStruct.toDTOList(memoRepository.findAll());
    }

    @Override
    @Cacheable(value = "PATIENT_MEMO", key = "#id")
    public MemoResDTO findMemoDetail(Long id) {
        MemoEntity entity = memoRepository.findById(id).orElseThrow(() -> new MemoNotFoundException(id));
        return memoResMapStruct.toDTO(entity);
    }

    @Override
    @Transactional
    public MemoResDTO registerMemo(MemoCreateReqDTO createReqDTO) {
        MemoEntity entity = memoReqMapStruct.toEntity(createReqDTO);
        return memoResMapStruct.toDTO(memoRepository.save(entity));
    }

    @Override
    @Transactional
    @CacheEvict(value = "PATIENT_MEMO", key = "#id")
    public MemoResDTO modifyMemo(Long id, MemoUpdateReqDTO updateReqDTO) {
        MemoEntity saved = memoRepository.findById(id).orElseThrow(() -> new MemoNotFoundException(id));
        if (updateReqDTO.getMemo() != null) saved.setMemo(updateReqDTO.getMemo());
        return memoResMapStruct.toDTO(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "PATIENT_MEMO", key = "#id")
    public void removeMemo(Long id) {
        if (!memoRepository.existsById(id)) throw new MemoNotFoundException(id);
        memoRepository.deleteById(id);
    }

    @Override
    public List<MemoResDTO> searchMemos(String type, String keyword) {
        if (keyword == null || keyword.isBlank()) throw new IllegalArgumentException("keyword is required");
        return memoResMapStruct.toDTOList(memoMapper.search(type, keyword.trim()));
    }

    @Override
    public List<RestrictionResDTO> findRestrictionList() {
        return restrictionResMapStruct.toDTOList(restrictionRepository.findAll());
    }

    @Override
    @Cacheable(value = "PATIENT_RESTRICTION", key = "#id")
    public RestrictionResDTO findRestrictionDetail(Long id) {
        RestrictionEntity entity = restrictionRepository.findById(id).orElseThrow(() -> new RestrictionNotFoundException(id));
        return restrictionResMapStruct.toDTO(entity);
    }

    @Override
    @Transactional
    public RestrictionResDTO registerRestriction(RestrictionCreateReqDTO createReqDTO) {
        codeValidationService.validateActiveCode(GROUP_PATIENT_RESTRICTION, createReqDTO.getRestrictionType(), "restrictionType");
        RestrictionEntity entity = restrictionReqMapStruct.toEntity(createReqDTO);
        if (entity.getActiveYn() == null) entity.setActiveYn(Boolean.TRUE);
        return restrictionResMapStruct.toDTO(restrictionRepository.save(entity));
    }

    @Override
    @Transactional
    @CacheEvict(value = "PATIENT_RESTRICTION", key = "#id")
    public RestrictionResDTO modifyRestriction(Long id, RestrictionUpdateReqDTO updateReqDTO) {
        RestrictionEntity saved = restrictionRepository.findById(id).orElseThrow(() -> new RestrictionNotFoundException(id));
        if (updateReqDTO.getRestrictionType() != null) {
            codeValidationService.validateActiveCode(GROUP_PATIENT_RESTRICTION, updateReqDTO.getRestrictionType(), "restrictionType");
            saved.setRestrictionType(updateReqDTO.getRestrictionType());
        }
        if (updateReqDTO.getActiveYn() != null) saved.setActiveYn(updateReqDTO.getActiveYn());
        if (updateReqDTO.getReason() != null) saved.setReason(updateReqDTO.getReason());
        if (updateReqDTO.getEndAt() != null) saved.setEndAt(updateReqDTO.getEndAt());
        return restrictionResMapStruct.toDTO(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "PATIENT_RESTRICTION", key = "#id")
    public void removeRestriction(Long id) {
        if (!restrictionRepository.existsById(id)) throw new RestrictionNotFoundException(id);
        restrictionRepository.deleteById(id);
    }

    @Override
    public List<RestrictionResDTO> searchRestrictions(String type, String keyword) {
        if (keyword == null || keyword.isBlank()) throw new IllegalArgumentException("keyword is required");
        return restrictionResMapStruct.toDTOList(restrictionMapper.search(type, keyword.trim()));
    }

    @Override
    public List<StatusHistoryResDTO> findStatusHistoryList() {
        return statusHistoryResMapStruct.toDTOList(statusHistoryRepository.findAll());
    }

    @Override
    @Cacheable(value = "PATIENT_STATUS_HISTORY", key = "#id")
    public StatusHistoryResDTO findStatusHistoryDetail(Long id) {
        StatusHistoryEntity entity = statusHistoryRepository.findById(id).orElseThrow(() -> new StatusHistoryNotFoundException(id));
        return statusHistoryResMapStruct.toDTO(entity);
    }

    @Override
    @Transactional
    public StatusHistoryResDTO registerStatusHistory(StatusHistoryCreateReqDTO createReqDTO) {
        StatusHistoryEntity entity = statusHistoryReqMapStruct.toEntity(createReqDTO);
        return statusHistoryResMapStruct.toDTO(statusHistoryRepository.save(entity));
    }

    @Override
    @Transactional
    @CacheEvict(value = "PATIENT_STATUS_HISTORY", key = "#id")
    public StatusHistoryResDTO modifyStatusHistory(Long id, StatusHistoryUpdateReqDTO updateReqDTO) {
        StatusHistoryEntity saved = statusHistoryRepository.findById(id).orElseThrow(() -> new StatusHistoryNotFoundException(id));
        if (updateReqDTO.getFromStatus() != null) saved.setFromStatus(updateReqDTO.getFromStatus());
        if (updateReqDTO.getToStatus() != null) saved.setToStatus(updateReqDTO.getToStatus());
        if (updateReqDTO.getReason() != null) saved.setReason(updateReqDTO.getReason());
        if (updateReqDTO.getChangedBy() != null) saved.setChangedBy(updateReqDTO.getChangedBy());
        return statusHistoryResMapStruct.toDTO(saved);
    }

    @Override
    @Transactional
    @CacheEvict(value = "PATIENT_STATUS_HISTORY", key = "#id")
    public void removeStatusHistory(Long id) {
        if (!statusHistoryRepository.existsById(id)) throw new StatusHistoryNotFoundException(id);
        statusHistoryRepository.deleteById(id);
    }

    @Override
    public List<StatusHistoryResDTO> searchStatusHistories(String type, String keyword) {
        if (keyword == null || keyword.isBlank()) throw new IllegalArgumentException("keyword is required");
        return statusHistoryResMapStruct.toDTOList(statusHistoryMapper.search(type, keyword.trim()));
    }

    @Override
    public List<InfoHistoryResDTO> findInfoHistoryByPatientId(Long patientId) {
        return infoHistoryRepository.findByPatientIdOrderByChangedAtDesc(patientId).stream()
                .map(e -> new InfoHistoryResDTO(e.getHistoryId(), e.getPatientId(), e.getChangeType(), e.getBeforeData(), e.getAfterData(), e.getChangedBy(), e.getChangedAt()))
                .toList();
    }

    private String trimToNull(String value) {
        if (value == null) return null;
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizePhone(String value) {
        if (value == null) return null;
        String digits = NON_DIGIT.matcher(value).replaceAll("");
        return digits.isEmpty() ? null : digits;
    }
}
