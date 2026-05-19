package kr.co.hospital.patients.code.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CodeDetailRes {

    private String groupCode;
    private String code;
    private String name;
    private Integer sortOrder;
    private Boolean isActive;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}