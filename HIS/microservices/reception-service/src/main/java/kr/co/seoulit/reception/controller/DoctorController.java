package kr.co.seoulit.reception.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.hms.util.api.ApiResponse;
import kr.co.seoulit.reception.dto.DoctorDTO;
import kr.co.seoulit.reception.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/doctors")
@Tag(name = "?섏궗 API", description = "?섏궗 API")
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "?섏궗 紐⑸줉 議고쉶", description = "HOSPITAL.CMH_STAFF 湲곗? ?섏궗 紐⑸줉 議고쉶")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DoctorDTO>>> getDoctors(
            @Parameter(description = "吏꾨즺怨?ID")
            @RequestParam(required = false) String departmentId
    ) {
        List<DoctorDTO> list = doctorService.getActiveDoctors(departmentId);
        return ResponseEntity.ok(new ApiResponse<>(true, "?섏궗 紐⑸줉 議고쉶 ?꾨즺", list));
    }
}
