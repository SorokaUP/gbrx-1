package ru.sorokin.rx1.ui.register

sealed class RegisterEnums {
    enum class FioError(val value: Int) {
        NON_FIRST_NAME(10),
        NON_LAST_NAME(20)
    }

    enum class PhoneError(val value: Int) {
        DATA(30),
        FORMAT(31),
        OTHER_CHARS(32)
    }

    enum class PasswordError(val value: Int) {
        DATA(40),
        REQUIRE(41)
    }

    enum class LoginError(val value: Int) {
        DATA(50),
        FORMAT(51)
    }

    enum class EmailError(val value: Int) {
        DATA(60),
        LARGE(61),
        VALID(62)
    }
}