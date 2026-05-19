package kr.co.hospital.patients.code.controller;

import kr.co.hospital.patients.code.dto.CodeDetailReq;
import kr.co.hospital.patients.code.dto.CodeDetailRes;
import kr.co.hospital.patients.code.dto.CodeGroupReq;
import kr.co.hospital.patients.code.dto.CodeGroupRes;
import kr.co.hospital.patients.code.service.CodeAdminService;
import com.hms.util.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/codes")
@RequiredArgsConstructor
public class CodeAdminController {

    private final CodeAdminService codeAdminService;

    @GetMapping("/groups")
    public ResponseEntity<ApiResponse<List<CodeGroupRes>>> getGroups(
            @RequestParam(defaultValue = "false") boolean activeOnly
    ) {
        return ResponseEntity.ok(ApiResponse.success(codeAdminService.findGroups(activeOnly)));
    }

    @PostMapping("/groups")
    public ResponseEntity<ApiResponse<CodeGroupRes>> createGroup(@RequestBody CodeGroupReq req) {
        return ResponseEntity.ok(ApiResponse.success(codeAdminService.createGroup(req)));
    }

    @PutMapping("/groups/{groupCode}")
    public ResponseEntity<ApiResponse<CodeGroupRes>> updateGroup(
            @PathVariable String groupCode,
            @RequestBody CodeGroupReq req
    ) {
        return ResponseEntity.ok(ApiResponse.success(codeAdminService.updateGroup(groupCode, req)));
    }

    @PatchMapping("/groups/{groupCode}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateGroup(@PathVariable String groupCode) {
        codeAdminService.deactivateGroup(groupCode);
        return ResponseEntity.ok(ApiResponse.<Void>success("OK", null));
    }

    @PatchMapping("/groups/{groupCode}/activate")
    public ResponseEntity<ApiResponse<Void>> activateGroup(@PathVariable String groupCode) {
        codeAdminService.activateGroup(groupCode);
        return ResponseEntity.ok(ApiResponse.<Void>success("OK", null));
    }

    @GetMapping("/details")
    public ResponseEntity<ApiResponse<List<CodeDetailRes>>> getDetails(
            @RequestParam(required = false) String groupCode,
            @RequestParam(defaultValue = "false") boolean activeOnly
    ) {
        return ResponseEntity.ok(ApiResponse.success(codeAdminService.findDetails(groupCode, activeOnly)));
    }

    @PostMapping("/details")
    public ResponseEntity<ApiResponse<CodeDetailRes>> createDetail(@RequestBody CodeDetailReq req) {
        return ResponseEntity.ok(ApiResponse.success(codeAdminService.createDetail(req)));
    }

    @PutMapping("/details/{groupCode}/{code}")
    public ResponseEntity<ApiResponse<CodeDetailRes>> updateDetail(
            @PathVariable String groupCode,
            @PathVariable String code,
            @RequestBody CodeDetailReq req
    ) {
        return ResponseEntity.ok(ApiResponse.success(codeAdminService.updateDetail(groupCode, code, req)));
    }

    @PatchMapping("/details/{groupCode}/{code}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateDetail(
            @PathVariable String groupCode,
            @PathVariable String code
    ) {
        codeAdminService.deactivateDetail(groupCode, code);
        return ResponseEntity.ok(ApiResponse.<Void>success("OK", null));
    }

    @PatchMapping("/details/{groupCode}/{code}/activate")
    public ResponseEntity<ApiResponse<Void>> activateDetail(
            @PathVariable String groupCode,
            @PathVariable String code
    ) {
        codeAdminService.activateDetail(groupCode, code);
        return ResponseEntity.ok(ApiResponse.<Void>success("OK", null));
    }
}
