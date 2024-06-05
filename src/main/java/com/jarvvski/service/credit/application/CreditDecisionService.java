package com.jarvvski.service.credit.application;

import com.jarvvski.service.credit.domain.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;

@Service
@BrowserCallable
@AnonymousAllowed
public class CreditDecisionService {
    private final CreditApplicationValidator creditApplicationValidator;
    private final DecisionEngine decisionEngine;

    public CreditDecisionService(CreditApplicationValidator creditApplicationValidator, DecisionEngine decisionEngine) {
        this.creditApplicationValidator = creditApplicationValidator;
        this.decisionEngine = decisionEngine;
    }

    public record CreditApplicationDto(
            @NotNull String username,
            @NotNull String personalId,
            @NotNull long loanAmount,
            @NotNull int loanTermInMonths
            ) {
    }

    public CreditDecision apply(CreditApplicationDto applicationDto) {

        final var application = new CreditApplication(
                new User(
                        new Name(applicationDto.username),
                        new PersonalIdentifier(Long.parseLong(applicationDto.personalId.trim()))
                ),
                new Loan(
                        new Money(BigDecimal.valueOf(applicationDto.loanAmount)),
                        new LoanTerm(applicationDto.loanTermInMonths)
                )
        );

        final var validationResult = creditApplicationValidator.validate(application.getLoan());
        if (!validationResult.isValid()) {
            // TODO: Handle errors
            return new CreditDecision(Decision.DECLINED_UNKNOWN_CREDIT, null, Collections.emptyList());
        }

        return decisionEngine.performDecision(application);
    }
}
