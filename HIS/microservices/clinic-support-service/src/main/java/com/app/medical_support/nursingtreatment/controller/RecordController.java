package com.app.medical_support.nursingtreatment.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.nursingtreatment.dto.RecordCreateReqDTO;
import com.app.medical_support.nursingtreatment.dto.RecordDTO;
import com.app.medical_support.nursingtreatment.dto.RecordResponseDTO;
import com.app.medical_support.nursingtreatment.dto.RecordStatusRequest;
import com.app.medical_support.nursingtreatment.dto.RecordUpdateDTO;
import com.app.medical_support.nursingtreatment.service.NursingTreatmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/api/record")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Record", description = "Nursing record API")
public class RecordController {

    private final NursingTreatmentService recordService;

    @Operation(summary = "간호 기록 검색")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<RecordResponseDTO>>> search(
            @RequestParam("searchType") String searchType,
            @RequestParam(value = "searchValue", required = false) String searchValue,
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Record search completed.", recordService.search(searchType, searchValue, startDate, endDate)));
    }

    @Operation(summary = "간호 기록 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RecordResponseDTO>>> findList() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Record list loaded.", recordService.findRecordList()));
    }

    @Operation(summary = "간호 기록 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RecordResponseDTO>> findRecordDetail(@PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Record detail loaded.", recordService.findRecordDetail(id)));
    }

    @Operation(summary = "간호 기록 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<RecordDTO>> registerRecord(@RequestBody RecordCreateReqDTO record) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Record created.", recordService.registerRecord(record)));
    }

    @Operation(summary = "간호 기록 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RecordDTO>> modifyRecord(@PathVariable String id, @RequestBody RecordUpdateDTO recordDTO) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Record updated.", recordService.modifyRecord(id, recordDTO)));
    }

    @Operation(summary = "간호 기록 상태 변경")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<RecordDTO>> updateRecordStatus(@PathVariable String id, @RequestBody RecordStatusRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Record status updated.", recordService.updateRecordStatus(id, request.getStatus())));
    }
}
