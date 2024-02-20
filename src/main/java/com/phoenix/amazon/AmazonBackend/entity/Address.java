package com.phoenix.amazon.AmazonBackend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.COUNTRY;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.STATE;

@Entity
@Table(name = "user_address")
public class Address {
    @Id
    private String addressId;
    @Column(name = "user_mobile", nullable = false, unique = true)
    private String mobileNumber;
    @Column(name = "pin_code", nullable = false)
    private String pinCode;
    @Column(name = "address1", nullable = false)
    private String addressLine1;
    private String addressLine2;
    @Column(name = "house_apartment_no", nullable = false)
    private String houseOrApartmentNumber;
    @Column(name = "town", nullable = false)
    private String townOrCity;
    @Enumerated(value = EnumType.STRING)
    private COUNTRY country;
    @Enumerated(value = EnumType.STRING)
    private STATE state;

    protected Address() {
    }

    public Address(builder builder) {
        this.addressId = builder.addressId;
        this.mobileNumber = builder.mobileNumber;
        this.pinCode = builder.pinCode;
        this.addressLine1 = builder.addressLine1;
        this.addressLine2 = builder.addressLine2;
        this.houseOrApartmentNumber = builder.houseOrApartmentNumber;
        this.townOrCity = builder.townOrCity;
        this.country = builder.country;
        this.state = builder.state;
    }

    public static final class builder {
        private String addressId;
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

        public Address build() {
            return new Address(this);
        }
    }
}