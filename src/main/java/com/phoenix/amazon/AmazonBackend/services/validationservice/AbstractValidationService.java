package com.phoenix.amazon.AmazonBackend.services.validationservice;

import com.phoenix.amazon.AmazonBackend.exceptions.BadApiRequestExceptions;
import com.phoenix.amazon.AmazonBackend.exceptions.builder.ExceptionBuilder;

import java.util.Objects;
import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES.BAD_API_EXEC;

public abstract class AbstractValidationService {
    /**
     * @param field              - field to test
     * @param descriptionMessage - description
     * @param methodName         - place of origin
     * @throws BadApiRequestExceptions - list of exceptions being thrown
     **/
    protected void validateNullField(final Object field, final String descriptionMessage, final String methodName) throws BadApiRequestExceptions {
        if (Objects.isNull(field))
            throw (BadApiRequestExceptions) ExceptionBuilder.builder()
                    .className(BadApiRequestExceptions.class)
                    .description(descriptionMessage)
                    .methodName(methodName).build(BAD_API_EXEC);
    }

}
