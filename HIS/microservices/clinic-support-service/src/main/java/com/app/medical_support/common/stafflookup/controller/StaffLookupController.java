package com.app.medical_support.common.stafflookup.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.common.stafflookup.dto.StaffOptionDTO;
import com.app.medical_support.common.stafflookup.service.StaffLookupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staffs")
@Tag(name = "StaffLookup", description = "Staff lookup API")
public class StaffLookupController {

    private final StaffLookupService staffLookupService;

    @Operation(summary = "직원 드롭다운 옵션 조회")
    @GetMapping("/options")
    public ResponseEntity<ApiResponse<List<StaffOptionDTO>>> findStaffOptions(
            @RequestParam String role,
            @RequestParam(required = false) String examType,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer limit
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Staff options loaded.",
                        staffLookupService.findStaffOptions(role, examType, keyword, limit)
                )
        );
    }
}
