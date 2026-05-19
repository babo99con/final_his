package com.app.medical_support.diagnosticexecution.service;

import com.app.medical_support.common.exception.InvalidRequestException;
import com.app.medical_support.common.integration.claims.service.ClaimsCompletionStageService;
import com.app.medical_support.integration.outbound.kafka.DownstreamOutcomeEventPublisher;
import com.app.medical_support.common.sequence.SequenceIdService;
import com.app.medical_support.common.sequence.SequenceIdType;
import com.app.medical_support.diagnosticexecution.dto.DiagnosticExamOutcomeDTO;
import com.app.medical_support.diagnosticexecution.dto.EndoscopyCreateReqDTO;
import com.app.medical_support.diagnosticexecution.dto.EndoscopyDTO;
import com.app.medical_support.diagnosticexecution.dto.EndoscopyExamSearchCondition;
import com.app.medical_support.diagnosticexecution.dto.ImagingCreateReqDTO;
import com.app.medical_support.diagnosticexecution.dto.ImagingDTO;
import com.app.medical_support.diagnosticexecution.dto.ImagingExamSearchCondition;
import com.app.medical_support.diagnosticexecution.dto.PathologyCreateReqDTO;
import com.app.medical_support.diagnosticexecution.dto.PathologyDTO;
import com.app.medical_support.diagnosticexecution.dto.PathologyExamSearchCondition;
import com.app.medical_support.diagnosticexecution.dto.PhysiologicalCreateReqDTO;
import com.app.medical_support.diagnosticexecution.dto.PhysiologicalDTO;
import com.app.medical_support.diagnosticexecution.dto.PhysiologicalExamSearchCondition;
import com.app.medical_support.diagnosticexecution.dto.SpecimenCreateReqDTO;
import com.app.medical_support.diagnosticexecution.dto.SpecimenDTO;
import com.app.medical_support.diagnosticexecution.dto.SpecimenExamSearchCondition;
import com.app.medical_support.diagnosticexecution.dto.TestExecutionDTO;
import com.app.medical_support.diagnosticexecution.dto.TestExecutionReqDTO;
import com.app.medical_support.diagnosticexecution.dto.TestExecutionUpdateDTO;
import com.app.medical_support.diagnosticexecution.entity.EndoscopyEntity;
import com.app.medical_support.diagnosticexecution.entity.ImagingEntity;
import com.app.medical_support.diagnosticexecution.entity.PathologyEntity;
import com.app.medical_support.diagnosticexecution.entity.PhysiologicalEntity;
import com.app.medical_support.diagnosticexecution.entity.SpecimenEntity;
import com.app.medical_support.diagnosticexecution.entity.TestExecutionEntity;
import com.app.medical_support.diagnosticexecution.exception.EndoscopyNotFoundException;
import com.app.medical_support.diagnosticexecution.exception.ImagingNotFoundException;
import com.app.medical_support.diagnosticexecution.exception.PathologyNotFoundException;
import com.app.medical_support.diagnosticexecution.exception.PhysiologicalNotFoundException;
import com.app.medical_support.diagnosticexecution.exception.SpecimenNotFoundException;
import com.app.medical_support.diagnosticexecution.exception.TestExecutionNotFoundExecution;
import com.app.medical_support.diagnosticexecution.mapstruct.EndoscopyReqMapStruct;
import com.app.medical_support.diagnosticexecution.mapstruct.EndoscopyResMapStruct;
import com.app.medical_support.diagnosticexecution.mapstruct.ImagingReqMapstruct;
import com.app.medical_support.diagnosticexecution.mapstruct.ImagingResMapstruct;
import com.app.medical_support.diagnosticexecution.mapstruct.PathologyReqMapStruct;
import com.app.medical_support.diagnosticexecution.mapstruct.PathologyResMapStruct;
import com.app.medical_support.diagnosticexecution.mapstruct.PhysiologicalReqMapStruct;
import com.app.medical_support.diagnosticexecution.mapstruct.PhysiologicalResMapStruct;
import com.app.medical_support.diagnosticexecution.mapstruct.SpecimenReqMapStruct;
import com.app.medical_support.diagnosticexecution.mapstruct.SpecimenResMapStruct;
import com.app.medical_support.diagnosticexecution.mapstruct.TestExecutionReqMapStruct;
import com.app.medical_support.diagnosticexecution.mapstruct.TestExecutionResMapStruct;
import com.app.medical_support.diagnosticexecution.repository.EndoscopyRepository;
import com.app.medical_support.diagnosticexecution.repository.ImagingRepository;
import com.app.medical_support.diagnosticexecution.repository.PathologyRepository;
import com.app.medical_support.diagnosticexecution.repository.PhysiologicalRepository;
import com.app.medical_support.diagnosticexecution.repository.SpecimenRepository;
import com.app.medical_support.diagnosticexecution.repository.TestExecutionRepository;
import com.app.medical_support.diagnosticresult.entity.EndoscopyResultEntity;
import com.app.medical_support.diagnosticresult.entity.ImagingResultEntity;
import com.app.medical_support.diagnosticresult.entity.PathologyResultEntity;
import com.app.medical_support.diagnosticresult.entity.PhysiologicalResultEntity;
import com.app.medical_support.diagnosticresult.entity.SpecimenTestResultEntity;
import com.app.medical_support.diagnosticresult.repository.EndoscopyResultRepository;
import com.app.medical_support.diagnosticresult.repository.ImagingResultRepository;
import com.app.medical_support.diagnosticresult.repository.PathologyResultRepository;
import com.app.medical_support.diagnosticresult.repository.PhysiologicalResultRepository;
import com.app.medical_support.diagnosticresult.repository.SpecimenTestResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiagnosticExecutionServiceImpl implements DiagnosticExecutionService {

    private final ImagingRepository imagingRepository;
    private final ImagingResultRepository imagingResultRepository;
    private final EndoscopyRepository endoscopyRepository;
    private final EndoscopyResultRepository endoscopyResultRepository;
    private final PathologyRepository pathologyRepository;
    private final PathologyResultRepository pathologyResultRepository;
    private final PhysiologicalRepository physiologicalRepository;
    private final PhysiologicalResultRepository physiologicalResultRepository;
    private final SpecimenRepository specimenRepository;
    private final SpecimenTestResultRepository specimenTestResultRepository;
    private final EndoscopyReqMapStruct endoscopyReqMapStruct;
    private final EndoscopyResMapStruct endoscopyResMapStruct;
    private final PathologyReqMapStruct pathologyReqMapStruct;
    private final PathologyResMapStruct pathologyResMapStruct;
    private final PhysiologicalReqMapStruct physiologicalReqMapStruct;
    private final PhysiologicalResMapStruct physiologicalResMapStruct;
    private final SpecimenReqMapStruct specimenReqMapStruct;
    private final SpecimenResMapStruct specimenResMapStruct;
    private final TestExecutionRepository testExecutionRepository;
    private final TestExecutionReqMapStruct testExecutionReqMapStruct;
    private final TestExecutionResMapStruct testExecutionResMapStruct;
    private final SequenceIdService sequenceIdService;
    private final ImagingReqMapstruct imagingReqMapstruct;
    private final ImagingResMapstruct imagingResMapstruct;
    private final DownstreamOutcomeEventPublisher downstreamOutcomeEventPublisher;
    private final ClaimsCompletionStageService claimsCompletionStageService;

    @Override
    public List<ImagingDTO> findImagingList(ImagingExamSearchCondition condition) {
        List<ImagingDTO> list = imagingResMapstruct.toDTOList(imagingRepository.findAll());
        return filterImagingList(list, condition);
    }

    @Override
    public ImagingDTO findImagingDetail(String id) {
        return imagingResMapstruct.toDTO(imagingRepository.findById(id)
                .orElseThrow(() -> new ImagingNotFoundException("Imaging exam not found. id=" + id)));
    }

    @Override
    @Transactional
    public ImagingDTO registerImaging(ImagingCreateReqDTO imagingDTO) {
        ImagingEntity entity = imagingReqMapstruct.toEntity(imagingDTO);
        entity.setImagingExamId(sequenceIdService.nextId(SequenceIdType.IMAGING_EXAM_ID));
        entity.setCreatedAt(LocalDateTime.now());
        return imagingResMapstruct.toDTO(imagingRepository.save(entity));
    }

    @Override
    @Transactional
    public ImagingDTO modifyImaging(String id, ImagingDTO imagingDTO) {
        validateBodyIdAbsent("imagingExamId", imagingDTO.getImagingExamId());
        ImagingEntity entity = imagingRepository.findById(id)
                .orElseThrow(() -> new ImagingNotFoundException("Imaging exam not found. id=" + id));
        String previousProgressStatus = entity.getProgressStatus();
        if (imagingDTO.getTestExecutionId() != null) {
            entity.setTestExecutionId(normalizeOptionalValue(imagingDTO.getTestExecutionId()));
        }
        if (imagingDTO.getImagingType() != null) {
            entity.setImagingType(imagingDTO.getImagingType());
        }
        if (imagingDTO.getDetailCode() != null) {
            entity.setDetailCode(imagingDTO.getDetailCode());
        }
        if (imagingDTO.getPatientId() != null) {
            entity.setPatientId(imagingDTO.getPatientId());
        }
        if (imagingDTO.getPatientName() != null) {
            entity.setPatientName(imagingDTO.getPatientName());
        }
        if (imagingDTO.getDepartmentName() != null) {
            entity.setDepartmentName(imagingDTO.getDepartmentName());
        }
        if (imagingDTO.getPerformerId() != null) {
            entity.setPerformerId(normalizeOptionalValue(imagingDTO.getPerformerId()));
        }
        if (imagingDTO.getPerformerName() != null) {
            entity.setPerformerName(normalizeOptionalValue(imagingDTO.getPerformerName()));
        }
        entity.setStatus(hasText(imagingDTO.getStatus())
                ? normalizeStatus(imagingDTO.getStatus())
                : normalizeStatus(entity.getStatus()));
        entity.setProgressStatus(resolveProgressStatus(imagingDTO.getProgressStatus(), entity.getProgressStatus()));

        entity.setUpdatedAt(LocalDateTime.now());
        ImagingEntity savedEntity = imagingRepository.save(entity);

        if (!isCompleted(previousProgressStatus) && isCompleted(savedEntity.getProgressStatus())) {
            markTestExecutionCompleted(savedEntity.getTestExecutionId());
            ensureImagingResultExists(savedEntity);
            stageDiagnosticCompletionFromExam("IMAGING", savedEntity.getPatientId(),
                    imagingResultRepository.findByImagingExamId(savedEntity.getImagingExamId())
                            .map(ImagingResultEntity::getImagingResultId));
            downstreamOutcomeEventPublisher.publishDiagnosticExamOutcome(
                    toDiagnosticExamOutcome(
                            "IMAGING",
                            savedEntity.getImagingExamId(),
                            savedEntity.getTestExecutionId(),
                            resolveOrderItemId(savedEntity.getTestExecutionId()),
                            savedEntity.getProgressStatus(),
                            savedEntity.getPatientId(),
                            savedEntity.getDetailCode()
                    )
            );
        }

        return imagingResMapstruct.toDTO(savedEntity);
    }

    @Override
    @Transactional
    public void deleteImaging(String id) {
        ImagingEntity entity = imagingRepository.findById(id)
                .orElseThrow(() -> new ImagingNotFoundException("Imaging exam not found. id=" + id));
        entity.setStatus("INACTIVE");
        entity.setUpdatedAt(LocalDateTime.now());
        imagingRepository.save(entity);
    }

    @Override
    public List<EndoscopyDTO> findEndoscopyList(EndoscopyExamSearchCondition condition) {
        List<EndoscopyDTO> list = endoscopyResMapStruct.toDTOList(endoscopyRepository.findAll());
        return filterEndoscopyList(list, condition);
    }

    @Override
    public EndoscopyDTO findEndoscopyDetail(String id) {
        return endoscopyResMapStruct.toDTO(endoscopyRepository.findById(id)
                .orElseThrow(() -> new EndoscopyNotFoundException("Endoscopy exam not found. id=" + id)));
    }

    @Override
    @Transactional
    public EndoscopyDTO registerEndoscopy(EndoscopyCreateReqDTO endoscopyDTO) {
        EndoscopyEntity entity = endoscopyReqMapStruct.toEntity(endoscopyDTO);
        entity.setEndoscopyExamId(sequenceIdService.nextId(SequenceIdType.ENDOSCOPY_EXAM_ID));
        entity.setSedationYn(normalizeYnFlag(entity.getSedationYn()));
        entity.setPerformerId(normalizeOptionalValue(entity.getPerformerId()));
        entity.setPerformerName(normalizeOptionalValue(entity.getPerformerName()));
        entity.setStatus(normalizeStatus(entity.getStatus()));
        entity.setProgressStatus(normalizeProgressStatus(entity.getProgressStatus()));
        entity.setCreatedAt(LocalDateTime.now());
        return endoscopyResMapStruct.toDTO(endoscopyRepository.save(entity));
    }

    @Override
    @Transactional
    public EndoscopyDTO modifyEndoscopy(String id, EndoscopyDTO endoscopyDTO) {
        validateBodyIdAbsent("endoscopyExamId", endoscopyDTO.getEndoscopyExamId());
        EndoscopyEntity entity = endoscopyRepository.findById(id)
                .orElseThrow(() -> new EndoscopyNotFoundException("Endoscopy exam not found. id=" + id));
        String previousProgressStatus = entity.getProgressStatus();
        endoscopyReqMapStruct.updateEntityFromDto(endoscopyDTO, entity);
        if (endoscopyDTO.getSedationYn() != null) {
            entity.setSedationYn(normalizeYnFlag(endoscopyDTO.getSedationYn()));
        }
        if (endoscopyDTO.getPerformerId() != null) {
            entity.setPerformerId(normalizeOptionalValue(endoscopyDTO.getPerformerId()));
        }
        if (endoscopyDTO.getPerformerName() != null) {
            entity.setPerformerName(normalizeOptionalValue(endoscopyDTO.getPerformerName()));
        }
        entity.setStatus(hasText(endoscopyDTO.getStatus())
                ? normalizeStatus(endoscopyDTO.getStatus())
                : normalizeStatus(entity.getStatus()));
        entity.setProgressStatus(resolveProgressStatus(endoscopyDTO.getProgressStatus(), entity.getProgressStatus()));
        entity.setUpdatedAt(LocalDateTime.now());
        EndoscopyEntity savedEntity = endoscopyRepository.save(entity);

        if (!isCompleted(previousProgressStatus) && isCompleted(savedEntity.getProgressStatus())) {
            markTestExecutionCompleted(savedEntity.getTestExecutionId());
            ensureEndoscopyResultExists(savedEntity);
            stageDiagnosticCompletionFromExam("ENDOSCOPY", savedEntity.getPatientId(),
                    endoscopyResultRepository.findByEndoscopyExamId(savedEntity.getEndoscopyExamId())
                            .map(EndoscopyResultEntity::getEndoscopyResultId));
            downstreamOutcomeEventPublisher.publishDiagnosticExamOutcome(
                    toDiagnosticExamOutcome(
                            "ENDOSCOPY",
                            savedEntity.getEndoscopyExamId(),
                            savedEntity.getTestExecutionId(),
                            resolveOrderItemId(savedEntity.getTestExecutionId()),
                            savedEntity.getProgressStatus(),
                            savedEntity.getPatientId(),
                            savedEntity.getDetailCode()
                    )
            );
        }

        return endoscopyResMapStruct.toDTO(savedEntity);
    }

    @Override
    @Transactional
    public void deleteEndoscopy(String id) {
        EndoscopyEntity entity = endoscopyRepository.findById(id)
                .orElseThrow(() -> new EndoscopyNotFoundException("Endoscopy exam not found. id=" + id));
        entity.setStatus("INACTIVE");
        entity.setUpdatedAt(LocalDateTime.now());
        endoscopyRepository.save(entity);
    }

    @Override
    public List<PathologyDTO> findPathologyList(PathologyExamSearchCondition condition) {
        List<PathologyDTO> list = pathologyResMapStruct.toDTOList(pathologyRepository.findAll());
        return filterPathologyList(list, condition);
    }

    @Override
    public PathologyDTO findPathologyDetail(String id) {
        return pathologyResMapStruct.toDTO(pathologyRepository.findById(id)
                .orElseThrow(() -> new PathologyNotFoundException("Pathology exam not found. id=" + id)));
    }

    @Override
    @Transactional
    public PathologyDTO registerPathology(PathologyCreateReqDTO pathologyDTO) {
        PathologyEntity entity = pathologyReqMapStruct.toEntity(pathologyDTO);
        entity.setPathologyExamId(sequenceIdService.nextId(SequenceIdType.PATHOLOGY_EXAM_ID));
        entity.setPerformerId(normalizeOptionalValue(entity.getPerformerId()));
        entity.setPerformerName(normalizeOptionalValue(entity.getPerformerName()));
        entity.setReexamYn(normalizeYnFlag(entity.getReexamYn()));
        entity.setStatus(normalizeStatus(entity.getStatus()));
        entity.setProgressStatus(normalizeProgressStatus(entity.getProgressStatus()));
        entity.setCreatedAt(LocalDateTime.now());
        return pathologyResMapStruct.toDTO(pathologyRepository.save(entity));
    }

    @Override
    @Transactional
    public PathologyDTO modifyPathology(String id, PathologyDTO pathologyDTO) {
        validateBodyIdAbsent("pathologyExamId", pathologyDTO.getPathologyExamId());
        PathologyEntity entity = pathologyRepository.findById(id)
                .orElseThrow(() -> new PathologyNotFoundException("Pathology exam not found. id=" + id));
        String previousProgressStatus = entity.getProgressStatus();
        pathologyReqMapStruct.updateEntityFromDto(pathologyDTO, entity);
        if (pathologyDTO.getPerformerId() != null) {
            entity.setPerformerId(normalizeOptionalValue(pathologyDTO.getPerformerId()));
        }
        if (pathologyDTO.getPerformerName() != null) {
            entity.setPerformerName(normalizeOptionalValue(pathologyDTO.getPerformerName()));
        }
        if (pathologyDTO.getReexamYn() != null) {
            entity.setReexamYn(normalizeYnFlag(pathologyDTO.getReexamYn()));
        }
        entity.setStatus(hasText(pathologyDTO.getStatus())
                ? normalizeStatus(pathologyDTO.getStatus())
                : normalizeStatus(entity.getStatus()));
        entity.setProgressStatus(resolveProgressStatus(pathologyDTO.getProgressStatus(), entity.getProgressStatus()));
        entity.setUpdatedAt(LocalDateTime.now());
        PathologyEntity savedEntity = pathologyRepository.save(entity);

        if (!isCompleted(previousProgressStatus) && isCompleted(savedEntity.getProgressStatus())) {
            markTestExecutionCompleted(savedEntity.getTestExecutionId());
            ensurePathologyResultExists(savedEntity);
            stageDiagnosticCompletionFromExam("PATHOLOGY", savedEntity.getPatientId(),
                    pathologyResultRepository.findByPathologyExamId(savedEntity.getPathologyExamId())
                            .map(PathologyResultEntity::getPathologyExamResultId));
            downstreamOutcomeEventPublisher.publishDiagnosticExamOutcome(
                    toDiagnosticExamOutcome(
                            "PATHOLOGY",
                            savedEntity.getPathologyExamId(),
                            savedEntity.getTestExecutionId(),
                            resolveOrderItemId(savedEntity.getTestExecutionId()),
                            savedEntity.getProgressStatus(),
                            savedEntity.getPatientId(),
                            savedEntity.getDetailCode()
                    )
            );
        }

        return pathologyResMapStruct.toDTO(savedEntity);
    }

    @Override
    @Transactional
    public void deletePathology(String id) {
        PathologyEntity entity = pathologyRepository.findById(id)
                .orElseThrow(() -> new PathologyNotFoundException("Pathology exam not found. id=" + id));
        entity.setStatus("INACTIVE");
        entity.setUpdatedAt(LocalDateTime.now());
        pathologyRepository.save(entity);
    }

    @Override
    public List<PhysiologicalDTO> findPhysiologicalList(PhysiologicalExamSearchCondition condition) {
        List<PhysiologicalDTO> list = physiologicalResMapStruct.toDTOList(physiologicalRepository.findAll());
        return filterPhysiologicalList(list, condition);
    }

    @Override
    public PhysiologicalDTO findPhysiologicalDetail(String id) {
        return physiologicalResMapStruct.toDTO(physiologicalRepository.findById(id)
                .orElseThrow(() -> new PhysiologicalNotFoundException("Physiological exam not found. id=" + id)));
    }

    @Override
    @Transactional
    public PhysiologicalDTO registerPhysiological(PhysiologicalCreateReqDTO physiologicalDTO) {
        PhysiologicalEntity entity = physiologicalReqMapStruct.toEntity(physiologicalDTO);
        entity.setPhysiologicalExamId(sequenceIdService.nextId(SequenceIdType.PHYSIOLOGICAL_EXAM_ID));
        entity.setPerformerId(normalizeOptionalValue(entity.getPerformerId()));
        entity.setPerformerName(normalizeOptionalValue(entity.getPerformerName()));
        entity.setStatus(normalizeStatus(entity.getStatus()));
        entity.setProgressStatus(normalizeProgressStatus(entity.getProgressStatus()));
        entity.setCreatedAt(LocalDateTime.now());
        return physiologicalResMapStruct.toDTO(physiologicalRepository.save(entity));
    }

    @Override
    @Transactional
    public PhysiologicalDTO modifyPhysiological(String id, PhysiologicalDTO physiologicalDTO) {
        validateBodyIdAbsent("physiologicalExamId", physiologicalDTO.getPhysiologicalExamId());
        PhysiologicalEntity entity = physiologicalRepository.findById(id)
                .orElseThrow(() -> new PhysiologicalNotFoundException("Physiological exam not found. id=" + id));
        String previousProgressStatus = entity.getProgressStatus();
        physiologicalReqMapStruct.updateEntityFromDto(physiologicalDTO, entity);
        if (physiologicalDTO.getPerformerId() != null) {
            entity.setPerformerId(normalizeOptionalValue(physiologicalDTO.getPerformerId()));
        }
        if (physiologicalDTO.getPerformerName() != null) {
            entity.setPerformerName(normalizeOptionalValue(physiologicalDTO.getPerformerName()));
        }
        entity.setStatus(hasText(physiologicalDTO.getStatus())
                ? normalizeStatus(physiologicalDTO.getStatus())
                : normalizeStatus(entity.getStatus()));
        entity.setProgressStatus(resolveProgressStatus(physiologicalDTO.getProgressStatus(), entity.getProgressStatus()));
        entity.setUpdatedAt(LocalDateTime.now());
        PhysiologicalEntity savedEntity = physiologicalRepository.save(entity);

        if (!isCompleted(previousProgressStatus) && isCompleted(savedEntity.getProgressStatus())) {
            markTestExecutionCompleted(savedEntity.getTestExecutionId());
            ensurePhysiologicalResultExists(savedEntity);
            stageDiagnosticCompletionFromExam("PHYSIOLOGICAL", savedEntity.getPatientId(),
                    physiologicalResultRepository.findByPhysiologicalExamId(savedEntity.getPhysiologicalExamId())
                            .map(PhysiologicalResultEntity::getPhysiologicalExamResultId));
            downstreamOutcomeEventPublisher.publishDiagnosticExamOutcome(
                    toDiagnosticExamOutcome(
                            "PHYSIOLOGICAL",
                            savedEntity.getPhysiologicalExamId(),
                            savedEntity.getTestExecutionId(),
                            resolveOrderItemId(savedEntity.getTestExecutionId()),
                            savedEntity.getProgressStatus(),
                            savedEntity.getPatientId(),
                            savedEntity.getDetailCode()
                    )
            );
        }

        return physiologicalResMapStruct.toDTO(savedEntity);
    }

    @Override
    @Transactional
    public void deletePhysiological(String id) {
        PhysiologicalEntity entity = physiologicalRepository.findById(id)
                .orElseThrow(() -> new PhysiologicalNotFoundException("Physiological exam not found. id=" + id));
        entity.setStatus("INACTIVE");
        entity.setUpdatedAt(LocalDateTime.now());
        physiologicalRepository.save(entity);
    }

    @Override
    public List<SpecimenDTO> searchSpecimen(String searchType, String searchValue) {
        if (!hasText(searchType) || !hasText(searchValue)) {
            throw new SpecimenNotFoundException("Search type and search value are required.");
        }

        List<SpecimenEntity> result;
        String normalizedType = searchType.trim();

        if ("testExecutionId".equals(normalizedType)) {
            result = specimenRepository.findByTestExecutionId(searchValue);
        } else if ("specimenType".equals(normalizedType)) {
            result = specimenRepository.findBySpecimenType(searchValue);
        } else if ("specimenStatus".equals(normalizedType)) {
            result = specimenRepository.findBySpecimenStatus(searchValue);
        } else {
            throw new SpecimenNotFoundException("Unsupported search type. searchType=" + searchType);
        }

        return specimenResMapStruct.toDTOList(result);
    }

    @Override
    public List<SpecimenDTO> findSpecimenList(SpecimenExamSearchCondition condition) {
        List<SpecimenDTO> list = specimenResMapStruct.toDTOList(specimenRepository.findAll());
        return filterSpecimenList(list, condition);
    }

    @Override
    public SpecimenDTO findSpecimenDetail(String id) {
        return specimenResMapStruct.toDTO(specimenRepository.findById(id)
                .orElseThrow(() -> new SpecimenNotFoundException("Specimen exam not found. id=" + id)));
    }

    @Override
    @Transactional
    public SpecimenDTO registerSpecimen(SpecimenCreateReqDTO specimenDTO) {
        SpecimenEntity entity = specimenReqMapStruct.toEntity(specimenDTO);
        entity.setSpecimenExamId(sequenceIdService.nextId(SequenceIdType.SPECIMEN_EXAM_ID));
        entity.setSpecimenStatus(hasText(entity.getSpecimenStatus()) ? entity.getSpecimenStatus().trim().toUpperCase() : null);
        entity.setRecollectionYn(normalizeYnFlag(entity.getRecollectionYn()));
        entity.setPerformerId(normalizeOptionalValue(entity.getPerformerId()));
        entity.setPerformerName(normalizeOptionalValue(entity.getPerformerName()));
        entity.setStatus(normalizeStatus(entity.getStatus()));
        entity.setProgressStatus(normalizeProgressStatus(entity.getProgressStatus()));
        entity.setCreatedAt(LocalDateTime.now());
        return specimenResMapStruct.toDTO(specimenRepository.save(entity));
    }

    @Override
    @Transactional
    public SpecimenDTO modifySpecimen(String id, SpecimenDTO specimenDTO) {
        validateBodyIdAbsent("specimenExamId", specimenDTO.getSpecimenExamId());
        SpecimenEntity entity = specimenRepository.findById(id)
                .orElseThrow(() -> new SpecimenNotFoundException("Specimen exam not found. id=" + id));
        String previousProgressStatus = entity.getProgressStatus();
        if (specimenDTO.getTestExecutionId() != null) {
            entity.setTestExecutionId(normalizeOptionalValue(specimenDTO.getTestExecutionId()));
        }
        if (specimenDTO.getDetailCode() != null) {
            entity.setDetailCode(specimenDTO.getDetailCode());
        }
        if (specimenDTO.getPatientId() != null) {
            entity.setPatientId(specimenDTO.getPatientId());
        }
        if (specimenDTO.getPatientName() != null) {
            entity.setPatientName(specimenDTO.getPatientName());
        }
        if (specimenDTO.getDepartmentName() != null) {
            entity.setDepartmentName(specimenDTO.getDepartmentName());
        }
        if (specimenDTO.getSpecimenType() != null) {
            entity.setSpecimenType(specimenDTO.getSpecimenType());
        }
        if (specimenDTO.getSpecimenStatus() != null) {
            entity.setSpecimenStatus(hasText(specimenDTO.getSpecimenStatus())
                    ? specimenDTO.getSpecimenStatus().trim().toUpperCase()
                    : null);
        }
        if (specimenDTO.getCollectedAt() != null) {
            entity.setCollectedAt(specimenDTO.getCollectedAt());
        }
        if (specimenDTO.getPerformerId() != null) {
            entity.setPerformerId(normalizeOptionalValue(specimenDTO.getPerformerId()));
        }
        if (specimenDTO.getPerformerName() != null) {
            entity.setPerformerName(normalizeOptionalValue(specimenDTO.getPerformerName()));
        }
        if (specimenDTO.getCollectionSite() != null) {
            entity.setCollectionSite(specimenDTO.getCollectionSite());
        }
        if (specimenDTO.getRecollectionYn() != null) {
            entity.setRecollectionYn(normalizeYnFlag(specimenDTO.getRecollectionYn()));
        }
        entity.setStatus(hasText(specimenDTO.getStatus())
                ? normalizeStatus(specimenDTO.getStatus())
                : normalizeStatus(entity.getStatus()));
        entity.setProgressStatus(resolveProgressStatus(specimenDTO.getProgressStatus(), entity.getProgressStatus()));
        entity.setUpdatedAt(LocalDateTime.now());
        SpecimenEntity savedEntity = specimenRepository.save(entity);

        if (!isCompleted(previousProgressStatus) && isCompleted(savedEntity.getProgressStatus())) {
            markTestExecutionCompleted(savedEntity.getTestExecutionId());
            ensureSpecimenResultExists(savedEntity);
            stageDiagnosticCompletionFromExam("SPECIMEN", savedEntity.getPatientId(),
                    specimenTestResultRepository.findBySpecimenExamId(savedEntity.getSpecimenExamId())
                            .map(SpecimenTestResultEntity::getSpecimenExamResultId));
            downstreamOutcomeEventPublisher.publishDiagnosticExamOutcome(
                    toDiagnosticExamOutcome(
                            "SPECIMEN",
                            savedEntity.getSpecimenExamId(),
                            savedEntity.getTestExecutionId(),
                            resolveOrderItemId(savedEntity.getTestExecutionId()),
                            savedEntity.getProgressStatus(),
                            savedEntity.getPatientId(),
                            savedEntity.getDetailCode()
                    )
            );
        }

        return specimenResMapStruct.toDTO(savedEntity);
    }

    private void markTestExecutionCompleted(String testExecutionId) {
        if (!hasText(testExecutionId)) {
            return;
        }
        TestExecutionEntity entity = testExecutionRepository.findById(testExecutionId.trim()).orElse(null);
        if (entity == null) {
            return;
        }
        String previous = entity.getProgressStatus();
        entity.setProgressStatus(resolveProgressStatus("COMPLETED", previous));
        if (entity.getCompletedAt() == null) {
            entity.setCompletedAt(LocalDateTime.now());
        }
        entity.setUpdatedAt(LocalDateTime.now());
        testExecutionRepository.save(entity);
    }

    private Long resolveOrderItemId(String testExecutionId) {
        if (!hasText(testExecutionId)) {
            return null;
        }
        return testExecutionRepository.findById(testExecutionId.trim())
                .map(TestExecutionEntity::getOrderItemId)
                .orElse(null);
    }

    private static DiagnosticExamOutcomeDTO toDiagnosticExamOutcome(
            String examKind,
            String examId,
            String testExecutionId,
            Long orderItemId,
            String progressStatus,
            Long patientId,
            String detailCode
    ) {
        DiagnosticExamOutcomeDTO dto = new DiagnosticExamOutcomeDTO();
        dto.setExamKind(examKind);
        dto.setExamId(examId);
        dto.setTestExecutionId(testExecutionId);
        dto.setOrderItemId(orderItemId);
        dto.setProgressStatus(progressStatus);
        dto.setPatientId(patientId);
        dto.setDetailCode(detailCode);
        return dto;
    }

    @Override
    @Transactional
    public void deleteSpecimen(String id) {
        SpecimenEntity entity = specimenRepository.findById(id)
                .orElseThrow(() -> new SpecimenNotFoundException("Specimen exam not found. id=" + id));
        entity.setStatus("INACTIVE");
        entity.setUpdatedAt(LocalDateTime.now());
        specimenRepository.save(entity);
    }

    @Override
    public List<TestExecutionDTO> findTestExecutionList(String executionType) {
        if (!hasText(executionType)) {
            return testExecutionResMapStruct.toDTOList(testExecutionRepository.findAll());
        }

        String normalized = executionType.trim().toUpperCase();
        return testExecutionResMapStruct.toDTOList(testExecutionRepository.findByExecutionTypeAndProgressStatus(normalized, "IN_PROGRESS"));
    }

    @Override
    public TestExecutionDTO findTestExecutionDetail(String id) {
        return testExecutionResMapStruct.toDTO(testExecutionRepository.findById(id)
                .orElseThrow(() -> new TestExecutionNotFoundExecution("Test execution not found. id=" + id)));
    }

    @Override
    @Transactional
    public TestExecutionDTO registerTestExecution(TestExecutionReqDTO testExecutionDTO) {
        TestExecutionEntity entity = testExecutionReqMapStruct.toEntity(testExecutionDTO);
        entity.setDetailCode(testExecutionDTO.getDetailCode());
        entity.setStartedAt(testExecutionDTO.getStartedAt());
        entity.setCompletedAt(testExecutionDTO.getCompletedAt());
        entity.setPerformerId(normalizeOptionalValue(testExecutionDTO.getPerformerId()));
        entity.setPerformerName(normalizeOptionalValue(testExecutionDTO.getPerformerName()));
        entity.setTestExecutionId(sequenceIdService.nextId(SequenceIdType.TEST_EXECUTION_ID));
        entity.setStatus(normalizeStatus(null));
        entity.setCreatedAt(LocalDateTime.now());
        if (!hasText(entity.getProgressStatus())) {
            entity.setProgressStatus("WAITING");
        }
        if (entity.getRetryNo() == null) {
            entity.setRetryNo(0);
        }
        return testExecutionResMapStruct.toDTO(testExecutionRepository.save(entity));
    }

    @Override
    @Transactional
    public TestExecutionDTO modifyTestExecution(String id, TestExecutionUpdateDTO testExecutionUpdateDTO) {
        TestExecutionEntity entity = testExecutionRepository.findById(id)
                .orElseThrow(() -> new TestExecutionNotFoundExecution("Test execution not found. id=" + id));


        String previousProgressStatus = entity.getProgressStatus();
        entity.setProgressStatus(resolveProgressStatus(testExecutionUpdateDTO.getProgressStatus(), entity.getProgressStatus()));
        entity.setStatus(hasText(testExecutionUpdateDTO.getStatus())
                ? normalizeStatus(testExecutionUpdateDTO.getStatus())
                : normalizeStatus(entity.getStatus()));
        if (testExecutionUpdateDTO.getRetryNo() != null) {
            entity.setRetryNo(testExecutionUpdateDTO.getRetryNo());
        }
        if (testExecutionUpdateDTO.getCompletedAt() != null) {
            entity.setCompletedAt(testExecutionUpdateDTO.getCompletedAt());
        }
        if (testExecutionUpdateDTO.getDetailCode() != null) {
            entity.setDetailCode(testExecutionUpdateDTO.getDetailCode());
        }
        if (testExecutionUpdateDTO.getOrderItemId() != null) {
            entity.setOrderItemId(testExecutionUpdateDTO.getOrderItemId());
        }
        if (hasText(testExecutionUpdateDTO.getExecutionType())) {
            entity.setExecutionType(normalizeExecutionType(testExecutionUpdateDTO.getExecutionType()));
        }
        if (testExecutionUpdateDTO.getPerformerId() != null) {
            entity.setPerformerId(normalizeOptionalValue(testExecutionUpdateDTO.getPerformerId()));
        }
        if (testExecutionUpdateDTO.getPerformerName() != null) {
            entity.setPerformerName(normalizeOptionalValue(testExecutionUpdateDTO.getPerformerName()));
        }
        if (testExecutionUpdateDTO.getPatientId() != null) {
            entity.setPatientId(testExecutionUpdateDTO.getPatientId());
        }
        if (testExecutionUpdateDTO.getPatientName() != null) {
            entity.setPatientName(testExecutionUpdateDTO.getPatientName());
        }
        if (testExecutionUpdateDTO.getDepartmentName() != null) {
            entity.setDepartmentName(testExecutionUpdateDTO.getDepartmentName());
        }
        entity.setUpdatedAt(LocalDateTime.now());

        if (!isInProgress(previousProgressStatus) && isInProgress(entity.getProgressStatus())) {
            ensureExamRecordExists(entity);
        }

        return testExecutionResMapStruct.toDTO(testExecutionRepository.save(entity));
    }
    private void ensureExamRecordExists(TestExecutionEntity entity) {
        String executionType = normalizeExecutionType(entity.getExecutionType());

        switch (executionType) {
            case "IMAGING" -> ensureImagingExists(entity);
            case "ENDOSCOPY" -> ensureEndoscopyExists(entity);
            case "PATHOLOGY" -> ensurePathologyExists(entity);
            case "PHYSIOLOGICAL" -> ensurePhysiologicalExists(entity);
            case "SPECIMEN" -> ensureSpecimenExists(entity);
            default -> log.warn("Unsupported execution type for automatic exam creation. testExecutionId={}, executionType={}",
                    entity.getTestExecutionId(), entity.getExecutionType());
        }
    }

    private void ensureImagingExists(TestExecutionEntity entity) {
        if (imagingRepository.existsByTestExecutionId(entity.getTestExecutionId())) {
            return;
        }

        ImagingEntity imagingEntity = new ImagingEntity();
        imagingEntity.setImagingExamId(sequenceIdService.nextId(SequenceIdType.IMAGING_EXAM_ID));
        imagingEntity.setTestExecutionId(entity.getTestExecutionId());
        imagingEntity.setImagingType(normalizeExecutionType(entity.getExecutionType()));
        imagingEntity.setDetailCode(entity.getDetailCode());
        imagingEntity.setPatientId(entity.getPatientId());
        imagingEntity.setPatientName(entity.getPatientName());
        imagingEntity.setDepartmentName(entity.getDepartmentName());
        imagingEntity.setPerformerId(normalizeOptionalValue(entity.getPerformerId()));
        imagingEntity.setPerformerName(normalizeOptionalValue(entity.getPerformerName()));
        imagingEntity.setStatus("ACTIVE");
        imagingEntity.setProgressStatus("WAITING");
        imagingEntity.setCreatedAt(LocalDateTime.now());
        imagingRepository.save(imagingEntity);
    }

    private void ensureImagingResultExists(ImagingEntity entity) {
        if (imagingResultRepository.existsByImagingExamId(entity.getImagingExamId())) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        ImagingResultEntity resultEntity = new ImagingResultEntity();
        resultEntity.setImagingResultId(sequenceIdService.nextId(SequenceIdType.IMAGING_RESULT_ID));
        resultEntity.setImagingExamId(entity.getImagingExamId());
        resultEntity.setStatus("ACTIVE");
        resultEntity.setCreatedAt(now);
        resultEntity.setConfirmedAt(now);
        imagingResultRepository.save(resultEntity);
    }

    private void ensureEndoscopyResultExists(EndoscopyEntity entity) {
        if (endoscopyResultRepository.existsByEndoscopyExamId(entity.getEndoscopyExamId())) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        EndoscopyResultEntity resultEntity = new EndoscopyResultEntity();
        resultEntity.setEndoscopyResultId(sequenceIdService.nextId(SequenceIdType.ENDOSCOPY_RESULT_ID));
        resultEntity.setEndoscopyExamId(entity.getEndoscopyExamId());
        resultEntity.setStatus("ACTIVE");
        resultEntity.setCreatedAt(now);
        resultEntity.setConfirmedAt(now);
        endoscopyResultRepository.save(resultEntity);
    }

    private void ensureEndoscopyExists(TestExecutionEntity entity) {
        if (endoscopyRepository.existsByTestExecutionId(entity.getTestExecutionId())) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        EndoscopyEntity endoscopyEntity = new EndoscopyEntity();
        endoscopyEntity.setEndoscopyExamId(sequenceIdService.nextId(SequenceIdType.ENDOSCOPY_EXAM_ID));
        endoscopyEntity.setTestExecutionId(entity.getTestExecutionId());
        endoscopyEntity.setDetailCode(entity.getDetailCode());
        endoscopyEntity.setPatientId(entity.getPatientId());
        endoscopyEntity.setPatientName(entity.getPatientName());
        endoscopyEntity.setDepartmentName(entity.getDepartmentName());
        endoscopyEntity.setPerformerId(normalizeOptionalValue(entity.getPerformerId()));
        endoscopyEntity.setPerformerName(normalizeOptionalValue(entity.getPerformerName()));
        endoscopyEntity.setSedationYn("N");
        endoscopyEntity.setStatus("ACTIVE");
        endoscopyEntity.setProgressStatus("WAITING");
        endoscopyEntity.setCreatedAt(now);
        endoscopyRepository.save(endoscopyEntity);
    }

    private void ensurePathologyExists(TestExecutionEntity entity) {
        if (pathologyRepository.existsByTestExecutionId(entity.getTestExecutionId())) {
            return;
        }

        PathologyEntity pathologyEntity = new PathologyEntity();
        pathologyEntity.setPathologyExamId(sequenceIdService.nextId(SequenceIdType.PATHOLOGY_EXAM_ID));
        pathologyEntity.setTestExecutionId(entity.getTestExecutionId());
        pathologyEntity.setDetailCode(entity.getDetailCode());
        pathologyEntity.setPatientId(entity.getPatientId());
        pathologyEntity.setPatientName(entity.getPatientName());
        pathologyEntity.setDepartmentName(entity.getDepartmentName());
        pathologyEntity.setPerformerId(normalizeOptionalValue(entity.getPerformerId()));
        pathologyEntity.setPerformerName(normalizeOptionalValue(entity.getPerformerName()));
        pathologyEntity.setReexamYn("N");
        pathologyEntity.setStatus("ACTIVE");
        pathologyEntity.setProgressStatus("WAITING");
        pathologyEntity.setCreatedAt(LocalDateTime.now());
        pathologyRepository.save(pathologyEntity);
    }

    private void ensurePathologyResultExists(PathologyEntity entity) {
        if (pathologyResultRepository.existsByPathologyExamId(entity.getPathologyExamId())) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        PathologyResultEntity resultEntity = new PathologyResultEntity();
        resultEntity.setPathologyExamResultId(sequenceIdService.nextId(SequenceIdType.PATHOLOGY_EXAM_RESULT_ID));
        resultEntity.setPathologyExamId(entity.getPathologyExamId());
        resultEntity.setStatus("ACTIVE");
        resultEntity.setCreatedAt(now);
        resultEntity.setConfirmedAt(now);
        pathologyResultRepository.save(resultEntity);
    }

    private void ensurePhysiologicalExists(TestExecutionEntity entity) {
        if (physiologicalRepository.existsByTestExecutionId(entity.getTestExecutionId())) {
            return;
        }

        PhysiologicalEntity physiologicalEntity = new PhysiologicalEntity();
        physiologicalEntity.setPhysiologicalExamId(sequenceIdService.nextId(SequenceIdType.PHYSIOLOGICAL_EXAM_ID));
        physiologicalEntity.setTestExecutionId(entity.getTestExecutionId());
        physiologicalEntity.setDetailCode(entity.getDetailCode());
        physiologicalEntity.setPatientId(entity.getPatientId());
        physiologicalEntity.setPatientName(entity.getPatientName());
        physiologicalEntity.setDepartmentName(entity.getDepartmentName());
        physiologicalEntity.setPerformerId(normalizeOptionalValue(entity.getPerformerId()));
        physiologicalEntity.setPerformerName(normalizeOptionalValue(entity.getPerformerName()));
        physiologicalEntity.setStatus("ACTIVE");
        physiologicalEntity.setProgressStatus("WAITING");
        physiologicalEntity.setCreatedAt(LocalDateTime.now());
        physiologicalRepository.save(physiologicalEntity);
    }

    private void ensurePhysiologicalResultExists(PhysiologicalEntity entity) {
        if (physiologicalResultRepository.existsByPhysiologicalExamId(entity.getPhysiologicalExamId())) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        PhysiologicalResultEntity resultEntity = new PhysiologicalResultEntity();
        resultEntity.setPhysiologicalExamResultId(sequenceIdService.nextId(SequenceIdType.PHYSIOLOGICAL_EXAM_RESULT_ID));
        resultEntity.setPhysiologicalExamId(entity.getPhysiologicalExamId());
        resultEntity.setStatus("ACTIVE");
        resultEntity.setCreatedAt(now);
        resultEntity.setConfirmedAt(now);
        physiologicalResultRepository.save(resultEntity);
    }

    private void ensureSpecimenExists(TestExecutionEntity entity) {
        if (specimenRepository.existsByTestExecutionId(entity.getTestExecutionId())) {
            return;
        }

        SpecimenEntity specimenEntity = new SpecimenEntity();
        specimenEntity.setSpecimenExamId(sequenceIdService.nextId(SequenceIdType.SPECIMEN_EXAM_ID));
        specimenEntity.setTestExecutionId(entity.getTestExecutionId());
        specimenEntity.setDetailCode(entity.getDetailCode());
        specimenEntity.setPatientId(entity.getPatientId());
        specimenEntity.setPatientName(entity.getPatientName());
        specimenEntity.setDepartmentName(entity.getDepartmentName());
        specimenEntity.setPerformerId(normalizeOptionalValue(entity.getPerformerId()));
        specimenEntity.setPerformerName(normalizeOptionalValue(entity.getPerformerName()));
        specimenEntity.setRecollectionYn("N");
        specimenEntity.setStatus("ACTIVE");
        specimenEntity.setProgressStatus("WAITING");
        specimenEntity.setCreatedAt(LocalDateTime.now());
        specimenRepository.save(specimenEntity);
    }

    private void ensureSpecimenResultExists(SpecimenEntity entity) {
        if (specimenTestResultRepository.existsBySpecimenExamId(entity.getSpecimenExamId())) {
            return;
        }
        LocalDateTime now = LocalDateTime.now();
        SpecimenTestResultEntity resultEntity = new SpecimenTestResultEntity();
        resultEntity.setSpecimenExamResultId(sequenceIdService.nextId(SequenceIdType.SPECIMEN_EXAM_RESULT_ID));
        resultEntity.setSpecimenExamId(entity.getSpecimenExamId());
        resultEntity.setStatus("ACTIVE");
        resultEntity.setCreatedAt(now);
        resultEntity.setConfirmedAt(now);
        specimenTestResultRepository.save(resultEntity);
    }

    private void stageDiagnosticCompletionFromExam(String resultType, Long patientId, Optional<String> resultIdOptional) {
        resultIdOptional.ifPresent(resultId ->
                claimsCompletionStageService.stageDiagnosticCompleted(patientId, resultType, resultId)
        );
    }

    private List<ImagingDTO> filterImagingList(List<ImagingDTO> list, ImagingExamSearchCondition raw) {
        ImagingExamSearchCondition c = raw != null ? raw : new ImagingExamSearchCondition();
        return list.stream()
                .filter(dto -> execMatchesContains(dto.getPatientName(), c.getPatientName()))
                .filter(dto -> execMatchesContains(dto.getDepartmentName(), c.getDepartmentName()))
                .filter(dto -> execMatchesEquals(dto.getProgressStatus(), c.getProgressStatus()))
                .filter(dto -> execDateRange(dto.getCreatedAt(), c.getStartDate(), c.getEndDate()))
                .filter(dto -> matchesImagingExamName(dto, c.getExamName()))
                .toList();
    }

    private boolean matchesImagingExamName(ImagingDTO dto, String examNameKeyword) {
        if (!hasText(examNameKeyword)) {
            return true;
        }
        String key = examNameKeyword.trim().toLowerCase();
        String detail = dto.getDetailCode() != null ? dto.getDetailCode().trim().toLowerCase() : "";
        String type = dto.getImagingType() != null ? dto.getImagingType().trim().toLowerCase() : "";
        return detail.contains(key) || type.contains(key);
    }

    private List<SpecimenDTO> filterSpecimenList(List<SpecimenDTO> list, SpecimenExamSearchCondition raw) {
        SpecimenExamSearchCondition c = raw != null ? raw : new SpecimenExamSearchCondition();
        return list.stream()
                .filter(dto -> execMatchesContains(dto.getPatientName(), c.getPatientName()))
                .filter(dto -> execMatchesContains(dto.getSpecimenType(), c.getSpecimenType()))
                .filter(dto -> execMatchesEquals(dto.getSpecimenStatus(), c.getSpecimenStatus()))
                .filter(dto -> execMatchesEquals(dto.getProgressStatus(), c.getProgressStatus()))
                .filter(dto -> execDateRangeWithFallback(dto.getCollectedAt(), dto.getCreatedAt(), c.getStartDate(), c.getEndDate()))
                .toList();
    }

    private List<PathologyDTO> filterPathologyList(List<PathologyDTO> list, PathologyExamSearchCondition raw) {
        PathologyExamSearchCondition c = raw != null ? raw : new PathologyExamSearchCondition();
        return list.stream()
                .filter(dto -> execMatchesContains(dto.getPatientName(), c.getPatientName()))
                .filter(dto -> execMatchesContains(dto.getDepartmentName(), c.getDepartmentName()))
                .filter(dto -> execMatchesContains(dto.getTissueStatus(), c.getTissueStatus()))
                .filter(dto -> execMatchesEquals(dto.getProgressStatus(), c.getProgressStatus()))
                .filter(dto -> execDateRangeWithFallback(dto.getCollectedAt(), dto.getCreatedAt(), c.getStartDate(), c.getEndDate()))
                .toList();
    }

    private List<EndoscopyDTO> filterEndoscopyList(List<EndoscopyDTO> list, EndoscopyExamSearchCondition raw) {
        EndoscopyExamSearchCondition c = raw != null ? raw : new EndoscopyExamSearchCondition();
        return list.stream()
                .filter(dto -> execMatchesContains(dto.getPatientName(), c.getPatientName()))
                .filter(dto -> execMatchesContains(dto.getDepartmentName(), c.getDepartmentName()))
                .filter(dto -> matchesSedationFilter(dto.getSedationYn(), c.getSedationYn()))
                .filter(dto -> execMatchesEquals(dto.getProgressStatus(), c.getProgressStatus()))
                .filter(dto -> execDateRangeWithFallback(dto.getProcedureAt(), dto.getCreatedAt(), c.getStartDate(), c.getEndDate()))
                .toList();
    }

    private boolean matchesSedationFilter(String dtoValue, String conditionValue) {
        if (!hasText(conditionValue)) {
            return true;
        }
        return normalizeYnFlag(dtoValue).equals(normalizeYnFlag(conditionValue));
    }

    private List<PhysiologicalDTO> filterPhysiologicalList(List<PhysiologicalDTO> list, PhysiologicalExamSearchCondition raw) {
        PhysiologicalExamSearchCondition c = raw != null ? raw : new PhysiologicalExamSearchCondition();
        return list.stream()
                .filter(dto -> execMatchesEquals(dto.getPhysiologicalExamId(), c.getPhysiologicalExamId()))
                .filter(dto -> execMatchesContains(dto.getPatientName(), c.getPatientName()))
                .filter(dto -> execMatchesContains(dto.getDepartmentName(), c.getDepartmentName()))
                .filter(dto -> execMatchesEquals(dto.getProgressStatus(), c.getProgressStatus()))
                .filter(dto -> execDateRange(dto.getCreatedAt(), c.getStartDate(), c.getEndDate()))
                .toList();
    }

    private boolean execMatchesEquals(String source, String keyword) {
        if (!hasText(keyword)) {
            return true;
        }
        if (source == null) {
            return false;
        }
        return source.trim().equalsIgnoreCase(keyword.trim());
    }

    private boolean execMatchesContains(String source, String keyword) {
        if (!hasText(keyword)) {
            return true;
        }
        if (source == null) {
            return false;
        }
        return source.trim().toLowerCase().contains(keyword.trim().toLowerCase());
    }

    private boolean execDateRange(LocalDateTime at, LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            return true;
        }
        if (at == null) {
            return false;
        }
        LocalDateTime start = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime end = endDate != null ? endDate.atTime(LocalTime.MAX) : null;
        if (start != null && at.isBefore(start)) {
            return false;
        }
        if (end != null && at.isAfter(end)) {
            return false;
        }
        return true;
    }

    /** 날짜 구간 필터: primary(예: 채취일시·시술일시)가 있으면 그걸 쓰고, 없으면 fallback(보통 createdAt)으로 비교 */
    private boolean execDateRangeWithFallback(
            LocalDateTime primary,
            LocalDateTime fallback,
            LocalDate startDate,
            LocalDate endDate
    ) {
        LocalDateTime at = primary != null ? primary : fallback;
        return execDateRange(at, startDate, endDate);
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private boolean isInProgress(String progressStatus) {
        return "IN_PROGRESS".equalsIgnoreCase(progressStatus != null ? progressStatus.trim() : null);
    }

    private boolean isCompleted(String progressStatus) {
        return "COMPLETED".equalsIgnoreCase(progressStatus != null ? progressStatus.trim() : null);
    }

    private boolean isCancelled(String progressStatus) {
        return "CANCELLED".equalsIgnoreCase(progressStatus != null ? progressStatus.trim() : null);
    }

    private boolean isTerminalProgress(String progressStatus) {
        return isCompleted(progressStatus) || isCancelled(progressStatus);
    }

    private String normalizeExecutionType(String executionType) {
        return hasText(executionType) ? executionType.trim().toUpperCase() : "";
    }

    private void validateBodyIdAbsent(String fieldName, String value) {
        if (value != null) {
            throw new InvalidRequestException("Request body must not include " + fieldName + ".");
        }
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

    private String normalizeYnFlag(String value) {
        if (!hasText(value)) {
            return "N";
        }

        String trimmed = value.trim().toUpperCase();
        if ("Y".equals(trimmed) || "YES".equals(trimmed) || "TRUE".equals(trimmed)) {
            return "Y";
        }

        return "N";
    }

    private String normalizeProgressStatus(String value) {
        if (!hasText(value)) {
            return "WAITING";
        }

        String trimmed = value.trim().toUpperCase();
        if ("WAITING".equals(trimmed)
                || "IN_PROGRESS".equals(trimmed)
                || "COMPLETED".equals(trimmed)
                || "CANCELLED".equals(trimmed)) {
            return trimmed;
        }

        return "WAITING";
    }

    /**
     * 허용: WAITING → IN_PROGRESS / COMPLETED / CANCELLED; IN_PROGRESS → COMPLETED / CANCELLED; 동일 값(멱등).
     * 금지: IN_PROGRESS → WAITING; COMPLETED/CANCELLED → 다른 값.
     */
    private void assertProgressTransition(String currentNormalized, String nextNormalized) {
        if (currentNormalized.equals(nextNormalized)) {
            return;
        }
        if (isTerminalProgress(currentNormalized)) {
            throw new InvalidRequestException(
                    "Invalid progressStatus transition: cannot change from "
                            + currentNormalized + " to " + nextNormalized + ".");
        }
        if (isInProgress(currentNormalized) && "WAITING".equals(nextNormalized)) {
            throw new InvalidRequestException(
                    "Invalid progressStatus transition: cannot change from IN_PROGRESS to WAITING.");
        }
        if ("WAITING".equals(currentNormalized)) {
            if ("IN_PROGRESS".equals(nextNormalized)
                    || "COMPLETED".equals(nextNormalized)
                    || "CANCELLED".equals(nextNormalized)) {
                return;
            }
            throw new InvalidRequestException(
                    "Invalid progressStatus transition from WAITING to " + nextNormalized + ".");
        }
        if (isInProgress(currentNormalized)) {
            if ("COMPLETED".equals(nextNormalized) || "CANCELLED".equals(nextNormalized)) {
                return;
            }
            throw new InvalidRequestException(
                    "Invalid progressStatus transition from IN_PROGRESS to " + nextNormalized + ".");
        }
        throw new InvalidRequestException("Unsupported progressStatus: " + currentNormalized);
    }

    private String resolveProgressStatus(String newValue, String currentValue) {
        String currentNorm = hasText(currentValue) ? normalizeProgressStatus(currentValue) : "WAITING";
        if (!hasText(newValue)) {
            return currentNorm;
        }
        String nextNorm = normalizeProgressStatus(newValue);
        assertProgressTransition(currentNorm, nextNorm);
        return nextNorm;
    }

    private String normalizeOptionalValue(String value) {
        return hasText(value) ? value.trim() : null;
    }

}
