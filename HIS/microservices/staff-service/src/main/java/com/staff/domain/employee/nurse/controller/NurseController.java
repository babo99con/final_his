package com.staff.domain.employee.nurse.controller;

import com.hms.util.api.ApiResponse;
import com.staff.domain.employee.nurse.dto.NurseRequestDTO;
import com.staff.domain.employee.nurse.dto.NurseResponseDTO;
import com.staff.facade.EmployeeProfile.facade.EmployeeProfileFacade;
import com.staff.facade.EmployeeCreate.facade.EmployeeCreateFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/nurse")
public class NurseController {
    private final EmployeeCreateFacade staffOnboardingFacade;
    private final EmployeeProfileFacade employeeProfileFacade;

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<NurseResponseDTO>>> listNurses()

    { return ResponseEntity.ok(new ApiResponse<>(true, "조회 성공",

            employeeProfileFacade.listNurses())); }

    @GetMapping("/detail/{staffId}")
    public ResponseEntity<ApiResponse<NurseResponseDTO>> getNurseDetail
            (@PathVariable String staffId)

   { return ResponseEntity.ok(new ApiResponse<>(true, "조회 성공",

            employeeProfileFacade.getNurseDetail(staffId))); }



    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<NurseResponseDTO>>> searchNurses(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "all") String searchType
    ) {
        log.info("[GET] /api/nurse/search search={}, searchType={}", search, searchType);

        List<NurseResponseDTO> list = employeeProfileFacade.searchNurses(search, searchType);

        return ResponseEntity.ok(new ApiResponse<>(true, "간호사 검색 조회 성공", list));
    }






    @PostMapping("/create") public ResponseEntity<ApiResponse<String>>
    createNurse(@RequestBody NurseRequestDTO nurseReq)

    { String created = staffOnboardingFacade.createNurse(nurseReq);

        return ResponseEntity.status(201).body(new ApiResponse<>(true, "회원추가했습니다", created)); }


    @PutMapping("/edit/{staffId}")
    public ResponseEntity<ApiResponse<NurseResponseDTO>>
    updateNurse(@PathVariable String staffId,
                @RequestBody NurseRequestDTO req)
    { return ResponseEntity.ok(new ApiResponse<>(true, "수정되었습니다",
            employeeProfileFacade.updateNurse(staffId, req))); }


//    @DeleteMapping("/delete/{staffId}")
//    public ResponseEntity<ApiResponse<Void>>
//    deleteNurse(@PathVariable String staffId){
//
//            employeeProfileFacade.deleteNurse(staffId);
//    return ResponseEntity.ok(new ApiResponse<>(true, "의사 삭제 완료", null));
//    }
}

