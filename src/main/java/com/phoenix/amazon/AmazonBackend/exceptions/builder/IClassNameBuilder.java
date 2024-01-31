package com.phoenix.amazon.AmazonBackend.exceptions.builder;


public interface IClassNameBuilder {
    /**
     * @param className - Exception class type
     * @return IDescriptionBuilder
     */
    IDescriptionBuilder className(final Object className);
}