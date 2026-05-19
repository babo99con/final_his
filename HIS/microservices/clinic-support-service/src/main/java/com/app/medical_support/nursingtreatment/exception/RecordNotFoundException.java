package com.app.medical_support.nursingtreatment.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String nursingId) {
        super("해당 기록이 존재하지 않습니다: " + nursingId);
    }
}
