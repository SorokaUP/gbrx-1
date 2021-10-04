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
            this.view?.onFioError(10)
        }
    }

    override fun onChangeLName(value: String) {
        if (value?.length == 0) {
            this.view?.onFioError(20)
        }
    }

    override fun onChangePhone(value: String) {
        if (value.isEmpty()) {
            this.view?.onFioError(30)
        }
        else
        if (value.length > 11) {
            this.view?.onFioError(31)
        }
        else
        if (!value.isDigitsOnly())
            this.view?.onFioError(32)
    }

    override fun onCancel() {
        //
    }

    override fun onChangePasswordRequired(valueFirst: String, valueSecond: String) {
        if (valueFirst.isEmpty())
            this.view?.onPasswordError(40)
        if (valueFirst != valueSecond)
            this.view?.onPasswordError(41)
    }
}