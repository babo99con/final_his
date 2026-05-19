package com.app.medical_support.nursingtreatment.exception;

public class MedicationRecordNotFoundException extends RuntimeException {

    public MedicationRecordNotFoundException(String id) {
        super("Medication record not found. id=" + id);
    }
}
