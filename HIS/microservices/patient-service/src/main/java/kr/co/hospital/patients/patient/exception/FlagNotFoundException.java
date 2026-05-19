package kr.co.hospital.patients.patient.exception;

public class FlagNotFoundException extends RuntimeException {
    public FlagNotFoundException(Long id) {
        super("?? ???? ?? ? ????. id=" + id);
    }
}
