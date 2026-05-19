package com.app.medical_support.nursingtreatment.exception;

public class TreatmentResultNotFoundException extends RuntimeException {

    public TreatmentResultNotFoundException(String id) {
        super("Treatment result not found. id=" + id);
    }
}
