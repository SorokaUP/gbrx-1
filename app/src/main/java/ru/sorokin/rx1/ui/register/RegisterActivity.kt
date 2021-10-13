package ru.sorokin.rx1.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import ru.sorokin.rx1.R
import ru.sorokin.rx1.databinding.ActivityMainBinding
import ru.sorokin.rx1.domain.Fio
import ru.sorokin.rx1.domain.User
import ru.sorokin.rx1.impl.UserRepoImpl
import ru.sorokin.rx1.ui.register.RegisterContract.RegisterViewState

class RegisterActivity : AppCompatActivity(), RegisterContract.View {
    private lateinit var binding: ActivityMainBinding
    private var presenter: RegisterContract.Presenter = RegisterPresenter(UserRepoImpl())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter.onAttach(this)
        initView()
    }

    private fun initView() {
        doSetLoginListener()
        doSetFioListener()
        doSetButtonsListener()
        doSetOtherListener()
    }

    private fun doSetLoginListener() {
        binding.loginInput.addTextChangedListener {
            presenter.onChangeLogin(it.toString())
        }
        binding.emailInput.addTextChangedListener {
            presenter.onChangeEmail(it.toString())
        }
    }

    private fun doSetFioListener() {
        binding.fnameInput.addTextChangedListener {
            presenter.onChangeFName(it.toString())
        }
        binding.lnameInput.addTextChangedListener {
            presenter.onChangeLName(it.toString())
        }
    }

    private fun doSetButtonsListener() {
        binding.btnSuccess.setOnClickListener {
            presenter.onSave(buildUser())
        }
        binding.btnCancel.setOnClickListener {
            presenter.onCancel()
        }
    }

    private fun doSetOtherListener() {
        binding.phoneInput.addTextChangedListener {
            presenter.onChangePhone(it.toString())
        }

        binding.passwordRequiredInput.addTextChangedListener {
            presenter.onChangePasswordRequired(binding.passwordInput.toString(), it.toString())
        }
    }

    private fun buildUser() = User(
            binding.loginInput.toString(),
            binding.passwordInput.toString(),
            binding.emailInput.toString(),
            Fio(
                binding.fnameInput.toString(),
                binding.lnameInput.toString()
            ),
            binding.phoneInput.toString()
        )

    override fun setState(state: RegisterViewState) {
        doHideControls()

        when (state) {
            RegisterViewState.LOADING -> binding.progressBar.isVisible = true
            RegisterViewState.IDLE -> binding.container.isVisible = true
            RegisterViewState.ERROR -> {
                binding.container.isVisible = true
                Snackbar.make(binding.root, R.string.error_message, Snackbar.LENGTH_SHORT).show()
            }
            RegisterViewState.SUCCESS -> binding.successImageView.isVisible = true
        }
    }

    private fun doHideControls() {
        binding.progressBar.isVisible = false
        binding.container.isVisible = false
        binding.successImageView.isVisible = false
    }

    override fun setUser(user: User) {
        binding.loginInput.setText(user.login)
        binding.passwordInput.setText(user.password)
        binding.emailInput.setText(user.email)
        binding.fnameInput.setText(user.fio?.fname)
        binding.lnameInput.setText(user.fio?.lname)
        binding.phoneInput.setText(user.phone)
    }

    override fun onFioError(code: Int) {
        val msg = getErrorByCode(code)
        if (code == RegisterEnums.FioError.NON_FIRST_NAME.value) {
            binding.fnameInput.error = msg
        }
        if (code == RegisterEnums.FioError.NON_LAST_NAME.value) {
            binding.lnameInput.error = msg
        }
    }

    override fun onPhoneError(code: Int) {
        binding.phoneInput.error = getErrorByCode(code)
    }

    override fun onLoginError(code: Int) {
        binding.loginInput.error = getErrorByCode(code)
    }

    override fun onEmailError(code: Int) {
        binding.emailInput.error = getErrorByCode(code)
    }

    override fun onPasswordError(code: Int) {
        val msg = getErrorByCode(code)
        if (code == RegisterEnums.PasswordError.DATA.value)
            binding.passwordInput.error = msg
        if (code == RegisterEnums.PasswordError.REQUIRE.value)
            binding.passwordRequiredInput.error = msg
    }

    private fun getErrorByCode(code: Int): String {
        return when (code) {
            RegisterEnums.FioError.NON_FIRST_NAME.value -> resources.getString(R.string.fio_error_non_first_name)
            RegisterEnums.FioError.NON_LAST_NAME.value -> resources.getString(R.string.fio_error_non_last_name)

            RegisterEnums.PhoneError.DATA.value -> resources.getString(R.string.phone_error_data)
            RegisterEnums.PhoneError.FORMAT.value -> resources.getString(R.string.phone_error_format)
            RegisterEnums.PhoneError.OTHER_CHARS.value -> resources.getString(R.string.phone_error_other_chars)

            RegisterEnums.PasswordError.DATA.value -> resources.getString(R.string.password_error_data)
            RegisterEnums.PasswordError.REQUIRE.value -> resources.getString(R.string.password_error_require)

            RegisterEnums.LoginError.DATA.value -> resources.getString(R.string.login_error_data)
            RegisterEnums.LoginError.FORMAT.value -> resources.getString(R.string.login_error_format)

            RegisterEnums.EmailError.DATA.value -> resources.getString(R.string.email_error_data)
            RegisterEnums.EmailError.LARGE.value -> resources.getString(R.string.email_error_large)
            RegisterEnums.EmailError.VALID.value -> resources.getString(R.string.email_error_valid)

            else -> "undefine"
        }
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }
}