package com.jarvvski.service.credit

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@Theme(value = "my-theme", variant= Lumo.DARK)
class CreditServiceApplication : AppShellConfigurator

fun main(args: Array<String>) {
    runApplication<CreditServiceApplication>(*args)
}
