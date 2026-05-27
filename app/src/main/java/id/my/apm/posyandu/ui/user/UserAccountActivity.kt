package id.my.apm.posyandu.ui.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.adapter.user.UserAccountAdapter
import id.my.apm.posyandu.databinding.ActivityUserAccountBinding
import id.my.apm.posyandu.model.User
import id.my.apm.posyandu.repository.UserRepository
import id.my.apm.posyandu.use_case.user.ChangeAccountUseCase
import id.my.apm.posyandu.use_case.user.GetUsersUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class UserAccountActivity : AppCompatActivity() {

    private lateinit var viewModel: UserAccountViewModel
    private lateinit var binding: ActivityUserAccountBinding
    @Inject
    lateinit var repository: UserRepository
    private var listUser = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val getUsersUseCase = GetUsersUseCase(repository)
        val changeAccountUseCase = ChangeAccountUseCase(repository)
        viewModel = UserAccountViewModel(getUsersUseCase, changeAccountUseCase)

        viewModel.responseGetUsers.observe(this) { resp ->
            listUser.clear()
            if (resp.isNotEmpty()) {
                listUser = resp
                setuprecyclerview(listUser)
            } else {
                this.showToast("Tidak ada data")
            }
        }

        viewModel.responseChange.observe(this) { resp ->
            this.showToast(resp.message)
            if (!resp.error) {
                finish()
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        val swipeRefresh = binding.lihatAkunSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llLihatAkun.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llLihatAkun.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.getUsers()
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        binding.ivLihatAkunToolbarBack.setOnClickListener {
            finish()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getUsers()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun setuprecyclerview(data: ArrayList<User>) {
        val rvUser = binding.rvUserAccount
        rvUser.layoutManager = LinearLayoutManager(this)
        val adapter = UserAccountAdapter(data)
        adapter.setOnItemClickCallback(object : UserAccountAdapter.OnItemClickCallback {
            @SuppressLint("SetTextI18n")
            override fun onItemClicked(data: User) {
                val type = if (data.userType == "1") "2" else "1"
                val name = if (type == "1") "anggota" else "pengurus"
                val customDialog = findViewById<ConstraintLayout>(R.id.customDialogCancel)
                val view = LayoutInflater.from(this@UserAccountActivity).inflate(R.layout.custom_dialog_cancel, customDialog)
                val btnYes = view.findViewById<Button>(R.id.dialogButtonYes)
                val btnCancel = view.findViewById<Button>(R.id.dialogButtonCancel)
                val title = view.findViewById<TextView>(R.id.dialogCancelTitle)
                title.text = data.nama
                val desc = view.findViewById<TextView>(R.id.dialogCancelDesc)
                desc.text = "Yakin ingin ganti jadi ${name}?"

                val builder = AlertDialog.Builder(this@UserAccountActivity)
                builder.setView(view)
                val alertDialog = builder.create()

                btnYes.findViewById<Button>(R.id.dialogButtonYes).setOnClickListener {
                    alertDialog.dismiss()
                    viewModel.changeAccount(data.id, type)
                }
                btnCancel.findViewById<Button>(R.id.dialogButtonCancel).setOnClickListener {
                    alertDialog.dismiss()
                }

                if (alertDialog.window != null) {
                    alertDialog.window?.setBackgroundDrawable(0.toDrawable())
                }
                alertDialog.show()
            }
        })
        rvUser.adapter = adapter
    }
}