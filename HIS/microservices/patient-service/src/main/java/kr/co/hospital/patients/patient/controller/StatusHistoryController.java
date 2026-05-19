package kr.co.hospital.patients.patient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.hms.util.api.ApiResponse;
import kr.co.hospital.patients.patient.dto.StatusHistoryCreateReqDTO;
import kr.co.hospital.patients.patient.dto.StatusHistoryResDTO;
import kr.co.hospital.patients.patient.dto.StatusHistoryUpdateReqDTO;
import kr.co.hospital.patients.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status-history")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Patient status history", description = "Patient status history management")
public class StatusHistoryController {

    private final PatientService service;

    @Operation(summary = "List status history", description = "List all patient status history.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StatusHistoryResDTO>>> findList() {
        log.info("Controller: GET /api/status-history");
        List<StatusHistoryResDTO> list = service.findStatusHistoryList();
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", list));
    }

    @Operation(summary = "Status history detail", description = "Get status history detail by id.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request, invalid format of the request. See response message for more information."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found, the specified id does not exist."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StatusHistoryResDTO>> findDetail(@PathVariable Long id) {
        log.info("Controller: GET /api/status-history/{}", id);
        StatusHistoryResDTO history = service.findStatusHistoryDetail(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", history));
    }

    @Operation(summary = "Create status history", description = "Create a patient status history.")
    @PostMapping
    public ResponseEntity<ApiResponse<StatusHistoryResDTO>> register(
            @RequestBody StatusHistoryCreateReqDTO reqDTO
    ) {
        log.info("Controller: POST /api/status-history");
        StatusHistoryResDTO saved = service.registerStatusHistory(reqDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Created", saved));
    }

    @Operation(summary = "Update status history", description = "Update a patient status history.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StatusHistoryResDTO>> modify(
            @PathVariable Long id,
            @RequestBody StatusHistoryUpdateReqDTO updateReqDTO
    ) {
        log.info("Controller: PUT /api/status-history/{}", id);
        StatusHistoryResDTO updated = service.modifyStatusHistory(id, updateReqDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Updated", updated));
    }

    @Operation(summary = "Delete status history", description = "Delete a patient status history.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Long id) {
        log.info("Controller: DELETE /api/status-history/{}", id);
        service.removeStatusHistory(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Deleted", null));
    }

    @Operation(summary = "Search status history", description = "Search patient status history.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StatusHistoryResDTO>>> search(
            @RequestParam String type,
            @RequestParam String keyword
    ) {
        if (keyword == null || keyword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "keyword is required", List.of()));
        }

        List<StatusHistoryResDTO> list = service.searchStatusHistories(type, keyword);
        if (list.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(false, "No results", List.of()));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "OK", list));
    }
}