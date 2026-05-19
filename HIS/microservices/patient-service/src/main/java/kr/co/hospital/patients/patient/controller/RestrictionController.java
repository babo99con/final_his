package kr.co.hospital.patients.patient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.hms.util.api.ApiResponse;
import kr.co.hospital.patients.patient.dto.RestrictionCreateReqDTO;
import kr.co.hospital.patients.patient.dto.RestrictionResDTO;
import kr.co.hospital.patients.patient.dto.RestrictionUpdateReqDTO;
import kr.co.hospital.patients.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restrictions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Patient restriction", description = "Patient restriction management")
public class RestrictionController {

    private final PatientService service;

    @Operation(summary = "List restrictions", description = "List all patient restrictions.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RestrictionResDTO>>> findList() {
        log.info("Controller: GET /api/restrictions");
        List<RestrictionResDTO> list = service.findRestrictionList();
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", list));
    }

    @Operation(summary = "Restriction detail", description = "Get restriction detail by id.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request, invalid format of the request. See response message for more information."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found, the specified id does not exist."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RestrictionResDTO>> findDetail(@PathVariable Long id) {
        log.info("Controller: GET /api/restrictions/{}", id);
        RestrictionResDTO restriction = service.findRestrictionDetail(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", restriction));
    }

    @Operation(summary = "Create restriction", description = "Create a patient restriction.")
    @PostMapping
    public ResponseEntity<ApiResponse<RestrictionResDTO>> register(
            @RequestBody RestrictionCreateReqDTO reqDTO
    ) {
        log.info("Controller: POST /api/restrictions");
        RestrictionResDTO saved = service.registerRestriction(reqDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Created", saved));
    }

    @Operation(summary = "Update restriction", description = "Update a patient restriction.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RestrictionResDTO>> modify(
            @PathVariable Long id,
            @RequestBody RestrictionUpdateReqDTO updateReqDTO
    ) {
        log.info("Controller: PUT /api/restrictions/{}", id);
        RestrictionResDTO updated = service.modifyRestriction(id, updateReqDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Updated", updated));
    }

    @Operation(summary = "Delete restriction", description = "Delete a patient restriction.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Long id) {
        log.info("Controller: DELETE /api/restrictions/{}", id);
        service.removeRestriction(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Deleted", null));
    }

    @Operation(summary = "Search restrictions", description = "Search patient restrictions.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<RestrictionResDTO>>> search(
            @RequestParam String type,
            @RequestParam String keyword
    ) {
        if (keyword == null || keyword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "keyword is required", List.of()));
        }

        List<RestrictionResDTO> list = service.searchRestrictions(type, keyword);
        if (list.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(false, "No results", List.of()));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "OK", list));
    }
}
