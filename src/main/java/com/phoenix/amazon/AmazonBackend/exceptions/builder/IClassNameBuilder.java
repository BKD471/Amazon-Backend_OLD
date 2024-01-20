package com.phoenix.amazon.AmazonBackend.exceptions.builder;

import java.util.Objects;

public interface IClassNameBuilder {
    /**
     * @param className - Exception class type
     * @return IDescriptionBuilder
     */
    IDescriptionBuilder className(final Object className);
}
