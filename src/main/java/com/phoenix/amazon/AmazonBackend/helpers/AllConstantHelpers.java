package com.phoenix.amazon.AmazonBackend.helpers;

import com.phoenix.amazon.AmazonBackend.dto.UserDto;

import java.util.List;

public class AllConstantHelpers {
    public enum COUNTRY {
        USA,UNITED_KINGDOM,INDIA,AUSTRALIA,JAPAN,CHINA,MALAYSIA,ITALY,CANADA,GERMANY
    }

    public enum STATE {
        WEST_BENGAL,MICHIGAN,TEXAS,LOS_ANGELES,NEW_YORK
    }

    public enum GENDER {
        MALE,FEMALE,NON_BINARY,LGBTQ
    }

    public enum USER_FIELDS {
        NAME,EMAIL,GENDER,LAST_SEEN,ABOUT
    }

    public enum USER_VALIDATION{
        CREATE_USER,
        UPDATE_USER_BY_USER_ID_OR_USER_NAME,
        DELETE_USER_BY_USER_ID_OR_USER_NAME,
        GET_ALL_USERS,
        GET_USER_INFO_BY_EMAIL_USER_NAME,
        SEARCH_USER_BY_FIELD_AND_VALUE
    }

}
