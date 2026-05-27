package id.my.apm.posyandu.ui.user

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.databinding.ActivityUserBinding
import id.my.apm.posyandu.repository.UserRepository
import id.my.apm.posyandu.ui.registration.RegistrationOrgActivity
import id.my.apm.posyandu.ui.registration.RegistrationPubActivity
import id.my.apm.posyandu.use_case.user.LogoutUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import id.my.apm.posyandu.utils.SessionManager
import javax.inject.Inject

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {

    private lateinit var viewModel: UserViewModel
    private lateinit var binding: ActivityUserBinding
    private lateinit var userType: String
    @Inject
    lateinit var repository: UserRepository

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val useCase = LogoutUseCase(repository)
        val sessionManager = SessionManager(this)
        viewModel = UserViewModel(useCase, sessionManager)

        viewModel.responseGet.observe(this) { resp ->
            userType = resp.userType
            binding.tvUserNama.text = resp.nama
            binding.tvUserTelp.text = resp.telp
            binding.tvUserAlamat.text = resp.alamat
            binding.tvUserStatus.text = if (userType == "1") "Anggota" else "Pengurus"

            if (resp.id == "1") {
                binding.tvUserIndNama.text = "Posisi"
                binding.btnUserLihatAkun.visibility = View.VISIBLE
            }
        }

        viewModel.errorGet.observe(this) { err ->
            if (err) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        val navBottom = binding.userBtnMenu
        navBottom.selectedItemId = R.id.menu_user
        navBottom.isItemHorizontalTranslationEnabled = true
        navBottom.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        navBottom.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    if (userType == "1") {
                        startActivity(Intent(this, PublicActivity::class.java))
                        finish()
                    } else if (userType == "2") {
                        startActivity(Intent(this, OrganizerActivity::class.java))
                        finish()
                    }
                    true
                }
                R.id.menu_daftar -> {
                    if (userType == "1") {
                        startActivity(Intent(this, RegistrationPubActivity::class.java))
                        finish()
                    } else if (userType == "2") {
                        startActivity(Intent(this, RegistrationOrgActivity::class.java))
                        finish()
                    }
                    true
                }
                R.id.menu_user -> {
                    true
                }
                else -> false
            }
        }

        binding.btnUserGantipass.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        binding.btnUserLihatAkun.setOnClickListener {
            startActivity(Intent(this, UserAccountActivity::class.java))
        }

        binding.btnUserLogout.setOnClickListener {
            val customDialog = findViewById<ConstraintLayout>(R.id.customDialog)
            val view = LayoutInflater.from(this).inflate(R.layout.custom_dialog, customDialog)
            val btn = view.findViewById<Button>(R.id.dialogButton)
            val title = view.findViewById<TextView>(R.id.dialogTitle)
            title.text = "Logout"
            val desc = view.findViewById<TextView>(R.id.dialogDesc)
            desc.text = "Yakin ingin keluar?"

            val builder = AlertDialog.Builder(this)
            builder.setView(view)
            val alertDialog = builder.create()

            btn.findViewById<Button>(R.id.dialogButton).setOnClickListener {
                alertDialog.dismiss()
                viewModel.logout()
            }
            if (alertDialog.window != null) {
                alertDialog.window?.setBackgroundDrawable(0.toDrawable())
            }
            alertDialog.show()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getSession()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }
}