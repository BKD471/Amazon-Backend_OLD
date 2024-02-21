package com.phoenix.amazon.AmazonBackend.helpers;


public class AllConstantHelpers {
    public enum COUNTRY {
        USA, UNITED_KINGDOM, INDIA, AUSTRALIA, JAPAN, CHINA, MALAYSIA, ITALY, CANADA, GERMANY
    }

    public enum STATE {
        WEST_BENGAL, MICHIGAN, TEXAS, LOS_ANGELES, NEW_YORK
    }

    public enum GENDER {
        MALE, FEMALE, NON_BINARY, LGBTQ
    }

    public enum USER_FIELDS {
        USER_NAME, FIRST_NAME, LAST_NAME, PRIMARY_EMAIL, SECONDARY_EMAIL, GENDER, LAST_SEEN, ABOUT, PASSWORD, PROFILE_IMAGE

    }

    public enum  CATEGORY_FIELDS{
        TITLE,DESCRIPTION,COVER_IMAGE
    }

    public enum USER_VALIDATION {
        CREATE_USER,
        DELETE_USER_BY_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL,
        GET_ALL_USERS,
        SEARCH_ALL_USERS_BY_USER_NAME,
        SEARCH_USER_BY_PRIMARY_EMAIL,
        SEARCH_ALL_USERS_BY_GENDER,
        SEARCH_ALL_USERS_BY_FIRST_NAME,
        SEARCH_ALL_USERS_BY_LAST_NAME,
        NULL_OBJECT,
        UPDATE_PRIMARY_EMAIL,
        UPDATE_SECONDARY_EMAIL,
        UPDATE_PASSWORD,
        UPDATE_USERNAME,
        GET_USER_INFO_BY_USERID_USER_NAME_PRIMARY_EMAIL,
        VALIDATE_PASSWORD
    }
    public enum IMAGE_VALIDATION{
        UPDATE_PROFILE_IMAGE, GET_PROFILE_IMAGE,UPLOAD_CATEGORY_IMAGE
    }

    public enum USER_FIELD_VALIDATION {
        VALIDATE_USER_ID_OR_USER_NAME_OR_PRIMARY_EMAIL
    }

    public enum CATEGORY_VALIDATION{
        CREATE_CATEGORY,NOT_FOUND_CATEGORY,UPDATE_TITLE,UPDATE_DESCRIPTION
    }
    public enum EXCEPTION_CODES {
        USER_EXEC, BAD_API_EXEC, USER_NOT_FOUND_EXEC, SERVICE_DOWN_EXEC,
        CATEGORY_NOT_FOUND_EXEC,CATEGORY_EXEC
    }

    public enum DestinationDtoType {
        USER_DTO,CATEGORY_DTO
    }

    public enum AddressType{
        HOME,BUSINESS
    }
}
