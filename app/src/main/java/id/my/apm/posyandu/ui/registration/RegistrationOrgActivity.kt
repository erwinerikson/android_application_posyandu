package id.my.apm.posyandu.ui.registration

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.adapter.RegistrationOrgAdapter
import id.my.apm.posyandu.databinding.ActivityRegistrationOrgBinding
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.repository.RegistrationRepository
import id.my.apm.posyandu.ui.user.LoginActivity
import id.my.apm.posyandu.ui.user.OrganizerActivity
import id.my.apm.posyandu.ui.user.UserActivity
import id.my.apm.posyandu.use_case.registration.ChangeStatusScheduleUseCase
import id.my.apm.posyandu.use_case.registration.GetRegistrationOrgUseCase
import id.my.apm.posyandu.use_case.registration.RegistrationOrgUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import id.my.apm.posyandu.utils.SessionManager
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationOrgActivity : AppCompatActivity() {

    private lateinit var viewModel: RegistrationOrgViewModel
    private lateinit var binding: ActivityRegistrationOrgBinding
    @Inject
    lateinit var repository: RegistrationRepository
    private var listRegistration = ArrayList<RegistrationPub>()
    private var idJadwal = "0"
    private var kode = "0"
    private var idMin = "0"

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.getDataRegistration()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrationOrgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val registrationOrgUseCase = RegistrationOrgUseCase(repository)
        val changeStatusScheduleUseCase = ChangeStatusScheduleUseCase(repository)
        val getRegistrationOrgUseCase = GetRegistrationOrgUseCase(repository)
        val sessionManager = SessionManager(this)
        viewModel = RegistrationOrgViewModel(registrationOrgUseCase, changeStatusScheduleUseCase, getRegistrationOrgUseCase, sessionManager)

        viewModel.responseGet.observe(this) { resp ->
            if (resp != null) {
                if (resp.id == "1" && kode == "1") {
                    binding.llDaftarOrgStatus.visibility = View.VISIBLE
                } else if (resp.id == "1" && kode == "2") {
                    binding.tvDaftarOrgStatus.text = getText(R.string.ubah_selesai)
                    binding.llDaftarOrgStatus.visibility = View.VISIBLE
                } else {
                    binding.llDaftarOrgStatus.visibility = View.GONE
                }
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        viewModel.responseChangeStatus.observe(this) { resp ->
            if (resp.error) {
                this.showToast(resp.message)
            } else {
                viewModel.checkRegistration()
            }
        }

        viewModel.responseRegistrationOrg.observe(this) { resp ->
            if (resp.error) {
                this.showToast(resp.message)
            } else {
                idJadwal = resp.id
                kode = resp.kode
                idMin = resp.idmin

                binding.llDaftarOrg.visibility = View.VISIBLE

                when (kode) {
                    "1" -> {
                        binding.llDaftarOrgStatus.visibility = View.GONE
                        binding.rlDaftarOrgPendaftaran.visibility = View.GONE
                        binding.llDaftarOrgData.visibility = View.GONE
                        binding.llDaftarOrgNoData.visibility = View.VISIBLE
                        binding.tvDaftarOrgNoData.text = getText(R.string.ada_layanan)
                        viewModel.getSession()
                    }
                    "2" -> {
                        binding.llDaftarOrgStatus.visibility = View.GONE
                        binding.rlDaftarOrgPendaftaran.visibility = View.VISIBLE
                        binding.llDaftarOrgData.visibility = View.GONE
                        binding.llDaftarOrgNoData.visibility = View.GONE
                        binding.tvDaftarOrgNoData.text = ""
                        viewModel.getDataRegistration()
                    }
                    "3" -> {
                        binding.llDaftarOrgStatus.visibility = View.GONE
                        binding.rlDaftarOrgPendaftaran.visibility = View.GONE
                        binding.llDaftarOrgData.visibility = View.GONE
                        binding.llDaftarOrgNoData.visibility = View.VISIBLE
                        binding.tvDaftarOrgNoData.text = getText(R.string.layanan_selesai)
                        viewModel.getSession()
                    }
                    else -> {
                        binding.llDaftarOrgStatus.visibility = View.GONE
                        binding.rlDaftarOrgPendaftaran.visibility = View.GONE
                        binding.llDaftarOrgData.visibility = View.GONE
                        binding.llDaftarOrgNoData.visibility = View.VISIBLE
                        binding.tvDaftarOrgNoData.text = getText(R.string.tidak_ada_layanan)
                    }
                }
            }
        }

        viewModel.responseGetDataRegistration.observe(this) { resp ->
            listRegistration.clear()
            viewModel.getSession()
            if (resp.isNotEmpty()) {
                listRegistration = resp
                setuprecyclerview(idMin, listRegistration)
                binding.llDaftarOrgJumlah.visibility = View.VISIBLE
                binding.tvDaftarOrgJumlah.text = resp.size.toString()
                binding.llDaftarOrgData.visibility = View.VISIBLE
            } else {
                binding.llDaftarOrgData.visibility = View.GONE
                this.showToast("Tidak ada data")
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        val navBottom = binding.daftarOrgBtnMenu
        navBottom.selectedItemId = R.id.menu_daftar
        navBottom.isItemHorizontalTranslationEnabled = true
        navBottom.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        navBottom.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    startActivity(Intent(this, OrganizerActivity::class.java))
                    finish()
                    true
                }
                R.id.menu_daftar -> {
                    true
                }
                R.id.menu_user -> {
                    startActivity(Intent(this, UserActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        val switch = binding.scDaftarOrgStatus
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val customDialog = findViewById<ConstraintLayout>(R.id.customDialogCancel)
                val view = LayoutInflater.from(this@RegistrationOrgActivity).inflate(R.layout.custom_dialog_cancel, customDialog)
                val btnYes = view.findViewById<Button>(R.id.dialogButtonYes)
                val btnCancel = view.findViewById<Button>(R.id.dialogButtonCancel)
                val title = view.findViewById<TextView>(R.id.dialogCancelTitle)
                title.text = getText(R.string.ubah_status)
                val desc = view.findViewById<TextView>(R.id.dialogCancelDesc)
                if (kode == "1") {
                    desc.text = getText(R.string.yakin_diubah_jadi_berlangsung)
                } else if (kode == "2") {
                    desc.text = getText(R.string.yakin_diubah_jadi_selesai)
                }

                val builder = AlertDialog.Builder(this@RegistrationOrgActivity)
                builder.setView(view)
                val alertDialog = builder.create()

                btnYes.findViewById<Button>(R.id.dialogButtonYes).setOnClickListener {
                    alertDialog.dismiss()
                    switch.isChecked = false
                    if (AppUtils.isInternetAvailable(this)) {
                        if (kode == "1") {
                            viewModel.changeStatus(idJadwal, "1")
                        } else if (kode == "2") {
                            viewModel.changeStatus(idJadwal, "2")
                        }
                    } else {
                        this.showToast("Tidak ada koneksi internet")
                    }
                }
                btnCancel.findViewById<Button>(R.id.dialogButtonCancel).setOnClickListener {
                    alertDialog.dismiss()
                    switch.isChecked = false
                }

                if (alertDialog.window != null) {
                    alertDialog.window?.setBackgroundDrawable(0.toDrawable())
                }
                alertDialog.show()
            }
        }

        val swipeRefresh = binding.daftarOrgSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llDaftarOrg.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.checkRegistration()
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.checkRegistration()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun setuprecyclerview(idMin: String, data: ArrayList<RegistrationPub>) {
        val rvBabySelect = binding.rvDaftarOrg
        rvBabySelect.layoutManager = LinearLayoutManager(this)
        val adapter = RegistrationOrgAdapter(idMin, data)
        adapter.setOnItemClickCallback(object : RegistrationOrgAdapter.OnItemClickCallback {
            override fun onItemClicked(data: RegistrationPub) {
                val intent = Intent(this@RegistrationOrgActivity, RegistrationOrgDetailActivity::class.java).apply {
                    putExtra("ID", data.id)
                    putExtra("ID_MIN", idMin)
                    putExtra("NAMA", data.nama)
                    putExtra("STATUS", data.status)
                }
                activityLauncher.launch(intent)
            }
        })
        rvBabySelect.adapter = adapter
    }
}