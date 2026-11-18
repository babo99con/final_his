package kr.co.seoulit.reception.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import kr.co.seoulit.reception.mapper.DoctorMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Converter
@RequiredArgsConstructor
public class DoctorCodeConverter implements AttributeConverter<String, Long> {
    private final DoctorMapper doctorMapper;

    // The API uses staff codes, while the restored Oracle tables store numeric keys.
    @Override
    public Long convertToDatabaseColumn(String doctorCode) {
        if (doctorCode == null) {
            return null;
        }
        if (doctorCode.matches("[0-9]+")) {
            return Long.valueOf(doctorCode);
        }
        Long staffKey = doctorMapper.selectStaffKey(doctorCode);
        if (staffKey == null) {
            throw new IllegalArgumentException("Unknown doctor code: " + doctorCode);
        }
        return staffKey;
    }

    @Override
    public String convertToEntityAttribute(Long staffKey) {
        if (staffKey == null) {
            return null;
        }
        String doctorCode = doctorMapper.selectDoctorCode(staffKey);
        return doctorCode == null ? staffKey.toString() : doctorCode;
    }
}
