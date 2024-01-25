package com.phoenix.amazon.AmazonBackend.dto.fieldvalidator;

public class RegexMatchersHelpers {
    public static final String PATTERN_FOR_EMAIL="^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-]+)(\\.[a-zA-Z]{2,5}){1,2}$";
    public static final String PATTERN_FOR_PHONE_NUMBER="^(\\+\\d{1,2}\\s?)?1?\\-?\\.?\\s?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";
    public static final String PATTERN_FOR_PASSWORD="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{15,}$";
}
