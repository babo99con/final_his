package com.staff.domain.employee.reception.controller;

import com.hms.util.api.ApiResponse;
import com.staff.domain.employee.reception.dto.ReceptionRequestDTO;
import com.staff.domain.employee.reception.dto.ReceptionResponseDTO;
import com.staff.facade.EmployeeCreate.facade.EmployeeCreateFacade;
import com.staff.facade.EmployeeProfile.facade.EmployeeProfileFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reception")
public class ReceptionController {

    private final EmployeeCreateFacade staffOnboardingFacade;
    private final EmployeeProfileFacade employeeProfileFacade;


    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ReceptionResponseDTO>>> listReceptions() {
        return ResponseEntity.ok(new ApiResponse<>(true, "조회 성공", employeeProfileFacade.listReceptions()));
    }

    @GetMapping("/detail/{staffId}")
    public ResponseEntity<ApiResponse<ReceptionResponseDTO>> getReceptionDetail(@PathVariable String staffId) {
        return ResponseEntity.ok(new ApiResponse<>(true, "조회 성공", employeeProfileFacade.getReceptionDetail(staffId)));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ReceptionResponseDTO>>> searchReceptions(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "all") String searchType
    ) {
        log.info("[GET] /api/reception/search search={}, searchType={}", search, searchType);
        List<ReceptionResponseDTO> list = employeeProfileFacade.searchReceptions(search, searchType);
        return ResponseEntity.ok(new ApiResponse<>(true, "원무 검색 조회 성공", list));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createReception(@RequestBody ReceptionRequestDTO receptionReq) {
        String created = staffOnboardingFacade.createReception(receptionReq);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "원무 직원이 추가되었습니다", created));
    }

    @PutMapping("/edit/{staffId}")
    public ResponseEntity<ApiResponse<ReceptionResponseDTO>> updateReception(
            @PathVariable String staffId,
            @RequestBody ReceptionRequestDTO req
    ) {
        return ResponseEntity.ok(new ApiResponse<>(true, "수정되었습니다", employeeProfileFacade.updateReception(staffId, req)));
    }

    @DeleteMapping("/delete/{staffId}")
    public ResponseEntity<ApiResponse<Void>> deleteReception(@PathVariable String staffId) {
        employeeProfileFacade.deleteReception(staffId);
        return ResponseEntity.ok(new ApiResponse<>(true, "원무 삭제 완료", null));
    }
}
