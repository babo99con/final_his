package com.staff.facade.EmployeeProfile.command;

import com.staff.domain.employee.reception.dto.ReceptionRequestDTO;


//원무 임시레코드
public record ReceptionProfileCommand(
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

    public static ReceptionProfileCommand from(String staffId, ReceptionRequestDTO requestDTO) {
        if (requestDTO == null) return null;
        return new ReceptionProfileCommand(
                staffId,
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
