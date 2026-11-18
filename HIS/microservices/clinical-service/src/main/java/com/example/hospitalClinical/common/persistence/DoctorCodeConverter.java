package com.example.hospitalClinical.common.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Converter
@RequiredArgsConstructor
public class DoctorCodeConverter implements AttributeConverter<String, Long> {
    private final JdbcTemplate jdbcTemplate;

    // Reception sends staff codes, while Oracle stores numeric keys for visits and orders.
    @Override
    public Long convertToDatabaseColumn(String doctorCode) {
        if (doctorCode == null) {
            return null;
        }
        String code = doctorCode.trim();
        if (code.matches("[0-9]+")) {
            return Long.valueOf(code);
        }

        Long staffKey = jdbcTemplate.query(
                "SELECT id FROM HOSPITAL.CMH_STAFF WHERE staff_id = ?",
                rows -> rows.next() ? rows.getLong(1) : null,
                code
        );
        if (staffKey == null) {
            throw new IllegalArgumentException("Unknown doctor code: " + code);
        }
        return staffKey;
    }

    @Override
    public String convertToEntityAttribute(Long staffKey) {
        if (staffKey == null) {
            return null;
        }

        String doctorCode = jdbcTemplate.query(
                "SELECT staff_id FROM HOSPITAL.CMH_STAFF WHERE id = ? AND staff_id LIKE 'DOC-%'",
                rows -> rows.next() ? rows.getString(1) : null,
                staffKey
        );
        return doctorCode == null ? staffKey.toString() : doctorCode;
    }
}
