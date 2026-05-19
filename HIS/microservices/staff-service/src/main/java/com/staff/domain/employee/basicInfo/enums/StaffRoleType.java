package com.staff.domain.employee.basicInfo.enums;

/**
 * 직원 역할 공통 상수.
 * 각 서비스에 타입넣을려고 만듬
 */

public enum StaffRoleType {
    DOCTOR,
    NURSE,
    RECEPTION
}

//if ("DOCTOR".equals(role)) { ... }
//if ("NURSE".equals(role)) { ... }
//
//이걸
//
//if (role == StaffRoleType.DOCTOR) { ... }
//if (role == StaffRoleType.NURSE) { ... }

//더 이상 하드코딩으로 타입을 지정하는게아니라
//이걸 이용해서 타입을 삽입할수있음
//각각 서비스에 가면 사용처 보임