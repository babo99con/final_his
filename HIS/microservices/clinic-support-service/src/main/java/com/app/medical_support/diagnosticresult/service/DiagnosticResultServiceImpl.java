package com.app.medical_support.diagnosticresult.service;

import com.app.medical_support.common.sequence.SequenceIdService;
import com.app.medical_support.common.sequence.SequenceIdType;
import com.app.medical_support.common.exception.InvalidRequestException;
import com.app.medical_support.common.integration.claims.service.ClaimsCompletionStageService;
import com.app.medical_support.diagnosticexecution.entity.TestExecutionEntity;
import com.app.medical_support.diagnosticexecution.repository.TestExecutionRepository;
import com.app.medical_support.integration.outbound.kafka.DownstreamOutcomeEventPublisher;
import com.app.medical_support.diagnosticresult.dto.*;
import com.app.medical_support.diagnosticresult.entity.EndoscopyResultEntity;
import com.app.medical_support.diagnosticresult.entity.ImagingResultEntity;
import com.app.medical_support.diagnosticresult.entity.PathologyResultEntity;
import com.app.medical_support.diagnosticresult.entity.PhysiologicalResultEntity;
import com.app.medical_support.diagnosticresult.entity.SpecimenTestResultEntity;
import com.app.medical_support.diagnosticresult.exception.DiagnosticResultNotFoundException;
import com.app.medical_support.diagnosticresult.repository.EndoscopyResultRepository;
import com.app.medical_support.diagnosticresult.repository.ImagingResultRepository;
import com.app.medical_support.diagnosticresult.repository.PathologyResultRepository;
import com.app.medical_support.diagnosticresult.repository.PhysiologicalResultRepository;
import com.app.medical_support.diagnosticresult.repository.SpecimenTestResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagnosticResultServiceImpl implements DiagnosticResultService {

    private final ImagingResultRepository imagingResultRepository;
    private final EndoscopyResultRepository endoscopyResultRepository;
    private final PathologyResultRepository pathologyResultRepository;
    private final PhysiologicalResultRepository physiologicalResultRepository;
    private final SpecimenTestResultRepository specimenTestResultRepository;
    private final TestExecutionRepository testExecutionRepository;
    private final SequenceIdService sequenceIdService;
    private final ClaimsCompletionStageService claimsCompletionStageService;
    private final DownstreamOutcomeEventPublisher downstreamOutcomeEventPublisher;

    private static final String TYPE_IMAGING = "IMAGING";
    private static final String TYPE_SPECIMEN = "SPECIMEN";
    private static final String TYPE_PATHOLOGY = "PATHOLOGY";
    private static final String TYPE_ENDOSCOPY = "ENDOSCOPY";
    private static final String TYPE_PHYSIOLOGICAL = "PHYSIOLOGICAL";
    private static final String STATUS_INACTIVE = "INACTIVE";
    private static final String PROGRESS_IN_PROGRESS = "IN_PROGRESS";
    private static final String PROGRESS_COMPLETED = "COMPLETED";

    // 검사별 CRUD

    @Override
    public List<ImagingResultDTO> findImagingResultList() {
        return imagingResultRepository.findImagingResultResponseList();
    }

    @Override
    public ImagingResultDTO findImagingResultDetail(String id) {
        return getImagingResultResponse(id);
    }

    @Override
    @Transactional
    public ImagingResultDTO registerImagingResult(ImagingResultCreateReqDTO dto) {
        ImagingResultEntity entity = new ImagingResultEntity();
        entity.setImagingResultId(sequenceIdService.nextId(SequenceIdType.IMAGING_RESULT_ID));
        entity.setImagingExamId(dto.getImagingExamId());
        entity.setImagingType(dto.getImagingType());
        entity.setResultSummary(dto.getResultSummary());
        entity.setReadingDetail(dto.getReadingDetail());
        entity.setConfirmedAt(dto.getConfirmedAt());
        entity.setResultManagerId(dto.getResultManagerId());
        entity.setResultManagerName(dto.getResultManagerName());
        entity.setStatus(normalizeStatus(dto.getStatus()));
        entity.setProgressStatus(PROGRESS_IN_PROGRESS);
        entity.setCreatedAt(LocalDateTime.now());

        ImagingResultEntity savedEntity = imagingResultRepository.save(entity);

        return getImagingResultResponse(savedEntity.getImagingResultId());


    }

    @Override
    @Transactional
    public ImagingResultDTO modifyImagingResult(String id, ImagingResultUpdateReqDTO dto) {
        ImagingResultEntity entity = imagingResultRepository.findById(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Imaging result not found. id=" + id));
        if (dto.getResultSummary() != null) {
            entity.setResultSummary(dto.getResultSummary());
        }
        if (dto.getReadingDetail() != null) {
            entity.setReadingDetail(dto.getReadingDetail());
        }
        if (dto.getConfirmedAt() != null) {
            entity.setConfirmedAt(dto.getConfirmedAt());
        }
        if (dto.getResultManagerId() != null) {
            entity.setResultManagerId(dto.getResultManagerId());
        }
        if (dto.getResultManagerName() != null) {
            entity.setResultManagerName(dto.getResultManagerName());
        }
        if (hasText(dto.getStatus())) {
            entity.setStatus(normalizeStatus(dto.getStatus()));
        }
        entity.setUpdatedAt(LocalDateTime.now());
        ImagingResultEntity savedEntity = imagingResultRepository.save(entity);
        return getImagingResultResponse(savedEntity.getImagingResultId());
    }

    @Override
    @Transactional
    public void deleteImagingResult(String id) {
        ImagingResultEntity entity = imagingResultRepository.findById(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Imaging result not found. id=" + id));
        entity.setStatus(STATUS_INACTIVE);
        imagingResultRepository.save(entity);
    }

    @Override
    public List<EndoscopyResultDTO> findEndoscopyResultList() {
        return endoscopyResultRepository.findEndoscopyResultResponseList();
    }

    @Override
    public EndoscopyResultDTO findEndoscopyResultDetail(String id) {
        return getEndoscopyResultResponse(id);
    }

    @Override
    @Transactional
    public EndoscopyResultDTO registerEndoscopyResult(EndoscopyResultCreateReqDTO dto) {
        EndoscopyResultEntity entity = new EndoscopyResultEntity();
        entity.setEndoscopyResultId(sequenceIdService.nextId(SequenceIdType.ENDOSCOPY_RESULT_ID));
        entity.setEndoscopyExamId(dto.getEndoscopyExamId());
        entity.setResultSummary(dto.getResultSummary());
        entity.setBiopsyYn(normalizeYnFlag(dto.getBiopsyYn()));
        entity.setConfirmedAt(dto.getConfirmedAt());
        entity.setResultManagerId(dto.getResultManagerId());
        entity.setResultManagerName(dto.getResultManagerName());
        entity.setReaderId(dto.getReaderId());
        entity.setStatus(normalizeStatus(dto.getStatus()));
        entity.setProgressStatus(PROGRESS_IN_PROGRESS);
        entity.setCreatedAt(LocalDateTime.now());
        EndoscopyResultEntity savedEntity = endoscopyResultRepository.save(entity);
        return getEndoscopyResultResponse(savedEntity.getEndoscopyResultId());
    }

    @Override
    @Transactional
    public EndoscopyResultDTO modifyEndoscopyResult(String id, EndoscopyResultUpdateReqDTO dto) {
        EndoscopyResultEntity entity = endoscopyResultRepository.findById(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Endoscopy result not found. id=" + id));
        if (dto.getResultSummary() != null) {
            entity.setResultSummary(dto.getResultSummary());
        }
        if (hasText(dto.getBiopsyYn())) {
            entity.setBiopsyYn(normalizeYnFlag(dto.getBiopsyYn()));
        }
        if (dto.getConfirmedAt() != null) {
            entity.setConfirmedAt(dto.getConfirmedAt());
        }
        if (dto.getResultManagerId() != null) {
            entity.setResultManagerId(dto.getResultManagerId());
        }
        if (dto.getResultManagerName() != null) {
            entity.setResultManagerName(dto.getResultManagerName());
        }
        if (dto.getReaderId() != null) {
            entity.setReaderId(dto.getReaderId());
        }
        if (hasText(dto.getStatus())) {
            entity.setStatus(normalizeStatus(dto.getStatus()));
        }
        entity.setUpdatedAt(LocalDateTime.now());
        EndoscopyResultEntity savedEntity = endoscopyResultRepository.save(entity);
        return getEndoscopyResultResponse(savedEntity.getEndoscopyResultId());
    }

    @Override
    @Transactional
    public void deleteEndoscopyResult(String id) {
        EndoscopyResultEntity entity = endoscopyResultRepository.findById(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Endoscopy result not found. id=" + id));
        entity.setStatus(STATUS_INACTIVE);
        endoscopyResultRepository.save(entity);
    }

    @Override
    public List<PathologyResultDTO> findPathologyResultList() {
        return pathologyResultRepository.findPathologyResultResponseList();
    }

    @Override
    public PathologyResultDTO findPathologyResultDetail(String id) {
        return getPathologyResultResponse(id);
    }

    @Override
    @Transactional
    public PathologyResultDTO registerPathologyResult(PathologyResultCreateReqDTO dto) {
        PathologyResultEntity entity = new PathologyResultEntity();
        entity.setPathologyExamResultId(sequenceIdService.nextId(SequenceIdType.PATHOLOGY_EXAM_RESULT_ID));
        entity.setPathologyExamId(dto.getPathologyExamId());
        entity.setResultSummary(dto.getResultSummary());
        entity.setJudgedAt(dto.getJudgedAt());
        entity.setConfirmedAt(dto.getConfirmedAt());
        entity.setResultManagerId(dto.getResultManagerId());
        entity.setResultManagerName(dto.getResultManagerName());
        entity.setReaderId(dto.getReaderId());
        entity.setDiagnosisName(dto.getDiagnosisName());
        entity.setStatus(normalizeStatus(dto.getStatus()));
        entity.setProgressStatus(PROGRESS_IN_PROGRESS);
        entity.setCreatedAt(LocalDateTime.now());
        PathologyResultEntity savedEntity = pathologyResultRepository.save(entity);
        return getPathologyResultResponse(savedEntity.getPathologyExamResultId());
    }

    @Override
    @Transactional
    public PathologyResultDTO modifyPathologyResult(String id, PathologyResultUpdateReqDTO dto) {
        PathologyResultEntity entity = pathologyResultRepository.findById(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Pathology result not found. id=" + id));
        if (dto.getResultSummary() != null) {
            entity.setResultSummary(dto.getResultSummary());
        }
        if (dto.getJudgedAt() != null) {
            entity.setJudgedAt(dto.getJudgedAt());
        }
        if (dto.getConfirmedAt() != null) {
            entity.setConfirmedAt(dto.getConfirmedAt());
        }
        if (dto.getResultManagerId() != null) {
            entity.setResultManagerId(dto.getResultManagerId());
        }
        if (dto.getResultManagerName() != null) {
            entity.setResultManagerName(dto.getResultManagerName());
        }
        if (dto.getReaderId() != null) {
            entity.setReaderId(dto.getReaderId());
        }
        if (dto.getDiagnosisName() != null) {
            entity.setDiagnosisName(dto.getDiagnosisName());
        }
        if (hasText(dto.getStatus())) {
            entity.setStatus(normalizeStatus(dto.getStatus()));
        }
        entity.setUpdatedAt(LocalDateTime.now());
        PathologyResultEntity savedEntity = pathologyResultRepository.save(entity);
        return getPathologyResultResponse(savedEntity.getPathologyExamResultId());
    }

    @Override
    @Transactional
    public void deletePathologyResult(String id) {
        PathologyResultEntity entity = pathologyResultRepository.findById(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Pathology result not found. id=" + id));
        entity.setStatus(STATUS_INACTIVE);
        pathologyResultRepository.save(entity);
    }

    @Override
    public List<PhysiologicalResultDTO> findPhysiologicalResultList() {
        return physiologicalResultRepository.findPhysiologicalResultResponseList();
    }

    @Override
    public PhysiologicalResultDTO findPhysiologicalResultDetail(String id) {
        return getPhysiologicalResultResponse(id);
    }

    @Override
    @Transactional
    public PhysiologicalResultDTO registerPhysiologicalResult(PhysiologicalResultCreateReqDTO dto) {
        PhysiologicalResultEntity entity = new PhysiologicalResultEntity();
        entity.setPhysiologicalExamResultId(sequenceIdService.nextId(SequenceIdType.PHYSIOLOGICAL_EXAM_RESULT_ID));
        entity.setPhysiologicalExamId(dto.getPhysiologicalExamId());
        entity.setResultSummary(dto.getResultSummary());
        entity.setReport(dto.getReport());
        entity.setMeasuredItemCode(dto.getMeasuredItemCode());
        entity.setConfirmedAt(dto.getConfirmedAt());
        entity.setResultManagerId(dto.getResultManagerId());
        entity.setResultManagerName(dto.getResultManagerName());
        entity.setStatus(normalizeStatus(dto.getStatus()));
        entity.setProgressStatus(PROGRESS_IN_PROGRESS);
        entity.setCreatedAt(LocalDateTime.now());
        PhysiologicalResultEntity savedEntity = physiologicalResultRepository.save(entity);
        return getPhysiologicalResultResponse(savedEntity.getPhysiologicalExamResultId());
    }

    @Override
    @Transactional
    public PhysiologicalResultDTO modifyPhysiologicalResult(String id, PhysiologicalResultUpdateReqDTO dto) {
        PhysiologicalResultEntity entity = physiologicalResultRepository.findById(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Physiological result not found. id=" + id));
        if (dto.getResultSummary() != null) {
            entity.setResultSummary(dto.getResultSummary());
        }
        if (dto.getReport() != null) {
            entity.setReport(dto.getReport());
        }
        if (dto.getMeasuredItemCode() != null) {
            entity.setMeasuredItemCode(dto.getMeasuredItemCode());
        }
        if (dto.getConfirmedAt() != null) {
            entity.setConfirmedAt(dto.getConfirmedAt());
        }
        if (dto.getResultManagerId() != null) {
            entity.setResultManagerId(dto.getResultManagerId());
        }
        if (dto.getResultManagerName() != null) {
            entity.setResultManagerName(dto.getResultManagerName());
        }
        if (hasText(dto.getStatus())) {
            entity.setStatus(normalizeStatus(dto.getStatus()));
        }
        entity.setUpdatedAt(LocalDateTime.now());
        PhysiologicalResultEntity savedEntity = physiologicalResultRepository.save(entity);
        return getPhysiologicalResultResponse(savedEntity.getPhysiologicalExamResultId());
    }

    @Override
    @Transactional
    public void deletePhysiologicalResult(String id) {
        PhysiologicalResultEntity entity = physiologicalResultRepository.findById(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Physiological result not found. id=" + id));
        entity.setStatus(STATUS_INACTIVE);
        physiologicalResultRepository.save(entity);
    }

    @Override
    public List<SpecimenTestResultDTO> findSpecimenResultList() {
        return specimenTestResultRepository.findSpecimenResultResponseList();
    }

    @Override
    public SpecimenTestResultDTO findSpecimenResultDetail(String id) {
        return getSpecimenResultResponse(id);
    }

    @Override
    @Transactional
    public SpecimenTestResultDTO registerSpecimenResult(SpecimenTestResultCreateReqDTO dto) {
        SpecimenTestResultEntity entity = new SpecimenTestResultEntity();
        entity.setSpecimenExamResultId(sequenceIdService.nextId(SequenceIdType.SPECIMEN_EXAM_RESULT_ID));
        entity.setSpecimenExamId(dto.getSpecimenExamId());
        entity.setResultItemCode(dto.getResultItemCode());
        entity.setResultSummary(dto.getResultSummary());
        entity.setUnit(dto.getUnit());
        entity.setReferenceRange(dto.getReferenceRange());
        entity.setJudgement(dto.getJudgement());
        entity.setConfirmedAt(dto.getConfirmedAt());
        entity.setResultManagerId(dto.getResultManagerId());
        entity.setResultManagerName(dto.getResultManagerName());
        entity.setStatus(normalizeStatus(dto.getStatus()));
        entity.setProgressStatus(PROGRESS_IN_PROGRESS);
        entity.setCreatedAt(LocalDateTime.now());
        SpecimenTestResultEntity savedEntity = specimenTestResultRepository.save(entity);
        return getSpecimenResultResponse(savedEntity.getSpecimenExamResultId());
    }

    @Override
    @Transactional
    public SpecimenTestResultDTO modifySpecimenResult(String id, SpecimenTestResultUpdateReqDTO dto) {
        SpecimenTestResultEntity entity = specimenTestResultRepository.findById(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Specimen result not found. id=" + id));
        if (dto.getResultItemCode() != null) {
            entity.setResultItemCode(dto.getResultItemCode());
        }
        if (dto.getResultSummary() != null) {
            entity.setResultSummary(dto.getResultSummary());
        }
        if (dto.getUnit() != null) {
            entity.setUnit(dto.getUnit());
        }
        if (dto.getReferenceRange() != null) {
            entity.setReferenceRange(dto.getReferenceRange());
        }
        if (dto.getJudgement() != null) {
            entity.setJudgement(dto.getJudgement());
        }
        if (dto.getConfirmedAt() != null) {
            entity.setConfirmedAt(dto.getConfirmedAt());
        }
        if (dto.getResultManagerId() != null) {
            entity.setResultManagerId(dto.getResultManagerId());
        }
        if (dto.getResultManagerName() != null) {
            entity.setResultManagerName(dto.getResultManagerName());
        }
        if (hasText(dto.getStatus())) {
            entity.setStatus(normalizeStatus(dto.getStatus()));
        }
        entity.setUpdatedAt(LocalDateTime.now());
        SpecimenTestResultEntity savedEntity = specimenTestResultRepository.save(entity);
        return getSpecimenResultResponse(savedEntity.getSpecimenExamResultId());
    }

    @Override
    @Transactional
    public void deleteSpecimenResult(String id) {
        SpecimenTestResultEntity entity = specimenTestResultRepository.findById(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Specimen result not found. id=" + id));
        entity.setStatus(STATUS_INACTIVE);
        specimenTestResultRepository.save(entity);
    }

    // 통합 조회

    @Override
    public List<TestResultListDTO> findTestResultList(TestResultSearchCondition condition) {
        TestResultSearchCondition searchCondition = condition != null ? condition : new TestResultSearchCondition();

        List<TestResultListDTO> mergedResults = new ArrayList<>();
        mergedResults.addAll(mapImagingResults(findImagingResultList()));
        mergedResults.addAll(mapSpecimenResults(findSpecimenResultList()));
        mergedResults.addAll(mapPathologyResults(findPathologyResultList()));
        mergedResults.addAll(mapEndoscopyResults(findEndoscopyResultList()));
        mergedResults.addAll(mapPhysiologicalResults(findPhysiologicalResultList()));

        return mergedResults.stream()
                .filter(result -> matchesIncludeInactive(result, searchCondition))
                .filter(result -> matchesEquals(result.getResultType(), searchCondition.getResultType()))
                .filter(result -> matchesEquals(result.getResultId(), searchCondition.getResultId()))
                .filter(result -> matchesContains(result.getPatientName(), searchCondition.getPatientName()))
                .filter(result -> matchesContains(result.getDetailCode(), searchCondition.getDetailCode()))
                .filter(result -> matchesContains(result.getDepartmentName(), searchCondition.getDepartmentName()))
                .filter(result -> matchesEquals(result.getStatus(), searchCondition.getStatus()))
                .filter(result -> matchesDateRange(result.getConfirmedAt(), searchCondition))
                .sorted(Comparator
                        .comparing(TestResultListDTO::getConfirmedAt, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(TestResultListDTO::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(TestResultListDTO::getResultId, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    @Override
    public TestResultDetailDTO findTestResultDetail(String resultType, String resultId) {
        String normalizedType = normalizeResultType(resultType);

        return switch (normalizedType) {
            case TYPE_IMAGING -> mapImagingDetail(findImagingResultDetail(resultId));
            case TYPE_SPECIMEN -> mapSpecimenDetail(findSpecimenResultDetail(resultId));
            case TYPE_PATHOLOGY -> mapPathologyDetail(findPathologyResultDetail(resultId));
            case TYPE_ENDOSCOPY -> mapEndoscopyDetail(findEndoscopyResultDetail(resultId));
            case TYPE_PHYSIOLOGICAL -> mapPhysiologicalDetail(findPhysiologicalResultDetail(resultId));
            default -> throw new DiagnosticResultNotFoundException("Unsupported result type. resultType=" + resultType);
        };
    }

    // 통합 수정

    @Override
    @Transactional
    public TestResultDetailDTO modifyTestResult(String resultType, String resultId, TestResultUpdateReqDTO dto) {
        String normalizedType = normalizeResultType(resultType);
        TestResultUpdateReqDTO request = dto != null ? dto : new TestResultUpdateReqDTO();
        TestResultUpdateDetailDTO detail = request.getDetail() != null ? request.getDetail() : new TestResultUpdateDetailDTO();

        return switch (normalizedType) {
            case TYPE_IMAGING -> mapImagingDetail(
                    modifyImagingResult(resultId, toImagingUpdateReq(request, detail))
            );
            case TYPE_SPECIMEN -> mapSpecimenDetail(
                    modifySpecimenResult(resultId, toSpecimenUpdateReq(request, detail))
            );
            case TYPE_PATHOLOGY -> mapPathologyDetail(
                    modifyPathologyResult(resultId, toPathologyUpdateReq(request, detail))
            );
            case TYPE_ENDOSCOPY -> mapEndoscopyDetail(
                    modifyEndoscopyResult(resultId, toEndoscopyUpdateReq(request, detail))
            );
            case TYPE_PHYSIOLOGICAL -> mapPhysiologicalDetail(
                    modifyPhysiologicalResult(resultId, toPhysiologicalUpdateReq(request, detail))
            );
            default -> throw new DiagnosticResultNotFoundException("Unsupported result type. resultType=" + resultType);
        };
    }

    @Override
    @Transactional
    public TestResultDetailDTO updateTestResultProgressStatus(String resultId, TestResultProgressStatusUpdateReqDTO dto) {
        if (dto == null) {
            throw new InvalidRequestException("Request body is required.");
        }

        String normalizedType = resolveResultTypeFromResultId(resultId);
        String targetProgressStatus = normalizeProgressStatus(dto.getProgressStatus());

        return switch (normalizedType) {
            case TYPE_IMAGING -> {
                ImagingResultEntity entity = imagingResultRepository.findById(resultId)
                        .orElseThrow(() -> new DiagnosticResultNotFoundException("Imaging result not found. id=" + resultId));
                assertProgressStatusUpdatable(entity.getProgressStatus(), targetProgressStatus);
                entity.setProgressStatus(targetProgressStatus);
                LocalDateTime completedAt = null;
                if (PROGRESS_COMPLETED.equals(targetProgressStatus)) {
                    completedAt = LocalDateTime.now();
                    entity.setCompletedAt(completedAt);
                }
                imagingResultRepository.save(entity);
                TestResultDetailDTO detail = mapImagingDetail(getImagingResultResponse(resultId));
                syncTestExecutionWhenResultCompleted(detail, normalizedType, completedAt);
                stageDiagnosticIfCompleted(detail);
                yield detail;
            }
            case TYPE_SPECIMEN -> {
                SpecimenTestResultEntity entity = specimenTestResultRepository.findById(resultId)
                        .orElseThrow(() -> new DiagnosticResultNotFoundException("Specimen result not found. id=" + resultId));
                assertProgressStatusUpdatable(entity.getProgressStatus(), targetProgressStatus);
                entity.setProgressStatus(targetProgressStatus);
                LocalDateTime completedAt = null;
                if (PROGRESS_COMPLETED.equals(targetProgressStatus)) {
                    completedAt = LocalDateTime.now();
                    entity.setCompletedAt(completedAt);
                }
                specimenTestResultRepository.save(entity);
                TestResultDetailDTO detail = mapSpecimenDetail(getSpecimenResultResponse(resultId));
                syncTestExecutionWhenResultCompleted(detail, normalizedType, completedAt);
                stageDiagnosticIfCompleted(detail);
                yield detail;
            }
            case TYPE_PATHOLOGY -> {
                PathologyResultEntity entity = pathologyResultRepository.findById(resultId)
                        .orElseThrow(() -> new DiagnosticResultNotFoundException("Pathology result not found. id=" + resultId));
                assertProgressStatusUpdatable(entity.getProgressStatus(), targetProgressStatus);
                entity.setProgressStatus(targetProgressStatus);
                LocalDateTime completedAt = null;
                if (PROGRESS_COMPLETED.equals(targetProgressStatus)) {
                    completedAt = LocalDateTime.now();
                    entity.setCompletedAt(completedAt);
                }
                pathologyResultRepository.save(entity);
                TestResultDetailDTO detail = mapPathologyDetail(getPathologyResultResponse(resultId));
                syncTestExecutionWhenResultCompleted(detail, normalizedType, completedAt);
                stageDiagnosticIfCompleted(detail);
                yield detail;
            }
            case TYPE_ENDOSCOPY -> {
                EndoscopyResultEntity entity = endoscopyResultRepository.findById(resultId)
                        .orElseThrow(() -> new DiagnosticResultNotFoundException("Endoscopy result not found. id=" + resultId));
                assertProgressStatusUpdatable(entity.getProgressStatus(), targetProgressStatus);
                entity.setProgressStatus(targetProgressStatus);
                LocalDateTime completedAt = null;
                if (PROGRESS_COMPLETED.equals(targetProgressStatus)) {
                    completedAt = LocalDateTime.now();
                    entity.setCompletedAt(completedAt);
                }
                endoscopyResultRepository.save(entity);
                TestResultDetailDTO detail = mapEndoscopyDetail(getEndoscopyResultResponse(resultId));
                syncTestExecutionWhenResultCompleted(detail, normalizedType, completedAt);
                stageDiagnosticIfCompleted(detail);
                yield detail;
            }
            case TYPE_PHYSIOLOGICAL -> {
                PhysiologicalResultEntity entity = physiologicalResultRepository.findById(resultId)
                        .orElseThrow(() -> new DiagnosticResultNotFoundException("Physiological result not found. id=" + resultId));
                assertProgressStatusUpdatable(entity.getProgressStatus(), targetProgressStatus);
                entity.setProgressStatus(targetProgressStatus);
                LocalDateTime completedAt = null;
                if (PROGRESS_COMPLETED.equals(targetProgressStatus)) {
                    completedAt = LocalDateTime.now();
                    entity.setCompletedAt(completedAt);
                }
                physiologicalResultRepository.save(entity);
                TestResultDetailDTO detail = mapPhysiologicalDetail(getPhysiologicalResultResponse(resultId));
                syncTestExecutionWhenResultCompleted(detail, normalizedType, completedAt);
                stageDiagnosticIfCompleted(detail);
                yield detail;
            }
            default -> throw new InvalidRequestException("Unsupported resultId: " + resultId);
        };
    }

    // 매핑

    private List<TestResultListDTO> mapImagingResults(List<ImagingResultDTO> source) {
        return source.stream().map(dto -> {
            TestResultListDTO result = createCommonResult(TYPE_IMAGING, dto.getImagingResultId(), dto.getImagingExamId());
            result.setTestExecutionId(dto.getTestExecutionId());
            result.setDetailCode(dto.getDetailCode());
            result.setPatientId(dto.getPatientId());
            result.setPatientName(dto.getPatientName());
            result.setDepartmentName(dto.getDepartmentName());
            result.setPerformerId(dto.getPerformerId());
            result.setPerformerName(dto.getPerformerName());
            result.setResultManagerId(dto.getResultManagerId());
            result.setResultManagerName(dto.getResultManagerName());
            result.setSummary(dto.getResultSummary());
            result.setConfirmedAt(dto.getConfirmedAt());
            result.setStatus(dto.getStatus());
            result.setProgressStatus(dto.getProgressStatus());
            result.setCreatedAt(dto.getCreatedAt());
            result.setUpdatedAt(dto.getUpdatedAt());
            result.setRevised(isRevised(dto.getProgressStatus(), dto.getUpdatedAt(), dto.getCompletedAt()));
            return result;
        }).toList();
    }

    private List<TestResultListDTO> mapSpecimenResults(List<SpecimenTestResultDTO> source) {
        return source.stream().map(dto -> {
            TestResultListDTO result = createCommonResult(TYPE_SPECIMEN, dto.getSpecimenExamResultId(), dto.getSpecimenExamId());
            result.setTestExecutionId(dto.getTestExecutionId());
            result.setDetailCode(dto.getDetailCode());
            result.setPatientId(dto.getPatientId());
            result.setPatientName(dto.getPatientName());
            result.setDepartmentName(dto.getDepartmentName());
            result.setPerformerId(dto.getPerformerId());
            result.setPerformerName(dto.getPerformerName());
            result.setResultManagerId(dto.getResultManagerId());
            result.setResultManagerName(dto.getResultManagerName());
            result.setSummary(dto.getResultSummary());
            result.setConfirmedAt(dto.getConfirmedAt());
            result.setStatus(dto.getStatus());
            result.setProgressStatus(dto.getProgressStatus());
            result.setCreatedAt(dto.getCreatedAt());
            result.setUpdatedAt(dto.getUpdatedAt());
            result.setRevised(isRevised(dto.getProgressStatus(), dto.getUpdatedAt(), dto.getCompletedAt()));
            return result;
        }).toList();
    }

    private List<TestResultListDTO> mapPathologyResults(List<PathologyResultDTO> source) {
        return source.stream().map(dto -> {
            TestResultListDTO result = createCommonResult(TYPE_PATHOLOGY, dto.getPathologyExamResultId(), dto.getPathologyExamId());
            result.setTestExecutionId(dto.getTestExecutionId());
            result.setDetailCode(dto.getDetailCode());
            result.setPatientId(dto.getPatientId());
            result.setPatientName(dto.getPatientName());
            result.setDepartmentName(dto.getDepartmentName());
            result.setTissueStatus(dto.getTissueStatus());
            result.setCollectionMethod(dto.getCollectionMethod());
            result.setTissueSite(dto.getTissueSite());
            result.setTissueType(dto.getTissueType());
            result.setCollectedAt(dto.getCollectedAt());
            result.setReexamYn(dto.getReexamYn());
            result.setPerformerId(dto.getPerformerId());
            result.setPerformerName(dto.getPerformerName());
            result.setResultManagerId(dto.getResultManagerId());
            result.setResultManagerName(dto.getResultManagerName());
            result.setSummary(dto.getResultSummary());
            result.setConfirmedAt(dto.getConfirmedAt());
            result.setStatus(dto.getStatus());
            result.setProgressStatus(dto.getProgressStatus());
            result.setCreatedAt(dto.getCreatedAt());
            result.setUpdatedAt(dto.getUpdatedAt());
            result.setRevised(isRevised(dto.getProgressStatus(), dto.getUpdatedAt(), dto.getCompletedAt()));
            return result;
        }).toList();
    }

    private List<TestResultListDTO> mapEndoscopyResults(List<EndoscopyResultDTO> source) {
        return source.stream().map(dto -> {
            TestResultListDTO result = createCommonResult(TYPE_ENDOSCOPY, dto.getEndoscopyResultId(), dto.getEndoscopyExamId());
            result.setTestExecutionId(dto.getTestExecutionId());
            result.setDetailCode(dto.getDetailCode());
            result.setPatientId(dto.getPatientId());
            result.setPatientName(dto.getPatientName());
            result.setDepartmentName(dto.getDepartmentName());
            result.setPerformerId(dto.getPerformerId());
            result.setPerformerName(dto.getPerformerName());
            result.setResultManagerId(dto.getResultManagerId());
            result.setResultManagerName(dto.getResultManagerName());
            result.setSummary(dto.getResultSummary());
            result.setConfirmedAt(dto.getConfirmedAt());
            result.setStatus(dto.getStatus());
            result.setProgressStatus(dto.getProgressStatus());
            result.setCreatedAt(dto.getCreatedAt());
            result.setUpdatedAt(dto.getUpdatedAt());
            result.setRevised(isRevised(dto.getProgressStatus(), dto.getUpdatedAt(), dto.getCompletedAt()));
            return result;
        }).toList();
    }

    private List<TestResultListDTO> mapPhysiologicalResults(List<PhysiologicalResultDTO> source) {
        return source.stream().map(dto -> {
            TestResultListDTO result = createCommonResult(TYPE_PHYSIOLOGICAL, dto.getPhysiologicalExamResultId(), dto.getPhysiologicalExamId());
            result.setTestExecutionId(dto.getTestExecutionId());
            result.setDetailCode(dto.getDetailCode());
            result.setPatientId(dto.getPatientId());
            result.setPatientName(dto.getPatientName());
            result.setDepartmentName(dto.getDepartmentName());
            result.setExamEquipmentId(dto.getExamEquipmentId());
            result.setRawData(dto.getRawData());
            result.setReportDocId(dto.getReportDocId());
            result.setPerformerId(dto.getPerformerId());
            result.setPerformerName(dto.getPerformerName());
            result.setResultManagerId(dto.getResultManagerId());
            result.setResultManagerName(dto.getResultManagerName());
            result.setSummary(dto.getResultSummary());
            result.setConfirmedAt(dto.getConfirmedAt());
            result.setStatus(dto.getStatus());
            result.setProgressStatus(dto.getProgressStatus());
            result.setCreatedAt(dto.getCreatedAt());
            result.setUpdatedAt(dto.getUpdatedAt());
            result.setRevised(isRevised(dto.getProgressStatus(), dto.getUpdatedAt(), dto.getCompletedAt()));
            return result;
        }).toList();
    }

    private TestResultDetailDTO mapImagingDetail(ImagingResultDTO dto) {
        TestResultDetailDTO result = createCommonDetail(TYPE_IMAGING, dto.getImagingResultId(), dto.getImagingExamId());
        result.setTestExecutionId(dto.getTestExecutionId());
        result.setDetailCode(dto.getDetailCode());
        result.setPatientId(dto.getPatientId());
        result.setPatientName(dto.getPatientName());
        result.setDepartmentName(dto.getDepartmentName());
        result.setPerformerId(dto.getPerformerId());
        result.setPerformerName(dto.getPerformerName());
        result.setResultManagerId(dto.getResultManagerId());
        result.setResultManagerName(dto.getResultManagerName());
        result.setSummary(dto.getResultSummary());
        result.setConfirmedAt(dto.getConfirmedAt());
        result.setStatus(dto.getStatus());
        result.setProgressStatus(dto.getProgressStatus());
        result.setCreatedAt(dto.getCreatedAt());
        result.setUpdatedAt(dto.getUpdatedAt());
        result.setRevised(isRevised(dto.getProgressStatus(), dto.getUpdatedAt(), dto.getCompletedAt()));

        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("readingDetail", dto.getReadingDetail());
        result.setDetail(detail);
        return result;
    }

    private TestResultDetailDTO mapSpecimenDetail(SpecimenTestResultDTO dto) {
        TestResultDetailDTO result = createCommonDetail(TYPE_SPECIMEN, dto.getSpecimenExamResultId(), dto.getSpecimenExamId());
        result.setTestExecutionId(dto.getTestExecutionId());
        result.setDetailCode(dto.getDetailCode());
        result.setPatientId(dto.getPatientId());
        result.setPatientName(dto.getPatientName());
        result.setDepartmentName(dto.getDepartmentName());
        result.setPerformerId(dto.getPerformerId());
        result.setPerformerName(dto.getPerformerName());
        result.setResultManagerId(dto.getResultManagerId());
        result.setResultManagerName(dto.getResultManagerName());
        result.setSummary(dto.getResultSummary());
        result.setConfirmedAt(dto.getConfirmedAt());
        result.setStatus(dto.getStatus());
        result.setProgressStatus(dto.getProgressStatus());
        result.setCreatedAt(dto.getCreatedAt());
        result.setUpdatedAt(dto.getUpdatedAt());
        result.setRevised(isRevised(dto.getProgressStatus(), dto.getUpdatedAt(), dto.getCompletedAt()));

        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("resultItemCode", dto.getResultItemCode());
        detail.put("unit", dto.getUnit());
        detail.put("referenceRange", dto.getReferenceRange());
        detail.put("judgement", dto.getJudgement());
        result.setDetail(detail);
        return result;
    }

    private TestResultDetailDTO mapPathologyDetail(PathologyResultDTO dto) {
        TestResultDetailDTO result = createCommonDetail(TYPE_PATHOLOGY, dto.getPathologyExamResultId(), dto.getPathologyExamId());
        result.setTestExecutionId(dto.getTestExecutionId());
        result.setDetailCode(dto.getDetailCode());
        result.setPatientId(dto.getPatientId());
        result.setPatientName(dto.getPatientName());
        result.setDepartmentName(dto.getDepartmentName());
        result.setPerformerId(dto.getPerformerId());
        result.setPerformerName(dto.getPerformerName());
        result.setResultManagerId(dto.getResultManagerId());
        result.setResultManagerName(dto.getResultManagerName());
        result.setSummary(dto.getResultSummary());
        result.setConfirmedAt(dto.getConfirmedAt());
        result.setStatus(dto.getStatus());
        result.setProgressStatus(dto.getProgressStatus());
        result.setCreatedAt(dto.getCreatedAt());
        result.setUpdatedAt(dto.getUpdatedAt());
        result.setRevised(isRevised(dto.getProgressStatus(), dto.getUpdatedAt(), dto.getCompletedAt()));

        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("tissueStatus", dto.getTissueStatus());
        detail.put("collectionMethod", dto.getCollectionMethod());
        detail.put("tissueSite", dto.getTissueSite());
        detail.put("tissueType", dto.getTissueType());
        detail.put("collectedAt", dto.getCollectedAt());
        detail.put("reexamYn", dto.getReexamYn());
        detail.put("judgedAt", dto.getJudgedAt());
        detail.put("readerId", dto.getReaderId());
        detail.put("diagnosisName", dto.getDiagnosisName());
        result.setDetail(detail);
        return result;
    }

    private TestResultDetailDTO mapEndoscopyDetail(EndoscopyResultDTO dto) {
        TestResultDetailDTO result = createCommonDetail(TYPE_ENDOSCOPY, dto.getEndoscopyResultId(), dto.getEndoscopyExamId());
        result.setTestExecutionId(dto.getTestExecutionId());
        result.setDetailCode(dto.getDetailCode());
        result.setPatientId(dto.getPatientId());
        result.setPatientName(dto.getPatientName());
        result.setDepartmentName(dto.getDepartmentName());
        result.setPerformerId(dto.getPerformerId());
        result.setPerformerName(dto.getPerformerName());
        result.setResultManagerId(dto.getResultManagerId());
        result.setResultManagerName(dto.getResultManagerName());
        result.setSummary(dto.getResultSummary());
        result.setConfirmedAt(dto.getConfirmedAt());
        result.setStatus(dto.getStatus());
        result.setProgressStatus(dto.getProgressStatus());
        result.setCreatedAt(dto.getCreatedAt());
        result.setUpdatedAt(dto.getUpdatedAt());
        result.setRevised(isRevised(dto.getProgressStatus(), dto.getUpdatedAt(), dto.getCompletedAt()));

        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("biopsyYn", dto.getBiopsyYn());
        detail.put("readerId", dto.getReaderId());
        result.setDetail(detail);
        return result;
    }

    private TestResultDetailDTO mapPhysiologicalDetail(PhysiologicalResultDTO dto) {
        TestResultDetailDTO result = createCommonDetail(TYPE_PHYSIOLOGICAL, dto.getPhysiologicalExamResultId(), dto.getPhysiologicalExamId());
        result.setTestExecutionId(dto.getTestExecutionId());
        result.setDetailCode(dto.getDetailCode());
        result.setPatientId(dto.getPatientId());
        result.setPatientName(dto.getPatientName());
        result.setDepartmentName(dto.getDepartmentName());
        result.setPerformerId(dto.getPerformerId());
        result.setPerformerName(dto.getPerformerName());
        result.setResultManagerId(dto.getResultManagerId());
        result.setResultManagerName(dto.getResultManagerName());
        result.setSummary(dto.getResultSummary());
        result.setConfirmedAt(dto.getConfirmedAt());
        result.setStatus(dto.getStatus());
        result.setProgressStatus(dto.getProgressStatus());
        result.setCreatedAt(dto.getCreatedAt());
        result.setUpdatedAt(dto.getUpdatedAt());
        result.setRevised(isRevised(dto.getProgressStatus(), dto.getUpdatedAt(), dto.getCompletedAt()));

        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("examEquipmentId", dto.getExamEquipmentId());
        detail.put("rawData", dto.getRawData());
        detail.put("reportDocId", dto.getReportDocId());
        detail.put("report", dto.getReport());
        detail.put("measuredItemCode", dto.getMeasuredItemCode());
        result.setDetail(detail);
        return result;
    }

    private TestResultListDTO createCommonResult(String resultType, String resultId, String examId) {
        TestResultListDTO result = new TestResultListDTO();
        result.setResultType(resultType);
        result.setResultTypeName(toResultTypeName(resultType));
        result.setResultId(resultId);
        result.setExamId(examId);
        return result;
    }

    private ImagingResultUpdateReqDTO toImagingUpdateReq(TestResultUpdateReqDTO request, TestResultUpdateDetailDTO detail) {
        ImagingResultUpdateReqDTO dto = new ImagingResultUpdateReqDTO();
        dto.setResultSummary(detail.getResultSummary());
        dto.setReadingDetail(detail.getReadingDetail());
        dto.setConfirmedAt(request.getConfirmedAt());
        dto.setResultManagerId(request.getResultManagerId());
        dto.setResultManagerName(request.getResultManagerName());
        dto.setStatus(request.getStatus());
        return dto;
    }

    private SpecimenTestResultUpdateReqDTO toSpecimenUpdateReq(TestResultUpdateReqDTO request, TestResultUpdateDetailDTO detail) {
        SpecimenTestResultUpdateReqDTO dto = new SpecimenTestResultUpdateReqDTO();
        dto.setResultItemCode(detail.getResultItemCode());
        dto.setResultSummary(detail.getResultSummary());
        dto.setUnit(detail.getUnit());
        dto.setReferenceRange(detail.getReferenceRange());
        dto.setJudgement(detail.getJudgement());
        dto.setConfirmedAt(request.getConfirmedAt());
        dto.setResultManagerId(request.getResultManagerId());
        dto.setResultManagerName(request.getResultManagerName());
        dto.setStatus(request.getStatus());
        return dto;
    }

    private PathologyResultUpdateReqDTO toPathologyUpdateReq(TestResultUpdateReqDTO request, TestResultUpdateDetailDTO detail) {
        PathologyResultUpdateReqDTO dto = new PathologyResultUpdateReqDTO();
        dto.setResultSummary(detail.getResultSummary());
        dto.setJudgedAt(detail.getJudgedAt());
        dto.setConfirmedAt(request.getConfirmedAt());
        dto.setResultManagerId(request.getResultManagerId());
        dto.setResultManagerName(request.getResultManagerName());
        dto.setReaderId(detail.getReaderId());
        dto.setDiagnosisName(detail.getDiagnosisName());
        dto.setStatus(request.getStatus());
        return dto;
    }

    private EndoscopyResultUpdateReqDTO toEndoscopyUpdateReq(TestResultUpdateReqDTO request, TestResultUpdateDetailDTO detail) {
        EndoscopyResultUpdateReqDTO dto = new EndoscopyResultUpdateReqDTO();
        dto.setResultSummary(detail.getResultSummary());
        dto.setBiopsyYn(detail.getBiopsyYn());
        dto.setConfirmedAt(request.getConfirmedAt());
        dto.setResultManagerId(request.getResultManagerId());
        dto.setResultManagerName(request.getResultManagerName());
        dto.setReaderId(detail.getReaderId());
        dto.setStatus(request.getStatus());
        return dto;
    }

    private PhysiologicalResultUpdateReqDTO toPhysiologicalUpdateReq(TestResultUpdateReqDTO request, TestResultUpdateDetailDTO detail) {
        PhysiologicalResultUpdateReqDTO dto = new PhysiologicalResultUpdateReqDTO();
        dto.setResultSummary(detail.getResultSummary());
        dto.setReport(detail.getReport());
        dto.setMeasuredItemCode(detail.getMeasuredItemCode());
        dto.setConfirmedAt(request.getConfirmedAt());
        dto.setResultManagerId(request.getResultManagerId());
        dto.setResultManagerName(request.getResultManagerName());
        dto.setStatus(request.getStatus());
        return dto;
    }

    private TestResultDetailDTO createCommonDetail(String resultType, String resultId, String examId) {
        TestResultDetailDTO result = new TestResultDetailDTO();
        result.setResultType(resultType);
        result.setResultTypeName(toResultTypeName(resultType));
        result.setResultId(resultId);
        result.setExamId(examId);
        return result;
    }

    // helper / 유틸

    private ImagingResultDTO getImagingResultResponse(String id) {
        return imagingResultRepository.findImagingResultResponseDetail(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Imaging result not found. id=" + id));
    }

    private EndoscopyResultDTO getEndoscopyResultResponse(String id) {
        return endoscopyResultRepository.findEndoscopyResultResponseDetail(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Endoscopy result not found. id=" + id));
    }

    private PathologyResultDTO getPathologyResultResponse(String id) {
        return pathologyResultRepository.findPathologyResultResponseDetail(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Pathology result not found. id=" + id));
    }

    private PhysiologicalResultDTO getPhysiologicalResultResponse(String id) {
        return physiologicalResultRepository.findPhysiologicalResultResponseDetail(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Physiological result not found. id=" + id));
    }

    private SpecimenTestResultDTO getSpecimenResultResponse(String id) {
        return specimenTestResultRepository.findSpecimenResultResponseDetail(id)
                .orElseThrow(() -> new DiagnosticResultNotFoundException("Specimen result not found. id=" + id));
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

    private String normalizeProgressStatus(String progressStatus) {
        if (!hasText(progressStatus)) {
            throw new InvalidRequestException("progressStatus is required.");
        }

        String normalized = progressStatus.trim().toUpperCase();
        if (!PROGRESS_IN_PROGRESS.equals(normalized) && !PROGRESS_COMPLETED.equals(normalized)) {
            throw new InvalidRequestException("Invalid progressStatus. Allowed values: IN_PROGRESS, COMPLETED");
        }
        return normalized;
    }

    private void assertProgressStatusUpdatable(String currentProgressStatus, String nextProgressStatus) {
        String current = hasText(currentProgressStatus) ? currentProgressStatus.trim().toUpperCase() : PROGRESS_IN_PROGRESS;
        if (PROGRESS_COMPLETED.equals(current)) {
            throw new InvalidRequestException("COMPLETED status cannot be changed.");
        }
        if (current.equals(nextProgressStatus)) {
            throw new InvalidRequestException("progressStatus is already " + nextProgressStatus + ".");
        }
    }

    private String resolveResultTypeFromResultId(String resultId) {
        if (!hasText(resultId)) {
            throw new InvalidRequestException("resultId is required.");
        }

        String normalizedId = resultId.trim().toUpperCase();
        if (normalizedId.startsWith("IMG_R")) {
            return TYPE_IMAGING;
        }
        if (normalizedId.startsWith("SPC_R")) {
            return TYPE_SPECIMEN;
        }
        if (normalizedId.startsWith("PTH_R")) {
            return TYPE_PATHOLOGY;
        }
        if (normalizedId.startsWith("END_R")) {
            return TYPE_ENDOSCOPY;
        }
        if (normalizedId.startsWith("PHY_R")) {
            return TYPE_PHYSIOLOGICAL;
        }

        List<String> matchedTypes = new ArrayList<>();
        if (imagingResultRepository.existsById(resultId)) {
            matchedTypes.add(TYPE_IMAGING);
        }
        if (specimenTestResultRepository.existsById(resultId)) {
            matchedTypes.add(TYPE_SPECIMEN);
        }
        if (pathologyResultRepository.existsById(resultId)) {
            matchedTypes.add(TYPE_PATHOLOGY);
        }
        if (endoscopyResultRepository.existsById(resultId)) {
            matchedTypes.add(TYPE_ENDOSCOPY);
        }
        if (physiologicalResultRepository.existsById(resultId)) {
            matchedTypes.add(TYPE_PHYSIOLOGICAL);
        }

        if (matchedTypes.isEmpty()) {
            throw new DiagnosticResultNotFoundException("Result not found. resultId=" + resultId);
        }
        if (matchedTypes.size() > 1) {
            throw new InvalidRequestException("Ambiguous resultId. Matched types=" + String.join(", ", matchedTypes));
        }
        return matchedTypes.get(0);
    }

    private boolean matchesIncludeInactive(TestResultListDTO result, TestResultSearchCondition condition) {
        boolean includeInactive = Boolean.TRUE.equals(condition.getIncludeInactive());
        return includeInactive || !STATUS_INACTIVE.equalsIgnoreCase(trimToEmpty(result.getStatus()));
    }

    private boolean matchesEquals(String source, String keyword) {
        if (!hasText(keyword)) {
            return true;
        }
        return trimToEmpty(source).equalsIgnoreCase(keyword.trim());
    }

    private boolean matchesContains(String source, String keyword) {
        if (!hasText(keyword)) {
            return true;
        }
        return trimToEmpty(source).toLowerCase().contains(keyword.trim().toLowerCase());
    }

    private boolean matchesDateRange(LocalDateTime confirmedAt, TestResultSearchCondition condition) {
        if (condition.getStartDate() == null && condition.getEndDate() == null) {
            return true;
        }
        if (confirmedAt == null) {
            return false;
        }

        LocalDateTime startDateTime = condition.getStartDate() != null ? condition.getStartDate().atStartOfDay() : null;
        LocalDateTime endDateTime = condition.getEndDate() != null ? condition.getEndDate().atTime(LocalTime.MAX) : null;

        if (startDateTime != null && confirmedAt.isBefore(startDateTime)) {
            return false;
        }
        if (endDateTime != null && confirmedAt.isAfter(endDateTime)) {
            return false;
        }
        return true;
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private boolean isRevised(String progressStatus, LocalDateTime updatedAt, LocalDateTime completedAt) {
        return PROGRESS_COMPLETED.equalsIgnoreCase(trimToEmpty(progressStatus))
                && updatedAt != null
                && completedAt != null
                && updatedAt.isAfter(completedAt);
    }

    private String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }

    private String normalizeResultType(String value) {
        return hasText(value) ? value.trim().toUpperCase() : "";
    }

    private String toResultTypeName(String resultType) {
        return switch (resultType) {
            case TYPE_IMAGING -> "영상검사";
            case TYPE_SPECIMEN -> "검체검사";
            case TYPE_PATHOLOGY -> "병리검사";
            case TYPE_ENDOSCOPY -> "내시경검사";
            case TYPE_PHYSIOLOGICAL -> "생리기능검사";
            default -> resultType;
        };
    }

    private void syncTestExecutionWhenResultCompleted(TestResultDetailDTO detail, String resultType, LocalDateTime resultCompletedAt) {
        if (detail == null || !PROGRESS_COMPLETED.equalsIgnoreCase(trimToEmpty(detail.getProgressStatus()))) {
            return;
        }

        String testExecutionId = trimToEmpty(detail.getTestExecutionId());
        if (testExecutionId.isEmpty()) {
            log.warn("Skip testExecution completion sync: missing testExecutionId. resultId={}, resultType={}",
                    detail.getResultId(), resultType);
            return;
        }

        Optional<TestExecutionEntity> executionOptional = testExecutionRepository.findById(testExecutionId);
        if (executionOptional.isEmpty()) {
            log.warn("Skip testExecution completion sync: execution not found. resultId={}, resultType={}, testExecutionId={}",
                    detail.getResultId(), resultType, testExecutionId);
            return;
        }

        TestExecutionEntity executionEntity = executionOptional.get();
        executionEntity.setProgressStatus(PROGRESS_COMPLETED);
        if (executionEntity.getCompletedAt() == null) {
            executionEntity.setCompletedAt(resultCompletedAt != null ? resultCompletedAt : LocalDateTime.now());
        }
        executionEntity.setUpdatedAt(LocalDateTime.now());
        testExecutionRepository.save(executionEntity);
    }

    private void stageDiagnosticIfCompleted(TestResultDetailDTO detail) {
        if (detail == null || !PROGRESS_COMPLETED.equalsIgnoreCase(trimToEmpty(detail.getProgressStatus()))) {
            return;
        }
        claimsCompletionStageService.stageDiagnosticCompleted(
                detail.getPatientId(),
                detail.getResultType(),
                detail.getResultId()
        );
        downstreamOutcomeEventPublisher.publishDiagnosticTestResultOutcome(detail);
    }

}
