package ru.sorokin.rx1.domain

data class User(
    val login: String,
    val password: String,
    val email: String,
    val fio: Fio?,
    val phone: String
)

data class Fio(
    val fname: String,
    val lname: String
)