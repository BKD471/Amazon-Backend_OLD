package com.phoenix.amazon.AmazonBackend.audit;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditAwareImpl implements AuditorAware<String> {
    /**
     * @returnType - String
     */
    @Override
    public @NotNull Optional<String> getCurrentAuditor() {
        return Optional.of("Admin");
    }
}
