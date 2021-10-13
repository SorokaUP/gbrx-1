package ru.sorokin.rx1.ui.register

import android.os.Handler
import android.os.Looper
import androidx.core.text.isDigitsOnly
import ru.sorokin.rx1.domain.User
import ru.sorokin.rx1.impl.UserRepoImpl

private const val LOADING_DURATION = 2000L

class RegisterPresenter(private val room: UserRepoImpl) : RegisterContract.Presenter {
    private var view: RegisterContract.View? = null

    override fun onAttach(view: RegisterContract.View) {
        this.view = view
        this.view?.setState(RegisterContract.RegisterViewState.IDLE)
    }

    override fun onDetach() {
        this.view = null
    }

    override fun onSave(user: User) {
        this.view?.setState(RegisterContract.RegisterViewState.LOADING)
        Handler(Looper.getMainLooper()).postDelayed({
            this.view?.setState(RegisterContract.RegisterViewState.ERROR)
        }, LOADING_DURATION,)
    }

    override fun onChangeFName(value: String) {
        if (value?.length == 0) {
            this.view?.onFioError(RegisterEnums.FioError.NON_FIRST_NAME.value)
        }
    }

    override fun onChangeLName(value: String) {
        if (value?.length == 0) {
            this.view?.onFioError(RegisterEnums.FioError.NON_LAST_NAME.value)
        }
    }

    override fun onChangePhone(value: String) {
        if (value.isEmpty()) {
            this.view?.onPhoneError(RegisterEnums.PhoneError.DATA.value)
        }
        else
        if (value.length > 11) {
            this.view?.onPhoneError(RegisterEnums.PhoneError.FORMAT.value)
        }
        else
        if (!value.isDigitsOnly())
            this.view?.onPhoneError(RegisterEnums.PhoneError.OTHER_CHARS.value)
    }

    override fun onCancel() {
        //
    }

    override fun onChangePasswordRequired(valueFirst: String, valueSecond: String) {
        if (valueFirst.isEmpty())
            this.view?.onPasswordError(RegisterEnums.PasswordError.DATA.value)
        if (valueFirst != valueSecond)
            this.view?.onPasswordError(RegisterEnums.PasswordError.REQUIRE.value)
    }

    override fun onChangeLogin(value: String) {
        if (value.isEmpty()) {
            this.view?.onLoginError(RegisterEnums.LoginError.DATA.value)
        }
        else
        if (value.length > 80) {
            this.view?.onLoginError(RegisterEnums.LoginError.FORMAT.value)
        }
    }

    override fun onChangeEmail(value: String) {
        if (value.isEmpty()) {
            this.view?.onEmailError(RegisterEnums.EmailError.DATA.value)
        }
        else
        if (value.length > 80) {
            this.view?.onLoginError(RegisterEnums.EmailError.LARGE.value)
        }
        else
        if (value != "@" && value != ".ru") {
            this.view?.onLoginError(RegisterEnums.EmailError.VALID.value)
        }
    }
}