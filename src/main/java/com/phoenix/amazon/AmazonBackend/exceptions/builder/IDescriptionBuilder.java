package com.phoenix.amazon.AmazonBackend.exceptions.builder;

public interface IDescriptionBuilder {
    /**
     * @param description - Description of exception
     * @return IMethodNameBuilder
     */
    IMethodNameBuilder description(final String description);
}
