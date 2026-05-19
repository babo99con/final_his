package com.staff.facade.EmployeeCreate.command;

import com.staff.domain.employee.reception.dto.ReceptionRequestDTO;

public record ReceptionCreate(
        String staffId,
        String jobTypeCd,
        String deskNo,
        String shiftType,
        String startDate,
        String windowArea,
        String multiTask,
        String rmk,
        String receptionType,
        String extNo
) {

    public static ReceptionCreate from(ReceptionRequestDTO requestDTO) {
        if (requestDTO == null) return null;
        return new ReceptionCreate(
                requestDTO.getStaffId(),
                requestDTO.getJobTypeCd(),
                requestDTO.getDeskNo(),
                requestDTO.getShiftType(),
                requestDTO.getStartDate(),
                requestDTO.getWindowArea(),
                requestDTO.getMultiTask(),
                requestDTO.getRmk(),
                requestDTO.getReceptionType(),
                requestDTO.getExtNo()
        );
    }

    public ReceptionRequestDTO toRequestDTO() {
        return ReceptionRequestDTO.builder()
                .staffId(staffId)
                .jobTypeCd(jobTypeCd)
                .deskNo(deskNo)
                .shiftType(shiftType)
                .startDate(startDate)
                .windowArea(windowArea)
                .multiTask(multiTask)
                .rmk(rmk)
                .receptionType(receptionType)
                .extNo(extNo)
                .build();
    }
}
