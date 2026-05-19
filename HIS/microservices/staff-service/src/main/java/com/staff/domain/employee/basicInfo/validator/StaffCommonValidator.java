package com.staff.domain.employee.basicInfo.validator;

import com.staff.common.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/*** 직원관리 공통 validator.* 여러 유스케이스에서 반복되는 검증 예외처리.*/
@Component
public class StaffCommonValidator {

    /** 대상 ID가 없거나 0 이하이면 예외 */
    public void validateTargetId(Long id, String message) {
        if (id == null || id <= 0L) {
            throw new BusinessException(message);
        }
    }

    /** 업로드 파일이 없거나 비어 있으면 예외 (업로드는 퍼사드에서 사용중 ) */
    public void validateUploadFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("업로드할 파일이 없습니다.");
        }
    }




    /** 필수 문자열 검증  + 정리 함수 */
    //DB로 따지면 필수 문자열값 낫널 보장
    public String requireText(String value, String message) {
        String normalized = trimToNull(value);
        if (normalized == null) {
            throw new BusinessException(message);
        }
        return normalized;
    }

        /**정리 함수
     * // 공백 제거 후 빈 문자열은 null로 통일 (입력 정규화)**/
       //DB로 따지면 NULL값 보장  (입력 정규화)
    public String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
        }

        }
