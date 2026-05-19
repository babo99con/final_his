package kr.co.hospital.patients.patient.exception;

public class StatusHistoryNotFoundException extends RuntimeException {
    public StatusHistoryNotFoundException(Long id) {
        super("Status history not found. id=" + id);
    }
}