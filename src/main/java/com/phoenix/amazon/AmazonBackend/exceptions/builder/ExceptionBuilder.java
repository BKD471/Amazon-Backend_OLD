package com.phoenix.amazon.AmazonBackend.exceptions.builder;

import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.ServicDownTimeException;
import com.phoenix.amazon.AmazonBackend.exceptions.UserExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.UserNotFoundExceptions;
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
     * @param className - Exception class type
     * @return IDescriptionBuilder
     */
    @Override
    public IDescriptionBuilder className(final Object className) {
        this.className = className;
        return this;
    }

    /**
     * @param description - Description of exception
     * @return IMethodNameBuilder
     */
    @Override
    public IMethodNameBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * @param methodName - Origin of exception
     * @return IBuild
     */
    @Override
    public IBuild methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    /**
     * @param exceptionCodes - exception code
     * @return Exception
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
            case USER_NOT_FOUND_EXEC -> {
                return new UserNotFoundExceptions(className, description, methodName);
            }
            case SERVICE_DOWN_EXEC -> {
                return new ServicDownTimeException(className, description, methodName);
            }
            default -> throw new RuntimeException("Invalid Exception Code");
        }
    }
}

