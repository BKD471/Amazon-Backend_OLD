package com.phoenix.amazon.AmazonBackend.exceptions.builder;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.EXCEPTION_CODES;

public interface IBuild {
      /**
       * @param exceptionCodes - exception code
       * @return Exception
       */
      Exception build(final EXCEPTION_CODES exceptionCodes);
}
