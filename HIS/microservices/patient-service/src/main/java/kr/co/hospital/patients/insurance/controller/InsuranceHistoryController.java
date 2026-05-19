package kr.co.hospital.patients.insurance.controller;

import com.hms.util.api.ApiResponse;
import kr.co.hospital.patients.insurance.service.InsuranceService;
import kr.co.hospital.patients.insurancehistory.dto.InsuranceHistoryResDTO;
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
@RequestMapping("/api/insurances/history")
@Slf4j
public class InsuranceHistoryController {

    private final InsuranceService insuranceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<InsuranceHistoryResDTO>>> findByPatient(
            @RequestParam Long patientId
    ) {
        log.info("Controller: GET /api/insurances/history?patientId={}", patientId);
        List<InsuranceHistoryResDTO> list = insuranceService.findHistoryByPatientId(patientId);
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", list));
    }
}
