package com.jarvvski.service.credit.domain

import com.jarvvski.service.credit.configuration.LoanTermConfiguration
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.math.RoundingMode

@Service
class DecisionEngine(
    private val loanTermProperties: LoanTermConfiguration.LoanTermProperties,
    private val creditReportProviders: List<CreditReportProvider>
) {
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

        // 4. if we have some approvals, lets see how high we can take it
        val provider = creditScores.first()
        val maxLoan = findMaxAmount(provider.segment, creditApplication.loan)

        // 5. return the approval
        return CreditDecision(Decision.APPROVED, maxLoan, creditScores.map { it.provider })
    }

    class DecisionEngineException(message : String, cause: Throwable? = null) : RuntimeException(message, cause)

    private fun specialSauce(creditSegment: CreditSegment, loan: Loan) : BigInteger {
        // credit score = (credit modifier / loan amount) * loan period

        // TODO: Calling #intValueExact on bigDecimal can throw exception
        return creditSegment.multiplier
            ?.let {
                val mathContext = MathContext(10, RoundingMode.HALF_UP)
                val creditSegmentAmount = it.toBigDecimal()
                val loanAmount = loan.amount.data
                val loanTermInMonths = loan.term.months.toBigDecimal()

                val step1 = creditSegmentAmount.divide(loanAmount, mathContext)
                val step2 = step1.multiply(loanTermInMonths)
                step2.toBigInteger()
            }
            ?: let { throw DecisionEngineException("$creditSegment cannot be used to calculate internal credit score")}
    }

    fun findMaxAmount(creditSegment: CreditSegment, loan: Loan) : Loan {
        var start = loan.amount.data
        var end   = loanTermProperties.maxAmount.toBigDecimal()
        while (start < end) {
            val middle = start + (end - start + BigDecimal.ONE) / BigDecimal.TWO
            val newLoan = Loan(Money(middle), loan.term)
            if (specialSauce(creditSegment, newLoan).compareTo(DecisionEngineConstants.APPROVAL_SCORE) != -1) {
                start = middle
            } else {
                end = middle - BigDecimal.ONE
            }
        }
        return Loan(Money(start), loan.term)
    }

}