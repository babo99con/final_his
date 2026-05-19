package com.app.medical_support.nursingtreatment.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.nursingtreatment.dto.RecordStatusRequest;
import com.app.medical_support.nursingtreatment.dto.TreatmentResultCreateDTO;
import com.app.medical_support.nursingtreatment.dto.TreatmentResultDTO;
import com.app.medical_support.nursingtreatment.dto.TreatmentResultUpdateDTO;
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
@RequestMapping("/api/treatmentResult")
@RequiredArgsConstructor
@Tag(name = "TreatmentResult", description = "Treatment result API")
public class TreatmentResultController {

    private final NursingTreatmentService nursingTreatmentService;

    @Operation(summary = "Treatment result list")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TreatmentResultDTO>>> findList() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Treatment result list loaded.", nursingTreatmentService.findTreatmentResultList()));
    }

    @Operation(summary = "Treatment result search")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<TreatmentResultDTO>>> search(
            @RequestParam(value = "patientName", required = false) String patientName,
            @RequestParam(value = "departmentName", required = false) String departmentName,
            @RequestParam(value = "progressStatus", required = false) String progressStatus,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ) {
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Treatment result search completed.",
                nursingTreatmentService.searchTreatmentResult(patientName, departmentName, progressStatus, startDate, endDate)
        ));
    }

    @Operation(summary = "Treatment result detail")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TreatmentResultDTO>> findDetail(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Treatment result detail loaded.", nursingTreatmentService.findTreatmentResultDetail(id)));
    }

    @Operation(summary = "Create treatment result")
    @PostMapping
    public ResponseEntity<ApiResponse<TreatmentResultDTO>> register(@RequestBody TreatmentResultCreateDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Treatment result created.", nursingTreatmentService.registerTreatmentResult(dto)));
    }

    @Operation(summary = "Update treatment result")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TreatmentResultDTO>> modify(@PathVariable String id, @RequestBody TreatmentResultUpdateDTO dto) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Treatment result updated.", nursingTreatmentService.modifyTreatmentResult(id, dto)));
    }

    @Operation(summary = "Update treatment result status")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TreatmentResultDTO>> updateStatus(@PathVariable String id, @RequestBody RecordStatusRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Treatment result status updated.", nursingTreatmentService.updateTreatmentResultStatus(id, request.getStatus())));
    }
}
