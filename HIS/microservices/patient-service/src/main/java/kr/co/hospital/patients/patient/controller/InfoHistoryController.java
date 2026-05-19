package kr.co.hospital.patients.patient.controller;

import com.hms.util.api.ApiResponse;
import kr.co.hospital.patients.patient.dto.InfoHistoryResDTO;
import kr.co.hospital.patients.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/patients/info-history")
@Slf4j
public class InfoHistoryController {

    private final PatientService service;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InfoHistoryResDTO>>> findByPatient(
            @RequestParam Long patientId
    ) {
        log.info("Controller: GET /api/patients/info-history?patientId={}", patientId);
        List<InfoHistoryResDTO> list = service.findInfoHistoryByPatientId(patientId);
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", list));
    }
}