package kr.co.hospital.patients.code.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CodeGroupRes {

    private String groupCode;
    private String groupName;
    private Boolean editableYn;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}