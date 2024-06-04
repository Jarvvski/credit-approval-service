package com.jarvvski.service.credit.domain

data class CreditDecision(val decision: Decision, val loan: Loan?, val creditReportSupplier: List<CreditReportSupplier>)

enum class Decision(){
    APPROVED, DECLINED_BAD_CREDIT, DECLINED_UNKNOWN_CREDIT
}
data class CreditReportSupplier(val data : String)