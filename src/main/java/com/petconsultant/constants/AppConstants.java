package com.petconsultant.constants;

public class AppConstants {

    private AppConstants() {}

    // Success Messages
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";

    public static final String REGISTER_SUCCESS = "User registered successfully";
    public static final String LOGIN_SUCCESS    = "Login successful";
    public static final String OTP_SENT        = "OTP sent successfully";
    public static final String OTP_VERIFIED    = "OTP verified successfully";
    public static final String PASSWORD_RESET  = "Password reset successfully";
    public static final String LOGOUT_SUCCESS  = "Logged out successfully";

    // Error Messages
    public static final String USER_NOT_FOUND        = "User not found";
    public static final String EMAIL_ALREADY_EXISTS  = "Email already registered";
    public static final String INVALID_CREDENTIALS   = "Invalid email or password";
    public static final String INVALID_OTP           = "Invalid or expired OTP";
    public static final String ACCOUNT_INACTIVE      = "Account is inactive";

    // OTP
    public static final long OTP_EXPIRY_MINUTES = 5L;
}
