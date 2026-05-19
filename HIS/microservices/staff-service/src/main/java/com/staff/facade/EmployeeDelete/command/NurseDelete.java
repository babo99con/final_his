package com.staff.facade.EmployeeDelete.command;



public record NurseDelete(
        String staffId,          //식별

        String deletedBy,        //누가 했는지 확인
        String reason,           //왜 삭제하는지

        boolean force            //강제 여부
) {

    public static NurseDelete of(String staffId) {
        return new NurseDelete(
                staffId,
                "system",
                "delete request",

                false
        );
    }
}