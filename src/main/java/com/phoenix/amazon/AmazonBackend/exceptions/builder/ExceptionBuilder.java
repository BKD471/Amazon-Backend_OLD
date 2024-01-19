package com.phoenix.amazon.AmazonBackend.exceptions.builder;

import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES;

public class ExceptionBuilder implements IClassNameBuilder, IDescriptionBuilder, IMethodNameBuilder, IBuild {
    private Object className;
    private String description;
    private String methodName;

    private ExceptionBuilder() {
    }

    public static IClassNameBuilder builder() {
        return new ExceptionBuilder();
    }

    /**
     * @param className
     * @return
     */
    @Override
    public IDescriptionBuilder className(final Object className) {
        this.className = className;
        return this;
    }

    /**
     * @param description
     * @return
     */
    @Override
    public IMethodNameBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * @param methodName
     * @return
     */
    @Override
    public IBuild methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    /**
     * @param exceptionCodes
     * @return
     */
    @Override
    public Exception build(EXCEPTION_CODES exceptionCodes) {
        switch (exceptionCodes) {
            case USER_EXEC -> {
                return new UserExceptions(className, description, methodName);
            }
            case BAD_API_EXEC -> {
                return new BadApiRequestExceptions(className, description, methodName);
            }
            default -> throw new RuntimeException("Invalid Exception Code");
        }
    }

}
