package com.app.medical_support.nursingtreatment.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.nursingtreatment.dto.MedicationRecordDTO;
import com.app.medical_support.nursingtreatment.dto.MedicationRecordReqDTO;
import com.app.medical_support.nursingtreatment.dto.MedicationRecordUpdateDTO;
import com.app.medical_support.nursingtreatment.dto.RecordStatusRequest;
import com.app.medical_support.nursingtreatment.service.NursingTreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/medicationRecord")
@RequiredArgsConstructor
@Tag(name = "MedicationRecord", description = "Medication record API")
public class MedicationRecordController {

    private final NursingTreatmentService nursingTreatmentService;

    @Operation(summary = "Medication record list")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MedicationRecordDTO>>> findList() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Medication record list loaded.", nursingTreatmentService.findMedicationRecordList()));
    }

    @Operation(summary = "Medication record search")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MedicationRecordDTO>>> search(
            @RequestParam(value = "patientName", required = false) String patientName,
            @RequestParam(value = "departmentName", required = false) String departmentName,
            @RequestParam(value = "progressStatus", required = false) String progressStatus,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Medication record search completed.",
                nursingTreatmentService.searchMedicationRecord(patientName, departmentName, progressStatus, startDate, endDate)
        ));
    }

    @Operation(summary = "Medication record detail")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicationRecordDTO>> findDetail(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Medication record detail loaded.", nursingTreatmentService.findMedicationRecordDetail(id)));
    }

    @Operation(summary = "Create medication record")
    @PostMapping
    public ResponseEntity<ApiResponse<MedicationRecordDTO>> register(@RequestBody MedicationRecordReqDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Medication record created.", nursingTreatmentService.registerMedicationRecord(dto)));
    }

    @Operation(summary = "Update medication record")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MedicationRecordDTO>> modify(@PathVariable String id, @RequestBody MedicationRecordUpdateDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Medication record updated.", nursingTreatmentService.modifyMedicationRecord(id, dto)));
    }

    @Operation(summary = "Update medication record status")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<MedicationRecordDTO>> updateStatus(@PathVariable String id, @RequestBody RecordStatusRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Medication record status updated.", nursingTreatmentService.updateMedicationRecordStatus(id, request.getStatus())));
    }
}
