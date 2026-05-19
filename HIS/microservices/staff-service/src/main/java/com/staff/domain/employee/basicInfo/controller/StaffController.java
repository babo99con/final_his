package com.staff.domain.employee.basicInfo.controller;

import com.hms.util.api.ApiResponse;
import com.staff.domain.employee.basicInfo.dto.StaffRequestDTO;
import com.staff.domain.employee.basicInfo.dto.StaffResponseDTO;
import com.staff.domain.employee.basicInfo.dto.StaffUpdateRequestDTO;
import com.staff.domain.employee.basicInfo.service.command.StaffCommonService;
import com.staff.domain.employee.basicInfo.service.query.StaffQueryService;
import com.staff.facade.EmployeeDelete.command.BasicInfoDelete;
import com.staff.facade.EmployeeDelete.facade.EmployeeDeleteFacadeImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*** 직원 공통 컨트롤러.*/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffCommonService staffCommonService;
    private final StaffQueryService staffQueryService;

    //삭제 퍼사드
    private final EmployeeDeleteFacadeImpl employeeDeleteFacadeImpl;

    //검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<StaffResponseDTO>>> searchStaffs(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "all") String searchType
    ) {
        log.info("[GET] /api/doctor/search search={}, searchType={}", search, searchType);

        List<StaffResponseDTO> list = staffQueryService.searchStaff(search, searchType);

        return ResponseEntity.ok(new ApiResponse<>(true, "의사 검색 조회 성공", list));
    }






    /**공통 직원 정보생성**/
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<StaffResponseDTO>> createStaff(
            @RequestBody StaffRequestDTO staffReq) {

        log.info("[POST] /api/staff/create body={}", staffReq);

        StaffResponseDTO created = staffCommonService.createStaff(staffReq);

        if (created == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>
                             (false, "직원 정보가 없습니다.", null));
        }
            return ResponseEntity.status(201).body(new ApiResponse<>
                             (true, "직원 공통 정보 생성 완료", created));
        }

    /** 공통 직원 단건조회**/
    @GetMapping("/detail/{staffId}")
    public ResponseEntity<ApiResponse<StaffResponseDTO>> detailStaff(
            @PathVariable String staffId) {
        log.info("[GET] /api/staff/detail/{}", staffId);

        StaffResponseDTO detail = staffQueryService.detailStaff(staffId);

        if (detail == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>
                              (false, "직원 정보가 없습니다.", null));
        }
            return ResponseEntity.status(201).body(new ApiResponse<>
                              (true, "직원 공통 정보 조회 성공", detail));
        }


    /** 공통 직원 목록조회 */
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<StaffResponseDTO>>> listStaff() {

        log.info("[GET] /api/staff/list");

        List<StaffResponseDTO> list = staffQueryService.listStaff();

        if (list == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>
                              (false, "직원 리스트가 없습니다.", null));

        }
            return ResponseEntity.status(201).body(new ApiResponse<>
                              (true, "직원 목록 조회 성공", list));
        }


    /** 공통 직원 수정.*/
    @PutMapping("/update/{staffId}")
    public ResponseEntity<ApiResponse<StaffResponseDTO>> updateStaff(
            @PathVariable String staffId,
            @RequestBody StaffUpdateRequestDTO requestDTO) {

        log.info("[PUT] /api/staff/update/{} body={}", staffId, requestDTO);

        StaffResponseDTO updated = staffCommonService.updateStaff(staffId, requestDTO);

        if (updated == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>
                               (false, "수정할 직원이 없습니다.", null));
        }

            return ResponseEntity.status(201).body(new ApiResponse<>
                               (true, "직원 공통 정보 수정 완료", updated));
        }




    /** 공통 직원 삭제 */
    @DeleteMapping("/delete/{staffId}")
    public ResponseEntity<ApiResponse<StaffResponseDTO>> deleteStaff(
            @PathVariable String staffId) {

        log.info("[DELETE] /api/staff/delete/{}", staffId);

        StaffResponseDTO delete = employeeDeleteFacadeImpl.deleteStaff(BasicInfoDelete.from(staffId));

        if (delete == null) {
            return ResponseEntity.status(404).body(new ApiResponse<>
                               (false, "삭제할 직원이 없습니다.", null));
        }

             return ResponseEntity.status(201).body(new ApiResponse<>
                               (true, "직원 삭제 완료", delete));
        }
        }
