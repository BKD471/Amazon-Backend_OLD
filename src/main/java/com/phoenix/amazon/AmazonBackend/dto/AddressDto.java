package com.phoenix.amazon.AmazonBackend.dto;


import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.AddressType;

public record AddressDto(String addressId,
                         String mobileNumber,
                         String addressLineNumberOne,
                         String addressLineNumberTwo,
                         AddressType addressType,
                         String pinCode,
                         String townOrCity,
                         String district,
                         String state,
                         String country) {

    public AddressDto(String addressId,
                      String mobileNumber,
                      String addressLineNumberOne,
                      String addressLineNumberTwo,
                      AddressType addressType,
                      String pinCode,
                      String townOrCity,
                      String district,
                      String state,
                      String country) {
        this.addressId = addressId;
        this.mobileNumber = mobileNumber;
        this.addressLineNumberOne = addressLineNumberOne;
        this.addressLineNumberTwo = addressLineNumberTwo;
        this.addressType = addressType;
        this.pinCode = pinCode;
        this.townOrCity = townOrCity;
        this.district = district;
        this.state = state;
        this.country = country;
    }

    public static final class builder {
        private String addressId;
        private String mobileNumber;
        private String addressLineNumberOne;
        private String addressLineNumberTwo;
        private AddressType addressType;
        private String pinCode;
        private String townOrCity;
        private String district;
        private String state;
        private String country;

        public builder() {
        }

        public builder addressId(final String addressId) {
            this.addressId = addressId;
            return this;
        }

        public builder mobileNumber(final String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public builder addressLineNumberOne(final String addressLineNumberOne) {
            this.addressLineNumberOne = addressLineNumberOne;
            return this;
        }

        public builder addressLineNumberTwo(final String addressLineNumberTwo) {
            this.addressLineNumberTwo = addressLineNumberTwo;
            return this;
        }

        public builder addressType(final AddressType addressType) {
            this.addressType = addressType;
            return this;
        }

        public builder pinCode(final String pinCode) {
            this.pinCode = pinCode;
            return this;
        }

        public builder townOrCity(final String townOrCity) {
            this.townOrCity = townOrCity;
            return this;
        }

        public builder district(final String district) {
            this.district = district;
            return this;
        }

        public builder state(final String state) {
            this.state = state;
            return this;
        }

        public builder country(final String country) {
            this.country = country;
            return this;
        }


        public AddressDto build() {
            return new AddressDto(addressId,
                    mobileNumber,
                    addressLineNumberOne,
                    addressLineNumberTwo,
                    addressType,
                    pinCode,
                    townOrCity,
                    district,
                    state,
                    country);
        }
    }
}
