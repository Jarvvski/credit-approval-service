package com.jarvvski.service.credit.domain

import com.jarvvski.service.credit.configuration.LoanTermConfiguration
import io.konform.validation.Validation
import io.konform.validation.ValidationResult
import io.konform.validation.jsonschema.maximum
import io.konform.validation.jsonschema.minimum
import org.springframework.stereotype.Service
import java.math.BigDecimal

data class CreditApplication(val user: User, val loan: Loan)

data class Loan(val amount: Money, val term: LoanTerm)
data class Money(val data: BigDecimal) {
    fun add(addition : Money) : Money {
        return Money(this.data.add(addition.data))
    }

    fun min(minus : Money) : Money {
        return Money(this.data.min(minus.data))
    }
}
data class LoanTerm(val months: Int)


@Service
class CreditApplicationValidator(private val loanTermProperties: LoanTermConfiguration.LoanTermProperties) {
    fun validate(loan: Loan): ValidationResult<Loan> {
        val validation = Validation {
            Loan::amount {
                Money::data {
                    maximum(loanTermProperties.maxAmount) hint "Sorry, we do not support loan amounts greater than '{value}'"
                    minimum(loanTermProperties.minAmount) hint "Sorry, we do not support loan amounts lower than '{value}'"
                }
            }
            Loan::term {
                LoanTerm::months {
                    maximum(loanTermProperties.maxTerm) hint "Sorry, we do not support loan terms greater than '{value}'"
                    minimum(loanTermProperties.minTerm) hint "Sorry, we do not support loan terms shorter than '{value}'"
                }
            }
        }
        return validation.validate(loan);
    }
}