package kr.co.hospital.patients.code.service;

import kr.co.hospital.patients.code.dto.ConsentTypeReq;
import kr.co.hospital.patients.code.dto.ConsentTypeRes;
import kr.co.hospital.patients.code.entity.CodeEntity;
import kr.co.hospital.patients.code.repository.CodeGroupRepository;
import kr.co.hospital.patients.code.repository.CodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConsentTypeServiceImpl implements ConsentTypeService {

    private static final String GROUP_CODE = "CONSENT_TYPE";

    private final CodeRepository codeRepository;
    private final CodeGroupRepository codeGroupRepository;

    @Override
    public List<ConsentTypeRes> findActive() {
        boolean groupActive = codeGroupRepository.countByGroupCodeAndIsActiveTrue(GROUP_CODE) > 0;
        if (!groupActive) {
            return Collections.emptyList();
        }
        return codeRepository
                .findAllByGroupCodeAndIsActiveTrueOrderBySortOrderAscCodeAsc(GROUP_CODE)
                .stream()
                .map(this::toRes)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsentTypeRes> findAll() {
        return codeRepository
                .findAllByGroupCodeOrderBySortOrderAscCodeAsc(GROUP_CODE)
                .stream()
                .map(this::toRes)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ConsentTypeRes create(ConsentTypeReq req) {
        if (req == null || req.getCode() == null || req.getCode().isBlank()) {
            throw new IllegalArgumentException("code is required");
        }
        if (req.getName() == null || req.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        Optional<CodeEntity> exists = codeRepository.findByGroupCodeAndCode(
                GROUP_CODE, req.getCode().trim()
        );
        if (exists.isPresent()) {
            throw new IllegalArgumentException("code already exists");
        }

        CodeEntity entity = new CodeEntity();
        entity.setGroupCode(GROUP_CODE);
        entity.setCode(req.getCode().trim());
        entity.setName(req.getName().trim());
        entity.setSortOrder(req.getSortOrder() == null ? 999 : req.getSortOrder());
        entity.setIsActive(req.getIsActive() == null ? Boolean.TRUE : req.getIsActive());

        CodeEntity saved = codeRepository.save(entity);
        return toRes(saved);
    }

    @Override
    @Transactional
    public ConsentTypeRes update(String code, ConsentTypeReq req) {
        CodeEntity entity = codeRepository.findByGroupCodeAndCode(GROUP_CODE, code)
                .orElseThrow(() -> new IllegalArgumentException("consent type not found"));

        if (req.getCode() != null && !req.getCode().isBlank()) {
            String nextCode = req.getCode().trim();
            if (!nextCode.equals(entity.getCode())) {
                codeRepository.findByGroupCodeAndCode(GROUP_CODE, nextCode)
                        .ifPresent(e -> { throw new IllegalArgumentException("code already exists"); });
                entity.setCode(nextCode);
            }
        }
        if (req.getName() != null && !req.getName().isBlank()) {
            entity.setName(req.getName().trim());
        }
        if (req.getSortOrder() != null) entity.setSortOrder(req.getSortOrder());
        if (req.getIsActive() != null) entity.setIsActive(req.getIsActive());

        return toRes(entity);
    }

    @Override
    @Transactional
    public void deactivate(String code) {
        CodeEntity entity = codeRepository.findByGroupCodeAndCode(GROUP_CODE, code)
                .orElseThrow(() -> new IllegalArgumentException("consent type not found"));
        entity.setIsActive(Boolean.FALSE);
    }

    private ConsentTypeRes toRes(CodeEntity entity) {
        ConsentTypeRes res = new ConsentTypeRes();
        res.setCode(entity.getCode());
        res.setName(entity.getName());
        res.setSortOrder(entity.getSortOrder());
        res.setIsActive(entity.getIsActive());
        return res;
    }
}
