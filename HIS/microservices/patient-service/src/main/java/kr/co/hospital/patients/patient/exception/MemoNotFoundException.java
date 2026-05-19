package kr.co.hospital.patients.patient.exception;

public class MemoNotFoundException extends RuntimeException {
    public MemoNotFoundException(Long id) {
        super("Patient memo not found. id=" + id);
    }
}
