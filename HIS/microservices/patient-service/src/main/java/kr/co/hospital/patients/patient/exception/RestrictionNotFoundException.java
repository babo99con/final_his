package kr.co.hospital.patients.patient.exception;

public class RestrictionNotFoundException extends RuntimeException {
    public RestrictionNotFoundException(Long id) {
        super("Patient restriction not found. id=" + id);
    }
}
