package com.app.medical_support.common.stafflookup.service;

import com.app.medical_support.common.exception.InvalidRequestException;
import com.app.medical_support.common.stafflookup.dto.StaffExamType;
import com.app.medical_support.common.stafflookup.dto.StaffLookupRole;
import com.app.medical_support.common.stafflookup.dto.StaffOptionDTO;
import com.app.medical_support.common.stafflookup.mapper.StaffLookupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffLookupServiceImpl implements StaffLookupService {

    private static final int DEFAULT_LIMIT = 20;
    private static final int MAX_LIMIT = 100;

    private final StaffLookupMapper staffLookupMapper;

    @Override
    public List<StaffOptionDTO> findStaffOptions(String role, String examType, String keyword, Integer limit) {
        StaffLookupRole lookupRole = StaffLookupRole.from(role);
        LookupRule rule = resolveRule(lookupRole, examType);
        String normalizedKeyword = normalizeKeyword(keyword);
        int normalizedLimit = normalizeLimit(limit);

        return staffLookupMapper.findStaffOptions(
                rule.staffIdPrefix(),
                rule.dutyCodes(),
                normalizedKeyword,
                normalizedLimit
        );
    }

    private LookupRule resolveRule(StaffLookupRole role, String examType) {
        return switch (role) {
            case NURSE -> new LookupRule("NUR-", List.of("NURSING"));
            case STF_ONLY -> new LookupRule("STF-", List.of("GENERAL"));
            case EXAM_RECEPTION_MANAGER -> new LookupRule("STF-", List.of("EXAM_RECEPTION_COORDINATOR"));
            case EXAM_RESULT_MANAGER -> new LookupRule("STF-", List.of("TEST_RESULT_MANAGER"));
            case EXAM_PERFORMER -> resolvePerformerRule(examType);
        };
    }

    private LookupRule resolvePerformerRule(String examType) {
        StaffExamType parsedExamType = StaffExamType.from(examType);
        return switch (parsedExamType) {
            case IMAGING -> new LookupRule("STF-", List.of("RADIOLOGIC_TECHNOLOGIST"));
            case PATHOLOGY -> new LookupRule("STF-", List.of("PATHOLOGY_COORDINATOR"));
            case ENDOSCOPY -> new LookupRule("STF-", List.of("ENDOSCOPY_COORDINATOR"));
            case PHYSIOLOGICAL -> new LookupRule("STF-", List.of("PHYSIOLOGICAL_TEST_COORDINATOR"));
            case SPECIMEN -> new LookupRule("STF-", List.of("CLINICAL_LAB_TECH"));
        };
    }

    private int normalizeLimit(Integer limit) {
        if (limit == null) {
            return DEFAULT_LIMIT;
        }
        if (limit < 1 || limit > MAX_LIMIT) {
            throw new InvalidRequestException("limit must be between 1 and " + MAX_LIMIT + ".");
        }
        return limit;
    }

    private String normalizeKeyword(String keyword) {
        if (keyword == null) {
            return null;
        }
        String trimmed = keyword.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private record LookupRule(String staffIdPrefix, List<String> dutyCodes) {
    }
}
