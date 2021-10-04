package ru.sorokin.rx1.ui.register

import ru.sorokin.rx1.domain.User

class RegisterContract {
    enum class RegisterViewState {
        IDLE, LOADING, SUCCESS, ERROR
    }

    interface View {
        fun setState(state: RegisterViewState)
        fun setUser(user: User)

        fun onFioError(code: Int)
        fun onPhoneError(code: Int)
        fun onPasswordError(code: Int)
    }

    interface Presenter {
        fun onAttach(view: RegisterContract.View)
        fun onDetach()

        fun onSave(user: User)
        fun onCancel()

        fun onChangeFName(value: String)
        fun onChangeLName(value: String)
        fun onChangePhone(value: String)
        fun onChangePasswordRequired(valueFirst: String, valueSecond: String)
    }
}