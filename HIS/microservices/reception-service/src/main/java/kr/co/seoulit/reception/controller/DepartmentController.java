package kr.co.seoulit.reception.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.hms.util.api.ApiResponse;
import kr.co.seoulit.reception.dto.DepartmentDTO;
import kr.co.seoulit.reception.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
@Tag(name = "吏꾨즺怨?API", description = "吏꾨즺怨?API")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Operation(summary = "吏꾨즺怨?紐⑸줉 議고쉶", description = "HOSPITAL.STAFF_DEPARTMENT 湲곗? 吏꾨즺怨?紐⑸줉 議고쉶")
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentDTO>>> getDepartments() {
        List<DepartmentDTO> list = departmentService.getActiveDepartments();
        return ResponseEntity.ok(new ApiResponse<>(true, "吏꾨즺怨?紐⑸줉 議고쉶 ?꾨즺", list));
    }
}
