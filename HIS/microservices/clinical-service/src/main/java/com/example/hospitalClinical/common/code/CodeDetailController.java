package com.example.hospitalClinical.common.code;

import com.hms.util.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:3001",
        "http://127.0.0.1:3000",
        "http://127.0.0.1:3001",
        "http://localhost:5173"
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code-details")
public class CodeDetailController {

    private final CodeDetailRepository repository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CodeDetailOptionDto>>> listByGroup(
            @RequestParam("group") String group
    ) {
        String g = group != null ? group.trim().toUpperCase() : "";
        if (g.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail("group 파라미터가 필요합니다."));
        }

        List<CodeDetailOptionDto> items = repository
                .findByGroupCodeAndIsActiveTrue(
                        g,
                        Sort.by(Sort.Order.asc("sortOrder"), Sort.Order.asc("codeName"), Sort.Order.asc("code"))
                )
                .stream()
                .map(r -> new CodeDetailOptionDto(r.getCode(), r.getCodeName(), r.getNote()))
                .toList();

        return ResponseEntity.ok(ApiResponse.ok(items));
    }
}

