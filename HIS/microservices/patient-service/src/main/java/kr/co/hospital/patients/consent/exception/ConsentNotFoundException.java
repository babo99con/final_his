package kr.co.hospital.patients.consent.exception;

public class ConsentNotFoundException extends RuntimeException {
    public ConsentNotFoundException(Long id) {
        super("???? ?? ? ????. id=" + id);
    }
}
