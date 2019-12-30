package com.greenfoxacademy.TribesBackend.constants;

public class EmailVerConstants {
    public static final Byte VER_CODE_LENGTH = 30;
    public static final String CHARS_TO_BE_REPLACED = "!@#$%^&*()_+";
    public static final String URL = "http://localhost:8080/verify/";
    public static final String SUBJECT = "Verify Your Email";
    public static final String MESSAGE = "Tribes are awaiting You!" +
            "\n" +
            "Proceed to the link below and You may start Your journey!" +
            "\n" +
            URL + CHARS_TO_BE_REPLACED +
            "\n" +
            "Sincirely" +
            "\n" +
            "Your Tribes team";
}
