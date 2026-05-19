package com.app.medical_support.common.stafflookup.dto;

import com.app.medical_support.common.exception.InvalidRequestException;

import java.util.Locale;

public enum StaffExamType {
    IMAGING,
    PATHOLOGY,
    ENDOSCOPY,
    PHYSIOLOGICAL,
    SPECIMEN;

    public static StaffExamType from(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new InvalidRequestException("examType is required for EXAM_PERFORMER role.");
        }
        try {
            return StaffExamType.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new InvalidRequestException("Unsupported examType: " + raw);
        }
    }
}
