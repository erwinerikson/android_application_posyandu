package id.my.apm.posyandu.ui.user

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.databinding.ActivityChangePasswordBinding
import id.my.apm.posyandu.repository.UserRepository
import id.my.apm.posyandu.use_case.user.ChangePasswordUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import id.my.apm.posyandu.utils.SessionManager
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var viewModel: ChangePasswordViewModel
    private lateinit var binding: ActivityChangePasswordBinding
    @Inject
    lateinit var repository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sessionManager = SessionManager(this)
        val useCase = ChangePasswordUseCase(repository)
        viewModel = ChangePasswordViewModel(useCase, sessionManager)

        viewModel.response.observe(this) { resp ->
            binding.btnGantiPassUser.isClickable = true
            this.showToast(resp.message)
            if (!resp.error) {
                finish()
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            binding.btnGantiPassUser.isClickable = true
            this.showToast(message)
        }

        val password = binding.editTextPasswordLamaUser
        val passwordNew = binding.editTextPasswordBaruUser
        val passwordNewAgain = binding.editTextKonfPasswordBaruUser
        binding.btnGantiPassUser.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(this)) {
                if (password.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(password, it, "Field ini tidak boleh kosong")
                } else if (passwordNew.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(passwordNew, it, "Field ini tidak boleh kosong")
                } else if (passwordNewAgain.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(passwordNewAgain, it, "Field ini tidak boleh kosong")
                } else if (passwordNew.text.toString().trim() != passwordNewAgain.text.toString().trim()) {
                    AppUtils.checkEt(passwordNewAgain, it, "Harus sama dengan password baru")
                } else {
                    viewModel.change(password.text.toString().trim(), passwordNew.text.toString().trim())
                }
            } else {
                this.showToast("Tidak ada koneksi internet")
            }
        }

        binding.changePassToolbarBack.setOnClickListener {
            finish()
        }
    }
}