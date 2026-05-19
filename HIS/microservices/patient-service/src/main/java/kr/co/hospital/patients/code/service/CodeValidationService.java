package kr.co.hospital.patients.code.service;

import kr.co.hospital.patients.code.repository.CodeRepository;
import kr.co.hospital.patients.code.repository.CodeGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CodeValidationService {

    private final CodeRepository codeRepository;
    private final CodeGroupRepository codeGroupRepository;

    public void validateActiveCode(String groupCode, String code, String fieldName) {
        boolean groupActive = codeGroupRepository.countByGroupCodeAndIsActiveTrue(groupCode) > 0;
        if (!groupActive) {
            throw new IllegalArgumentException("????? ????? ????. " + groupCode);
        }

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        boolean exists = codeRepository.countByGroupCodeAndCodeAndIsActiveTrue(groupCode, code) > 0;
        if (!exists) {
            throw new IllegalArgumentException("????? ?? ?????. " + fieldName + "=" + code);
        }
    }
}
