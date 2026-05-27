package id.my.apm.posyandu.ui.user

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.databinding.ActivityEditUserBinding
import id.my.apm.posyandu.repository.UserRepository
import id.my.apm.posyandu.use_case.user.GetUserUseCase
import javax.inject.Inject

@AndroidEntryPoint // Tambahkan ini
class EditUserActivity : AppCompatActivity() {

    private lateinit var viewModel: GetUserViewModel
    private lateinit var binding: ActivityEditUserBinding
    @Inject
    lateinit var repository: UserRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inisialisasi manual (lebih baik gunakan DI seperti Hilt)
        //val repository = UserRepository(this)
        val useCase = GetUserUseCase(repository)
        viewModel = GetUserViewModel(useCase)

        // Observe Data
        viewModel.userData.observe(this) { user ->
            // Update UI dengan data user
        }

        // Trigger request
        viewModel.fetchUser("1")
    }
}