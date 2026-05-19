package kr.co.hospital.patients.consent.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.hms.util.api.ApiResponse;
import kr.co.hospital.patients.consent.dto.ConsentTodayItemResDTO;
import kr.co.hospital.patients.consent.service.ConsentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/consents")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Consent Standalone", description = "Consent standalone (today list)")
public class ConsentStandaloneController {

    private final ConsentService service;

    @Operation(summary = "Today's consents", description = "?? ??? ??? ??/?? ??")
    @GetMapping("/today")
    public ResponseEntity<ApiResponse<List<ConsentTodayItemResDTO>>> findToday() {
        log.info("Controller: GET /api/consents/today");
        List<ConsentTodayItemResDTO> list = service.findTodayItems();
        return ResponseEntity.ok(new ApiResponse<>(true, "OK", list));
    }
}
