package com.phoenix.amazon.AmazonBackend.dto.fieldvalidator;

public class RegexMatchersHelpers {
    public static final String PATTERN_FOR_PASSWORD="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{15,}$";
    public static final String PATTERN_FOR_USERNAME="^[A-Za-z][A-Za-z0-9_]{5,20}$";
}
