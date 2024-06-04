package com.jarvvski.service.credit.domain

import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.BigInteger

@Service
class DecisionEngine(private val creditReportProviders: List<CreditReportProvider>) {
    interface CreditReportProvider {
        fun fetchSegment(personalIdentifier: PersonalIdentifier) : CreditReport
    }

    data class CreditReport(val provider : CreditReportSupplier, val segment: CreditSegment)

    enum class CreditSegment(val multiplier: Int?) {
        DEBT(null),
        ONE(100),
        TWO(200),
        THREE(300),
        UNKNOWN(null)
    }

    private object DecisionEngineConstants {
        val APPROVAL_SCORE : BigInteger = BigInteger.ONE
    }

    fun performDecision(creditApplication: CreditApplication) : CreditDecision {
        // 1. Get all user data
        val creditReportResults = creditReportProviders
            .map { it -> it.fetchSegment(creditApplication.user.identifier) }
            .filter { it -> it.segment != CreditSegment.UNKNOWN };

        // if no reports are found
        if (creditReportResults.isEmpty()) {
            return CreditDecision(Decision.DECLINED_UNKNOWN_CREDIT, null, emptyList())
        }

        val foundCreditReports = creditReportResults
            .filter { it -> it.segment != CreditSegment.DEBT };

        // if the reports just report 'debt'
        if (foundCreditReports.isEmpty()) {
            return CreditDecision(Decision.DECLINED_BAD_CREDIT, null, emptyList())
        }

        // 2. for each credit supplier, calculate if we would approve loan
        val creditScores = foundCreditReports
            .filter { specialSauce(it.segment, creditApplication.loan)
                .compareTo(DecisionEngineConstants.APPROVAL_SCORE) != -1
            }

        // 3. if none approve, return no
        if (creditScores.isEmpty()) {
            return CreditDecision(Decision.DECLINED_BAD_CREDIT, null, emptyList())
        }

        // 4. return the approval
        return CreditDecision(Decision.APPROVED, creditApplication.loan, creditScores.map { it.provider })
    }

    class DecisionEngineException(message : String, cause: Throwable? = null) : RuntimeException(message, cause)

    private fun specialSauce(creditSegment: CreditSegment, loan: Loan) : BigInteger {
        // credit score = (credit modifier / loan amount) * loan period

        // TODO: Calling #intValueExact on bigDecimal can throw exception
        return creditSegment.multiplier
            ?.let {
                BigDecimal(it)
                    .div(loan.amount.data)
                    .multiply(BigDecimal(loan.term.months))
                    .toBigInteger()
            }
            ?: let { throw DecisionEngineException("$creditSegment cannot be used to calculate internal credit score")}
    }

}