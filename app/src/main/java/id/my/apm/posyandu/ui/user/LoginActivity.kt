package id.my.apm.posyandu.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.MainActivity
import id.my.apm.posyandu.R
import id.my.apm.posyandu.databinding.ActivityLoginBinding
import id.my.apm.posyandu.repository.UserRepository
import id.my.apm.posyandu.use_case.user.LoginUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import id.my.apm.posyandu.utils.SessionManager
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    @Inject
    lateinit var repository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val useCase = LoginUseCase(repository)
        val sessionManager = SessionManager(this)
        viewModel = LoginViewModel(useCase, sessionManager)

        /*viewModel.responseCheck.observe(this) { resp ->
            when (resp?.userType) {
                "1" -> {
                    startActivity(Intent(this, PublicActivity::class.java))
                    finish()
                }
                "2" -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }*/

        viewModel.response.observe(this) { resp ->
            binding.buttonLogin.isClickable = true
            if (!resp!!.error) {
                this.showToast(resp.message)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                this.showToast(resp.message)
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            binding.buttonLogin.isClickable = true
            this.showToast(message)
        }

        val username = binding.editTextUsername
        val password = binding.editTextPassword
        val btnLogin = binding.buttonLogin
        btnLogin.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(this)) {
                if (username.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(username, it, "Field ini tidak boleh kosong")
                } else if (password.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(password, it, "Field ini tidak boleh kosong")
                } else {
                    viewModel.login(username.text.toString().trim(), password.text.toString().trim())
                }
            } else {
                this.showToast("Tidak ada koneksi internet")
            }
        }

        val tvDaftar = binding.tvDaftar
        tvDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        //viewModel.checkSession()
    }
}