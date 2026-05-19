package app.auth.verification.service;

public interface VerificationService {

    String sendVerificationCode(String username, String email);

    boolean verifyCode(String username, String email, String code);

    String sendPhoneVerificationCode(String username, String phone);

    boolean verifyPhoneCode(String username, String phone, String code);

    String sendRegisterEmailCode(String rawEmail);

    String verifyRegisterEmailCode(String rawEmail, String rawCode);

    String sendRegisterPhoneCode(String rawPhone);

    String verifyRegisterPhoneCode(String rawPhone, String rawCode);
}
