package com.jarvvski.service.credit.infrastructure

import com.jarvvski.service.credit.domain.CreditReportSupplier
import com.jarvvski.service.credit.domain.DecisionEngine
import com.jarvvski.service.credit.domain.PersonalIdentifier
import com.jarvvski.service.credit.infrastructure.AcmeLocalCreditModifierProvider.AcmeConstants.ACME_PROVIDER_NAME
import org.springframework.stereotype.Service

@Service
class AcmeLocalCreditModifierProvider : DecisionEngine.CreditReportProvider {
    val fakeBackingStore : Map<Long, DecisionEngine.CreditSegment> = hashMapOf(
        49002010965L to DecisionEngine.CreditSegment.DEBT,
        49002010976L to DecisionEngine.CreditSegment.ONE,
        49002010987L to DecisionEngine.CreditSegment.TWO,
        49002010998L to DecisionEngine.CreditSegment.THREE
    )

    private object AcmeConstants {
        const val ACME_PROVIDER_NAME : String = "Acme Credit Reports ltd."
    }

    override fun fetchSegment(personalIdentifier: PersonalIdentifier): DecisionEngine.CreditReport {
        val segment = fakeBackingStore.getOrDefault(personalIdentifier.data, DecisionEngine.CreditSegment.UNKNOWN)
        return DecisionEngine.CreditReport(CreditReportSupplier(ACME_PROVIDER_NAME), segment)
    }
}