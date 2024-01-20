package com.phoenix.amazon.AmazonBackend.exceptions.builder;

public interface IMethodNameBuilder {
    /**
     * @param methodName - Origin of exception
     * @return IBuild
     */
    IBuild methodName(final String methodName);
}
