package com.jarvvski.service.credit.domain

data class User(val name: Name, val identifier: PersonalIdentifier)

data class Name(val data: String)
data class PersonalIdentifier(val data: Long)
