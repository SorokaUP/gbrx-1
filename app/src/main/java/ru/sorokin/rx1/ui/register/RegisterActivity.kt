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
        binding.btnSuccess.setOnClickListener {
            presenter.onSave(buildUser())
        }
        binding.btnCancel.setOnClickListener {
            presenter.onCancel()
        }
        binding.phone.addTextChangedListener {
            presenter.onChangePhone(it.toString())
        }
        binding.fname.addTextChangedListener {
            presenter.onChangeFName(it.toString())
        }
        binding.lname.addTextChangedListener {
            presenter.onChangeLName(it.toString())
        }
        binding.passwordRequired.addTextChangedListener {
            presenter.onChangePasswordRequired(binding.password.toString(), it.toString())
        }
    }

    private fun buildUser() = User(
            binding.login.toString(),
            binding.password.toString(),
            binding.email.toString(),
            Fio(
                binding.fname.toString(),
                binding.lname.toString()
            ),
            binding.phone.toString()
        )

    override fun setState(state: RegisterViewState) {
        binding.progressBar.isVisible = false
        binding.container.isVisible = false
        binding.successImage.isVisible = false

        when (state) {
            RegisterViewState.LOADING -> binding.progressBar.isVisible = true
            RegisterViewState.IDLE -> binding.container.isVisible = true
            RegisterViewState.ERROR -> {
                binding.container.isVisible = true
                Snackbar.make(binding.root, "Error", Snackbar.LENGTH_SHORT).show()
            }
            RegisterViewState.SUCCESS -> binding.successImage.isVisible = true
        }
    }

    override fun setUser(user: User) {
        binding.login.setText(user.login)
        binding.password.setText(user.password)
        binding.email.setText(user.email)
        binding.fname.setText(user.fio?.fname)
        binding.lname.setText(user.fio?.lname)
        binding.phone.setText(user.phone)
    }

    override fun onFioError(code: Int) {
        val msg = getErrorByCode(code)
        if (code in 10..19) {
            binding.fname.error = msg
        }
        if (code in 20..29) {
            binding.lname.error = msg
        }
    }

    override fun onPhoneError(code: Int) {
        binding.phone.error = getErrorByCode(code)
    }

    override fun onPasswordError(code: Int) {
        val msg = getErrorByCode(code)
        if (code == 40)
            binding.password.error = msg
        if (code == 41)
            binding.passwordRequired.error = msg
    }

    private fun getErrorByCode(code: Int): String {
        return when (code) {
            0 -> ""

            10 -> "Не указано Имя"
            20 -> "Не указана Фамилия"

            30 -> "Не введен номер телефона"
            31 -> "Номер телефона не соответствует формату"
            32 -> "Номер телефона содержит запрещенные символы"

            40 -> "Не введен пароль"
            41 -> "Пароли не совпадают"

            else -> "undefine"
        }
    }

    override fun onDestroy() {
        presenter.onDetach()
        super.onDestroy()
    }
}