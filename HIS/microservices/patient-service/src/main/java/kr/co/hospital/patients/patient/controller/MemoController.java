package kr.co.hospital.patients.patient.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.hms.util.api.ApiResponse;
import kr.co.hospital.patients.patient.dto.MemoCreateReqDTO;
import kr.co.hospital.patients.patient.dto.MemoResDTO;
import kr.co.hospital.patients.patient.dto.MemoUpdateReqDTO;
import kr.co.hospital.patients.patient.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/memos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Patient memo", description = "Patient memo management")
public class MemoController {

    private final PatientService service;

    @Operation(summary = "List memos", description = "List all patient memos.")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MemoResDTO>>> findList() {
        log.info("Controller: GET /api/memos");
        List<MemoResDTO> list = service.findMemoList();
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", list));
    }

    @Operation(summary = "Memo detail", description = "Get memo detail by id.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Bad Request, invalid format of the request. See response message for more information."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not found, the specified id does not exist."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Unprocessable entity, input parameters caused the processing to fails. See response message for more information.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemoResDTO>> findDetail(@PathVariable Long id) {
        log.info("Controller: GET /api/memos/{}", id);
        MemoResDTO memo = service.findMemoDetail(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", memo));
    }

    @Operation(summary = "Create memo", description = "Create a patient memo.")
    @PostMapping
    public ResponseEntity<ApiResponse<MemoResDTO>> register(
            @RequestBody MemoCreateReqDTO reqDTO
    ) {
        log.info("Controller: POST /api/memos");
        MemoResDTO saved = service.registerMemo(reqDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Created", saved));
    }

    @Operation(summary = "Update memo", description = "Update a patient memo.")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemoResDTO>> modify(
            @PathVariable Long id,
            @RequestBody MemoUpdateReqDTO updateReqDTO
    ) {
        log.info("Controller: PUT /api/memos/{}", id);
        MemoResDTO updated = service.modifyMemo(id, updateReqDTO);
        return ResponseEntity.ok(new ApiResponse<>(true, "Updated", updated));
    }

    @Operation(summary = "Delete memo", description = "Delete a patient memo.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> remove(@PathVariable Long id) {
        log.info("Controller: DELETE /api/memos/{}", id);
        service.removeMemo(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Deleted", null));
    }

    @Operation(summary = "Search memos", description = "Search patient memos.")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MemoResDTO>>> search(
            @RequestParam String type,
            @RequestParam String keyword
    ) {
        if (keyword == null || keyword.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "keyword is required", List.of()));
        }

        List<MemoResDTO> list = service.searchMemos(type, keyword);
        if (list.isEmpty()) {
            return ResponseEntity.ok(new ApiResponse<>(false, "No results", List.of()));
        }

        return ResponseEntity.ok(new ApiResponse<>(true, "OK", list));
    }
}
