package com.phoenix.amazon.AmazonBackend.dto;


import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.COUNTRY;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.STATE;

public record AddressDto(String addressId,
                         String firstName,
                         String lastName,
                         String mobileNumber,
                         String pinCode,
                         String addressLine1,
                         String addressLine2,
                         String houseOrApartmentNumber,
                         String townOrCity,
                         COUNTRY country,
                         STATE state) {

    public AddressDto(String addressId,
                      String firstName,
                      String lastName,
                      String mobileNumber,
                      String pinCode,
                      String addressLine1,
                      String addressLine2,
                      String houseOrApartmentNumber,
                      String townOrCity,
                      COUNTRY country,
                      STATE state) {
        this.addressId = addressId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mobileNumber = mobileNumber;
        this.pinCode = pinCode;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.houseOrApartmentNumber = houseOrApartmentNumber;
        this.townOrCity = townOrCity;
        this.country = country;
        this.state = state;
    }

    public static final class builder {
        private String addressId;
        private String firstName;
        private String lastName;
        private String mobileNumber;
        private String pinCode;
        private String addressLine1;
        private String addressLine2;
        private String houseOrApartmentNumber;
        private String townOrCity;
        private COUNTRY country;
        private STATE state;

        public builder() {
        }

        public builder addressId(final String addressId) {
            this.addressId = addressId;
            return this;
        }

        public builder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public builder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public builder mobileNumber(final String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public builder pinCode(final String pinCode) {
            this.pinCode = pinCode;
            return this;
        }

        public builder addressLine1(final String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public builder addressLine2(final String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public builder houseOrApartmentNumber(final String houseOrApartmentNumber) {
            this.houseOrApartmentNumber = houseOrApartmentNumber;
            return this;
        }

        public builder townOrCity(final String townOrCity) {
            this.townOrCity = townOrCity;
            return this;
        }

        public builder country(final COUNTRY country) {
            this.country = country;
            return this;
        }

        public builder state(final STATE state) {
            this.state = state;
            return this;
        }


        public AddressDto build() {
            return new AddressDto(addressId,
                    firstName,
                    lastName,
                    mobileNumber,
                    pinCode,
                    addressLine1,
                    addressLine2,
                    houseOrApartmentNumber,
                    townOrCity,
                    country,
                    state);
        }
    }
}
