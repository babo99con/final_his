package com.app.medical_support.common.stafflookup.dto;

import com.app.medical_support.common.exception.InvalidRequestException;

import java.util.Locale;

public enum StaffLookupRole {
    NURSE,
    STF_ONLY,
    EXAM_RECEPTION_MANAGER,
    EXAM_RESULT_MANAGER,
    EXAM_PERFORMER;

    public static StaffLookupRole from(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            throw new InvalidRequestException("role is required.");
        }
        try {
            return StaffLookupRole.valueOf(raw.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new InvalidRequestException("Unsupported role: " + raw);
        }
    }
}
