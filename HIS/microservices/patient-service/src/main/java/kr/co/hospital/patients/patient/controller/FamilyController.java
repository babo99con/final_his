package kr.co.hospital.patients.patient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.hms.util.api.ApiResponse;
import kr.co.hospital.patients.patient.dto.FamilyCreateReqDTO;
import kr.co.hospital.patients.patient.dto.FamilyResDTO;
import kr.co.hospital.patients.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients/{patientId}/families")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "PatientFamily", description = "Patient family management")
public class FamilyController {

    private final PatientService patientService;

    @Operation(summary = "List families", description = "Get patient's family list.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<FamilyResDTO>>> findByPatientId(@PathVariable Long patientId) {
        List<FamilyResDTO> list = patientService.findByPatientId(patientId);
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", list));
    }

    @Operation(summary = "Add families", description = "Add family members for a patient.")
    @PostMapping
    public ResponseEntity<ApiResponse<List<FamilyResDTO>>> create(
            @PathVariable Long patientId,
            @RequestBody List<FamilyCreateReqDTO> families
    ) {
        List<FamilyResDTO> created = patientService.createForPatient(patientId, families);
        return ResponseEntity.ok(new ApiResponse<>(true, "Created", created));
    }
}
