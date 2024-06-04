package com.jarvvski.service.credit.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(LoanTermConfiguration.LoanTermProperties::class)
class LoanTermConfiguration {

    @ConfigurationProperties(prefix = "loans")
    data class LoanTermProperties(
        val maxAmount: Long = 10000L,
        val minAmount: Long = 2000L,
        val maxTerm: Int = 60,
        val minTerm: Int = 12)
}
