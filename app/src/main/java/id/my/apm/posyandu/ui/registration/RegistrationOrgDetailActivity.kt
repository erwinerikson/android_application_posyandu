package id.my.apm.posyandu.ui.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.databinding.ActivityRegistrationOrgDetailBinding
import id.my.apm.posyandu.repository.RegistrationRepository
import id.my.apm.posyandu.use_case.registration.ChangeStatusRegistrationUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationOrgDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: RegistrationOrgDetailViewModel
    private lateinit var binding: ActivityRegistrationOrgDetailBinding
    @Inject
    lateinit var repository: RegistrationRepository
    private lateinit var status: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrationOrgDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val changeStatusRegistrationUseCase = ChangeStatusRegistrationUseCase(repository)
        viewModel = RegistrationOrgDetailViewModel(changeStatusRegistrationUseCase)

        viewModel.responseChangeStatus.observe(this) { resp ->
            if (!resp.error) {
                this.showToast(resp.message)
                if (status == "0") {
                    status = "1"
                    binding.tvPendaftaranDetailOrgStatus.text = getText(R.string.ubah_selesai)
                } else if (status == "1") {
                    back()
                }
            } else {
                this.showToast(resp.message)
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        val id = intent.getStringExtra("ID").toString()
        val idMin = intent.getStringExtra("ID_MIN").toString()
        val nama = intent.getStringExtra("NAMA")
        status = intent.getStringExtra("STATUS").toString()

        val antrian = (Integer.parseInt(id) - Integer.parseInt(idMin)) + 1

        binding.tvPendaftaranDetailOrgAntri.text = antrian.toString()
        binding.tvPendaftaranDetailOrgNama.text = nama

        if (status == "1") {
            binding.tvPendaftaranDetailOrgStatus.text = getText(R.string.ubah_selesai)
        }

        val switch = binding.scPendaftaranDetailOrgStatus
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val customDialog = findViewById<ConstraintLayout>(R.id.customDialogCancel)
                val view = LayoutInflater.from(this@RegistrationOrgDetailActivity).inflate(R.layout.custom_dialog_cancel, customDialog)
                val btnYes = view.findViewById<Button>(R.id.dialogButtonYes)
                val btnCancel = view.findViewById<Button>(R.id.dialogButtonCancel)
                val title = view.findViewById<TextView>(R.id.dialogCancelTitle)
                title.text = getText(R.string.ubah_status)
                val desc = view.findViewById<TextView>(R.id.dialogCancelDesc)
                if (status == "0") {
                    desc.text = getText(R.string.yakin_diubah_jadi_proses)
                } else if (status == "1") {
                    desc.text = getText(R.string.yakin_diubah_jadi_selesai)
                }

                val builder = AlertDialog.Builder(this@RegistrationOrgDetailActivity)
                builder.setView(view)
                val alertDialog = builder.create()

                btnYes.findViewById<Button>(R.id.dialogButtonYes).setOnClickListener {
                    alertDialog.dismiss()
                    switch.isChecked = false
                    if (AppUtils.isInternetAvailable(this)) {
                        if (status == "0") {
                            viewModel.changeStatus(id, "1")
                        } else if (status == "1") {
                            viewModel.changeStatus(id, "2")
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

        binding.ivPendaftaranDetailOrgToolbarBack.setOnClickListener { back() }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { back() }
        })
    }

    private fun back() {
        val intentBack = Intent()
        setResult(RESULT_OK, intentBack)
        finish()
    }
}