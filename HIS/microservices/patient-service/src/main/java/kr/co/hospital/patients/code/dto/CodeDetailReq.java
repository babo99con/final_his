package kr.co.hospital.patients.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeDetailReq {

    private String groupCode;
    private String code;
    private String name;
    private Integer sortOrder;
    private String note;
    private Boolean isActive;
}