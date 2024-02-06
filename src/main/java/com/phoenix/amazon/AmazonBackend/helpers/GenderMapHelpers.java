package com.phoenix.amazon.AmazonBackend.helpers;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.MALE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.FEMALE;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.NON_BINARY;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.GENDER.LGBTQ;

import java.util.HashMap;
import java.util.Map;

public class GenderMapHelpers {
    public static final Map<String, AllConstantHelpers.GENDER> genderMap = new HashMap<>();

    static {
        genderMap.put("MALE", MALE);
        genderMap.put("FEMALE", FEMALE);
        genderMap.put("NON_BINARY", NON_BINARY);
        genderMap.put("LGBTQ", LGBTQ);

    }

    public static GENDER getGender(final String gender) {
        if (genderMap.containsKey(gender)) return genderMap.get(gender);
        return null;
    }

}
