package com.staff.facade.EmployeeDelete.command;



public record BasicInfoDelete(

        String staffId,          //식별

        String deletedBy,        //누가 했는지 확인
        String reason,           //왜 삭제하는지
//        DeleteMode deleteMode,   //소프트삭제 / 물리삭제
        boolean force            //강제 여부

)

{
    public static BasicInfoDelete from(String staffId) {
        return new BasicInfoDelete(
                staffId,
                "system",
                "delete request",
//                DeleteMode.SOFT,
                false
        );

    }
}
