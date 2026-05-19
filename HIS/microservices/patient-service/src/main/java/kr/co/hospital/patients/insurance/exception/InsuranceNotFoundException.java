package kr.co.hospital.patients.insurance.exception;

public class InsuranceNotFoundException extends RuntimeException {
    public InsuranceNotFoundException(Long id) {
        super("??? ?? ? ????. id=" + id);
    }
}
