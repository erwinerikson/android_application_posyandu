package id.my.apm.posyandu.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.databinding.ActivityRegisterBinding
import id.my.apm.posyandu.repository.UserRepository
import id.my.apm.posyandu.use_case.user.RegisterUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivityRegisterBinding
    @Inject
    lateinit var repository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val useCase = RegisterUseCase(repository)
        viewModel = RegisterViewModel(useCase)

        viewModel.response.observe(this) { resp ->
            binding.buttonRegister.isClickable = true
            this.showToast(resp.message)
            if (!resp.error) {
                //startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            binding.buttonRegister.isClickable = true
            this.showToast(message)
        }

        val name = binding.eTRegName
        val address = binding.eTRegAddress
        val phone = binding.eTRegPhone
        val username = binding.eTRegUsername
        val password = binding.eTRegPassword
        val check = binding.cbPrivacy

        binding.buttonRegister.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(this)) {
                if (name.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(name, it, "Field ini tidak boleh kosong")
                } else if (address.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(address, it, "Field ini tidak boleh kosong")
                } else if (phone.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(phone, it, "Field ini tidak boleh kosong")
                } else if (username.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(username, it, "Field ini tidak boleh kosong")
                } else if (password.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(password, it, "Field ini tidak boleh kosong")
                } else if (check.isChecked) {
                    viewModel.register(name.text.toString().trim(), address.text.toString().trim(),
                        phone.text.toString().trim(), username.text.toString().trim(), password.text.toString().trim())
                } else {
                    this.showToast("Anda belum mencentang setuju")
                    it.isClickable = true
                }
            } else {
                this.showToast("Tidak ada koneksi internet")
                it.isClickable = true
            }
        }

        binding.tvRegPrivacy.setOnClickListener {
            this.showToast("di klik")
        }

        binding.regToolbarBack.setOnClickListener {
            finish()
        }
    }
}