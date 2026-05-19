package com.app.medical_support.common.integration.reception.service;

import com.app.medical_support.common.integration.reception.client.ReceptionApiClient;
import com.app.medical_support.common.integration.reception.dto.OutpatientReceptionDTO;
import com.app.medical_support.common.integration.reception.dto.ReceptionDepartmentDTO;
import com.app.medical_support.common.integration.reception.dto.ReceptionDoctorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceptionIntegrationService {

    private final ReceptionApiClient receptionApiClient;

    public List<OutpatientReceptionDTO> findListByConditions(
            String visitDate,
            String visitType,
            String statuses,
            String departmentId,
            String doctorId
    ) {
        LocalDate targetDate = parseVisitDate(visitDate);
        String day = targetDate.toString();
        String normalizedVisitType = normalizeVisitType(visitType);
        Set<String> allowedStatuses = parseStatusSet(normalizeStatuses(statuses));

        List<OutpatientReceptionDTO> rows = receptionApiClient.fetchReceptionsByDateRange(
                day,
                day,
                departmentId,
                doctorId
        );

        List<OutpatientReceptionDTO> filtered = rows.stream()
                .filter(row -> matchesVisitType(row, normalizedVisitType))
                .filter(row -> matchesAllowedStatus(row, allowedStatuses))
                .collect(Collectors.toList());
        enrichDoctorNamesInList(filtered);
        return filtered;
    }

    public OutpatientReceptionDTO findDetail(Long id) {
        OutpatientReceptionDTO dto = receptionApiClient.fetchDetail(id);
        enrichDepartmentNameIfMissing(dto);
        enrichDoctorNameIfMissing(dto);
        return dto;
    }

    /**
     * 접수 상세/대기열 폴백 응답에 진료과명이 비어 있는 경우, 접수 {@code GET /api/departments}로 보강한다.
     */
    private void enrichDepartmentNameIfMissing(OutpatientReceptionDTO dto) {
        if (dto == null) {
            return;
        }
        if (trimToNull(dto.getDepartmentName()) != null || trimToNull(dto.getDepartmentId()) == null) {
            return;
        }
        try {
            for (ReceptionDepartmentDTO row : receptionApiClient.fetchDepartments()) {
                if (dto.getDepartmentId().equals(trimToNull(row.getDepartmentId()))) {
                    dto.setDepartmentName(trimToNull(row.getDepartmentName()));
                    break;
                }
            }
        } catch (RuntimeException ignored) {
            // 보강 실패 시에도 상세 조회 자체는 유지
        }
    }

    /**
     * 접수 상세에 의사명이 비어 있으면 접수 {@code GET /api/doctors}로 {@code doctorId}에 맞는 이름을 채운다.
     * 진료과별 조회가 실패·누락돼도 전체 의사 목록 조회는 별도로 시도한다.
     */
    private void enrichDoctorNameIfMissing(OutpatientReceptionDTO dto) {
        if (dto == null) {
            return;
        }
        if (trimToNull(dto.getDoctorName()) != null) {
            return;
        }
        String doctorId = trimToNull(dto.getDoctorId());
        if (doctorId == null) {
            return;
        }
        String name = null;
        String deptId = trimToNull(dto.getDepartmentId());
        if (deptId != null) {
            try {
                name = findDoctorName(receptionApiClient.fetchDoctors(deptId), doctorId);
            } catch (RuntimeException ignored) {
                // 접수 /api/doctors?departmentId= 오류 시 전체 목록으로 이어감
            }
        }
        if (name == null) {
            try {
                name = findDoctorName(receptionApiClient.fetchDoctors(null), doctorId);
            } catch (RuntimeException ignored) {
                // 보강 실패 시에도 상세 조회 자체는 유지
            }
        }
        if (name != null) {
            dto.setDoctorName(name);
        }
    }

    /**
     * 목록·대기열에서 의사명이 비어 있는 행만, 접수 의사 목록 한 번 조회로 채운다.
     */
    private void enrichDoctorNamesInList(List<OutpatientReceptionDTO> rows) {
        if (rows == null || rows.isEmpty()) {
            return;
        }
        boolean anyMissing = rows.stream()
                .anyMatch(row -> trimToNull(row.getDoctorName()) == null && trimToNull(row.getDoctorId()) != null);
        if (!anyMissing) {
            return;
        }
        try {
            List<ReceptionDoctorDTO> doctors = receptionApiClient.fetchDoctors(null);
            Map<String, String> nameByDoctorId = doctors.stream()
                    .filter(Objects::nonNull)
                    .filter(d -> trimToNull(d.getDoctorId()) != null)
                    .collect(Collectors.toMap(
                            d -> trimToNull(d.getDoctorId()).toUpperCase(Locale.ROOT),
                            d -> trimToNull(d.getDoctorName()) != null ? trimToNull(d.getDoctorName()) : "",
                            (a, b) -> a
                    ));
            for (OutpatientReceptionDTO dto : rows) {
                if (trimToNull(dto.getDoctorName()) != null) {
                    continue;
                }
                String id = trimToNull(dto.getDoctorId());
                if (id == null) {
                    continue;
                }
                String resolved = nameByDoctorId.get(id.toUpperCase(Locale.ROOT));
                if (trimToNull(resolved) != null) {
                    dto.setDoctorName(resolved);
                }
            }
        } catch (RuntimeException ignored) {
            // 보강 실패 시 목록은 그대로
        }
    }

    private String findDoctorName(List<ReceptionDoctorDTO> doctorRows, String doctorId) {
        if (doctorRows == null || doctorRows.isEmpty()) {
            return null;
        }
        final String needle = doctorId.trim();
        return doctorRows.stream()
                .filter(row -> row != null && trimToNull(row.getDoctorId()) != null)
                .filter(row -> needle.equalsIgnoreCase(trimToNull(row.getDoctorId())))
                .map(ReceptionDoctorDTO::getDoctorName)
                .map(this::trimToNull)
                .findFirst()
                .orElse(null);
    }

    /**
     * 간호기록 등에서 사용하는 외래 접수 대기열.
     * 접수 MSA {@code /queue}는 환경에 따라 진료중 행이 빠질 수 있어, 진료 MSA와 같이
     * {@code GET /api/receptions}({@code dateFrom}/{@code dateTo})로 당일 목록을 받은 뒤
     * 외래·상태({@code WAITING},{@code CALLED},{@code IN_PROGRESS})만 걸러 반환한다.
     * {@code date}가 비어 있으면 서버 기준 오늘 날짜를 사용한다.
     */
    public List<OutpatientReceptionDTO> findQueue(String date, String departmentId, String doctorId) {
        LocalDate targetDate = resolveQueueDate(date);
        String day = targetDate.toString();
        Set<String> allowedStatuses = parseStatusSet(normalizeStatuses(null));

        List<OutpatientReceptionDTO> rows = receptionApiClient.fetchReceptionsByDateRange(
                day,
                day,
                departmentId,
                doctorId
        );

        List<OutpatientReceptionDTO> filtered = rows.stream()
                .filter(row -> matchesVisitType(row, "OUTPATIENT"))
                .filter(row -> matchesAllowedStatus(row, allowedStatuses))
                .collect(Collectors.toList());
        enrichDoctorNamesInList(filtered);
        return filtered;
    }

    private LocalDate resolveQueueDate(String date) {
        String value = trimToNull(date);
        if (value == null) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "date format is invalid. format=yyyy-MM-dd",
                    ex
            );
        }
    }

    private LocalDate parseVisitDate(String visitDate) {
        String value = trimToNull(visitDate);
        if (value == null) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "visitDate is required. format=yyyy-MM-dd"
            );
        }

        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException ex) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "visitDate format is invalid. format=yyyy-MM-dd",
                    ex
            );
        }
    }

    private String normalizeVisitType(String visitType) {
        String value = trimToNull(visitType);
        return value == null ? "OUTPATIENT" : value.toUpperCase();
    }

    private String normalizeStatuses(String statuses) {
        String value = trimToNull(statuses);
        if (value == null) {
            return "WAITING,CALLED,IN_PROGRESS";
        }

        String normalized = Arrays.stream(value.split(","))
                .map(this::trimToNull)
                .filter(item -> item != null && !item.isEmpty())
                .map(s -> s.toUpperCase(Locale.ROOT))
                .collect(Collectors.joining(","));

        if (normalized.isEmpty()) {
            throw new ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "statuses is invalid. use CSV format e.g. WAITING,CALLED,IN_PROGRESS"
            );
        }
        return normalized;
    }

    private Set<String> parseStatusSet(String normalizedCsv) {
        return Arrays.stream(normalizedCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.toUpperCase(Locale.ROOT))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private boolean matchesVisitType(OutpatientReceptionDTO row, String normalizedVisitType) {
        if (trimToNull(normalizedVisitType) == null) {
            return true;
        }
        String vt = trimToNull(row.getVisitType());
        if (vt == null) {
            return true;
        }
        return normalizedVisitType.equalsIgnoreCase(vt);
    }

    private boolean matchesAllowedStatus(OutpatientReceptionDTO row, Set<String> allowedStatuses) {
        String status = trimToNull(row.getStatus());
        if (status == null) {
            return false;
        }
        return allowedStatuses.contains(status.toUpperCase(Locale.ROOT));
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
