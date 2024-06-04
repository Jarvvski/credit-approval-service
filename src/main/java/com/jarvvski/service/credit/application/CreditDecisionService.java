package com.jarvvski.service.credit.application;

import com.jarvvski.service.credit.domain.CreditApplication;
import com.jarvvski.service.credit.domain.CreditApplicationValidator;
import com.jarvvski.service.credit.domain.CreditDecision;
import com.jarvvski.service.credit.domain.DecisionEngine;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import dev.hilla.BrowserCallable;
import org.springframework.stereotype.Service;

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


    public CreditDecision apply(CreditApplication application) {
        final var validationResult = creditApplicationValidator.validate(application.getLoan());
        if (!validationResult.isValid()) {
            // TODO: Handle errors
        }

        return decisionEngine.performDecision(application);
    }
}
