package kr.co.hospital.patients.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeGroupReq {

    private String groupCode;
    private String groupName;
    private Boolean editableYn;
}