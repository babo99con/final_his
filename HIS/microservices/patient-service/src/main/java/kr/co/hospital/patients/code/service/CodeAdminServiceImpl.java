package kr.co.hospital.patients.code.service;

import kr.co.hospital.patients.code.dto.CodeDetailReq;
import kr.co.hospital.patients.code.dto.CodeDetailRes;
import kr.co.hospital.patients.code.dto.CodeGroupReq;
import kr.co.hospital.patients.code.dto.CodeGroupRes;
import kr.co.hospital.patients.code.entity.CodeEntity;
import kr.co.hospital.patients.code.entity.CodeGroupEntity;
import kr.co.hospital.patients.code.repository.CodeGroupRepository;
import kr.co.hospital.patients.code.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeAdminServiceImpl implements CodeAdminService {

    private final CodeGroupRepository codeGroupRepository;
    private final CodeRepository codeRepository;

    @Override
    public List<CodeGroupRes> findGroups(boolean activeOnly) {
        List<CodeGroupEntity> entities = activeOnly
                ? codeGroupRepository.findAllByIsActiveTrueOrderByGroupCodeAsc()
                : codeGroupRepository.findAllByOrderByGroupCodeAsc();
        return entities.stream().map(this::toGroupRes).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CodeGroupRes createGroup(CodeGroupReq req) {
        throw new IllegalArgumentException("Creating new code groups is not allowed.");
    }

    @Override
    @Transactional
    public CodeGroupRes updateGroup(String groupCode, CodeGroupReq req) {
        CodeGroupEntity entity = getGroupOrThrow(groupCode);
        validateGroupEditable(entity);

        entity.setGroupName(requireText(req.getGroupName(), "groupName"));
        return toGroupRes(entity);
    }

    @Override
    @Transactional
    public void deactivateGroup(String groupCode) {
        CodeGroupEntity entity = getGroupOrThrow(groupCode);
        validateGroupEditable(entity);

        entity.setIsActive(Boolean.FALSE);
        codeRepository.deactivateAllByGroupCode(entity.getGroupCode());
    }

    @Override
    @Transactional
    public void activateGroup(String groupCode) {
        CodeGroupEntity entity = getGroupOrThrow(groupCode);
        validateGroupEditable(entity);

        entity.setIsActive(Boolean.TRUE);
        codeRepository.activateAllByGroupCode(entity.getGroupCode());
    }

    @Override
    public List<CodeDetailRes> findDetails(String groupCode, boolean activeOnly) {
        List<CodeEntity> entities;
        if (groupCode == null || groupCode.isBlank()) {
            entities = activeOnly
                    ? codeRepository.findAllByIsActiveTrueOrderByGroupCodeAscSortOrderAscCodeAsc()
                    : codeRepository.findAllByOrderByGroupCodeAscSortOrderAscCodeAsc();
        } else {
            String normalized = normalizeKey(groupCode, "groupCode");
            entities = activeOnly
                    ? codeRepository.findAllByGroupCodeAndIsActiveTrueOrderBySortOrderAscCodeAsc(normalized)
                    : codeRepository.findAllByGroupCodeOrderBySortOrderAscCodeAsc(normalized);
        }

        return entities.stream().map(this::toDetailRes).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CodeDetailRes createDetail(CodeDetailReq req) {
        String groupCode = normalizeKey(req.getGroupCode(), "groupCode");
        String code = normalizeKey(req.getCode(), "code");

        CodeGroupEntity group = getGroupOrThrow(groupCode);
        validateGroupEditable(group);
        validateGroupActive(group);

        if (codeRepository.findByGroupCodeAndCode(groupCode, code).isPresent()) {
            throw new IllegalArgumentException("code already exists: " + groupCode + "/" + code);
        }

        CodeEntity entity = new CodeEntity();
        entity.setGroupCode(groupCode);
        entity.setCode(code);
        entity.setName(requireText(req.getName(), "name"));
        entity.setSortOrder(req.getSortOrder() == null ? 1 : req.getSortOrder());
        entity.setNote(trimToNull(req.getNote()));
        entity.setIsActive(req.getIsActive() == null ? Boolean.TRUE : req.getIsActive());

        return toDetailRes(codeRepository.save(entity));
    }

    @Override
    @Transactional
    public CodeDetailRes updateDetail(String groupCode, String code, CodeDetailReq req) {
        String normalizedGroupCode = normalizeKey(groupCode, "groupCode");
        CodeGroupEntity group = getGroupOrThrow(normalizedGroupCode);
        validateGroupEditable(group);

        CodeEntity entity = codeRepository.findByGroupCodeAndCode(
                        normalizedGroupCode,
                        normalizeKey(code, "code")
                )
                .orElseThrow(() -> new IllegalArgumentException("code not found: " + groupCode + "/" + code));

        entity.setName(requireText(req.getName(), "name"));
        if (req.getSortOrder() != null) {
            entity.setSortOrder(req.getSortOrder());
        }
        entity.setNote(trimToNull(req.getNote()));
        if (req.getIsActive() != null) {
            entity.setIsActive(req.getIsActive());
        }

        return toDetailRes(entity);
    }

    @Override
    @Transactional
    public void deactivateDetail(String groupCode, String code) {
        String normalizedGroupCode = normalizeKey(groupCode, "groupCode");
        CodeGroupEntity group = getGroupOrThrow(normalizedGroupCode);
        validateGroupEditable(group);

        CodeEntity entity = codeRepository.findByGroupCodeAndCode(
                        normalizedGroupCode,
                        normalizeKey(code, "code")
                )
                .orElseThrow(() -> new IllegalArgumentException("code not found: " + groupCode + "/" + code));

        entity.setIsActive(Boolean.FALSE);
    }

    @Override
    @Transactional
    public void activateDetail(String groupCode, String code) {
        String normalizedGroupCode = normalizeKey(groupCode, "groupCode");
        CodeGroupEntity group = getGroupOrThrow(normalizedGroupCode);
        validateGroupEditable(group);
        validateGroupActive(group);

        CodeEntity entity = codeRepository.findByGroupCodeAndCode(
                        normalizedGroupCode,
                        normalizeKey(code, "code")
                )
                .orElseThrow(() -> new IllegalArgumentException("code not found: " + groupCode + "/" + code));

        entity.setIsActive(Boolean.TRUE);
    }

    private CodeGroupEntity getGroupOrThrow(String groupCode) {
        return codeGroupRepository.findById(normalizeKey(groupCode, "groupCode"))
                .orElseThrow(() -> new IllegalArgumentException("groupCode not found: " + groupCode));
    }

    private void validateGroupEditable(CodeGroupEntity group) {
        if (!Boolean.TRUE.equals(group.getEditableYn())) {
            throw new IllegalArgumentException("수정 불가 코드그룹입니다. groupCode=" + group.getGroupCode());
        }
    }

    private void validateGroupActive(CodeGroupEntity group) {
        if (!Boolean.TRUE.equals(group.getIsActive())) {
            throw new IllegalArgumentException("활성상태가 아닌 코드그룹입니다. groupCode=" + group.getGroupCode());
        }
    }

    private CodeGroupRes toGroupRes(CodeGroupEntity entity) {
        CodeGroupRes res = new CodeGroupRes();
        res.setGroupCode(entity.getGroupCode());
        res.setGroupName(entity.getGroupName());
        res.setEditableYn(entity.getEditableYn());
        res.setIsActive(entity.getIsActive());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        return res;
    }

    private CodeDetailRes toDetailRes(CodeEntity entity) {
        CodeDetailRes res = new CodeDetailRes();
        res.setGroupCode(entity.getGroupCode());
        res.setCode(entity.getCode());
        res.setName(entity.getName());
        res.setSortOrder(entity.getSortOrder());
        res.setIsActive(entity.getIsActive());
        res.setNote(entity.getNote());
        res.setCreatedAt(entity.getCreatedAt());
        res.setUpdatedAt(entity.getUpdatedAt());
        return res;
    }

    private String normalizeKey(String value, String fieldName) {
        return requireText(value, fieldName).toUpperCase(Locale.ROOT);
    }

    private String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value.trim();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}