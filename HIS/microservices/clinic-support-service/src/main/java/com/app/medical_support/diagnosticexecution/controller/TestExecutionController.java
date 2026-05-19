package com.app.medical_support.diagnosticexecution.controller;

import com.hms.util.api.ApiResponse;
import com.app.medical_support.diagnosticexecution.dto.TestExecutionDTO;
import com.app.medical_support.diagnosticexecution.dto.TestExecutionReqDTO;
import com.app.medical_support.diagnosticexecution.dto.TestExecutionUpdateDTO;
import com.app.medical_support.diagnosticexecution.service.DiagnosticExecutionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/testExecution")
@RequiredArgsConstructor
@Tag(name = "TestExecution", description = "Test execution API")
public class TestExecutionController {

    private final DiagnosticExecutionService testExecutionService;

    @Operation(summary = "검사 수행 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TestExecutionDTO>>> findList(
            @Parameter(
                    description = "Execution type filter",
                    schema = @Schema(allowableValues = {"IMAGING", "ENDOSCOPY", "PATHOLOGY", "PHYSIOLOGICAL", "SPECIMEN"})
            )
            @RequestParam(required = false) String executionType
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Test execution list loaded.", testExecutionService.findTestExecutionList(executionType)));
    }

    @Operation(summary = "검사 수행 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TestExecutionDTO>> findTestExecutionDetail(
            @Parameter(description = "Test execution ID")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Test execution detail loaded.", testExecutionService.findTestExecutionDetail(id)));
    }

    @Operation(summary = "검사 수행 등록")
    @PostMapping
    public ResponseEntity<ApiResponse<TestExecutionDTO>> registerTestExecution(
            @Parameter(description = "Test execution request body")
            @RequestBody TestExecutionReqDTO testExecutionDTO
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Test execution created.", testExecutionService.registerTestExecution(testExecutionDTO)));
    }

    @Operation(summary = "검사 수행 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TestExecutionDTO>> modifyTestExecution(
            @Parameter(description = "Test execution ID")
            @PathVariable String id,
            @Parameter(description = "Test execution update request body")
            @RequestBody TestExecutionUpdateDTO testExecutionUpdateDTO
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "Test execution updated.", testExecutionService.modifyTestExecution(id, testExecutionUpdateDTO)));
    }
}
