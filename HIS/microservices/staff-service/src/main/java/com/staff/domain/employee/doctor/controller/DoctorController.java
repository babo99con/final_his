package com.staff.domain.employee.doctor.controller;

import com.hms.util.api.ApiResponse;
import com.staff.domain.employee.doctor.dto.DoctorRequestDTO;
import com.staff.domain.employee.doctor.dto.DoctorResponseDTO;
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
@RequestMapping("/api/doctor")
public class DoctorController {
    private final EmployeeCreateFacade staffOnboardingFacade;
    private final EmployeeProfileFacade employeeProfileFacade;


    //조회
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<DoctorResponseDTO>>> listDoctors()
    { return ResponseEntity.ok(new ApiResponse<>(true, "조회 성공",
            employeeProfileFacade.listDoctors())); }


    //디테일
    @GetMapping("/detail/{staffId}")
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> getDoctorDetail
            (@PathVariable String staffId)

    { return ResponseEntity.ok(new ApiResponse<>(true, "조회 성공",
            employeeProfileFacade.getDoctorDetail(staffId))); }


    //검색
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<DoctorResponseDTO>>> searchDoctors(
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "all") String searchType
    ) {
        log.info("[GET] /api/doctor/search search={}, searchType={}", search, searchType);

        List<DoctorResponseDTO> list = employeeProfileFacade.searchDoctors(search, searchType);

        return ResponseEntity.ok(new ApiResponse<>(true, "의사 검색 조회 성공", list));
    }




    //생성
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createDoctor
            (@RequestBody DoctorRequestDTO doctorReq)

    { String created = staffOnboardingFacade.createDoctor(doctorReq);
        return ResponseEntity.status(201).body(new ApiResponse<>(true, "의사 생성 완료", created));
    }


    //수정
    @PatchMapping("/update/{staffId}")
    public ResponseEntity<ApiResponse<DoctorResponseDTO>> updateDoctor
            (@PathVariable String staffId, @RequestBody DoctorRequestDTO req)

    { return ResponseEntity.ok(new ApiResponse<>(true, "회원 수정 성공",
            employeeProfileFacade.updateDoctor(staffId, req)));
    }


/*
    //삭제
    @DeleteMapping("/delete/{staffId}")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable String staffId) {
        employeeProfileFacade.(staffId);
        return ResponseEntity.ok(new ApiResponse<>(true, "의사 삭제 완료", null));
*/

//    }
}
