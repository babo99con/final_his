package kr.co.hospital.patients.menu.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** 계층??쿼리 결과??flat DTO */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuFlatDto {

    private Long id;
    private Long parentId;
    private String code;
    private String name;
    private String path;
    private String icon;
    private Integer sortOrder;
}
