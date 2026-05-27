package id.my.apm.posyandu.ui.elderly

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.adapter.elderly.ElderlyPubSelectAdapter
import id.my.apm.posyandu.databinding.ActivityElderlyOrgDetailBinding
import id.my.apm.posyandu.model.ElderlyPub
import id.my.apm.posyandu.repository.ElderlyRepository
import id.my.apm.posyandu.repository.RegistrationRepository
import id.my.apm.posyandu.ui.baby.BabyActionImmunizationActivity
import id.my.apm.posyandu.ui.data.ActionCheckActivity
import id.my.apm.posyandu.ui.data.ActionMedicationActivity
import id.my.apm.posyandu.use_case.elderly.GetElderlyDataUseCase
import id.my.apm.posyandu.use_case.elderly.GetElderlyPubUseCase
import id.my.apm.posyandu.use_case.registration.ChangeStatusRegistrationUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import java.time.Year
import javax.inject.Inject

@AndroidEntryPoint
class ElderlyOrgDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: ElderlyOrgDetailViewModel
    private lateinit var binding: ActivityElderlyOrgDetailBinding
    @Inject
    lateinit var repository: ElderlyRepository
    @Inject
    lateinit var regRepository: RegistrationRepository
    private var listElderlySelect = ArrayList<ElderlyPub>()
    private lateinit var daftarTindakan: List<String>
    private var idReg = "0"
    private var idUser = "0"
    private var idMin = "0"
    private var status = "0"

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.rvLansiaDetOrg.visibility = View.GONE
            binding.llLansiaDetOrgData.visibility = View.GONE
            binding.llLansiaDetOrgStatus.visibility = View.GONE
            binding.rlLansiaDetOrgDropTindakan.visibility = View.GONE
            viewModel.getElderlyData(idUser)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityElderlyOrgDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val getElderlyPubUseCase = GetElderlyPubUseCase(repository)
        val getElderlyDataUseCase = GetElderlyDataUseCase(repository)
        val changeStatusRegistrationUseCase = ChangeStatusRegistrationUseCase(regRepository)
        viewModel = ElderlyOrgDetailViewModel(getElderlyPubUseCase, getElderlyDataUseCase, changeStatusRegistrationUseCase)

        idReg = intent.getStringExtra("ID").toString()
        idUser = intent.getStringExtra("ID_USER").toString()
        idMin = intent.getStringExtra("ID_MIN").toString()
        daftarTindakan = listOf("Silahkan pilih tindakan", "Periksa", "Obat")
        status = intent.getStringExtra("STATUS").toString()

        viewModel.responseGetElderlySelect.observe(this) { resp ->
            if (resp != null) {
                binding.tvLansiaDetOrgNama.text = resp.nama
                binding.tvLansiaDetOrgUmur.text = (Year.now().value - Integer.parseInt(resp.tahunLahir)).toString()
                binding.tvLansiaDetOrgJk.text = if (resp.jk === "L") "Laki-laki" else "Perempuan"
                binding.tvLansiaDetOrgNik.text = resp.nik.ifEmpty { "(tidak terdata)" }
                binding.tvLansiaDetOrgKk.text = resp.kk.ifEmpty { "(tidak terdata)" }
                binding.tvLansiaDetOrgBpjs.text = resp.bpjs.ifEmpty { "(tidak terdata)" }
                binding.llLansiaDetOrgData.visibility = View.VISIBLE
                binding.llLansiaDetOrg.visibility = View.VISIBLE

                when (status) {
                    "0" -> {
                        binding.llLansiaDetOrgStatus.visibility = View.VISIBLE
                        binding.rlLansiaDetOrgDropTindakan.visibility = View.GONE
                    }
                    "1" -> {
                        binding.tvLansiaDetOrgStatus.text = getText(R.string.ubah_selesai)
                        dropDownSpinner(resp)
                        binding.llLansiaDetOrgStatus.visibility = View.GONE
                        binding.rlLansiaDetOrgDropTindakan.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.llLansiaDetOrgStatus.visibility = View.GONE
                        binding.rlLansiaDetOrgDropTindakan.visibility = View.GONE
                    }
                }
            } else {
                this.showToast("Tidak ada data")
            }
        }

        viewModel.responseGetElderlyData.observe(this) { resp ->
            if (resp.size > 1) {
                listElderlySelect.clear()
                listElderlySelect = resp
                setuprecyclerview(listElderlySelect)
                binding.llLansiaDetOrgData.visibility = View.GONE
                binding.rvLansiaDetOrg.visibility = View.VISIBLE
                binding.llLansiaDetOrg.visibility = View.VISIBLE
            } else if (resp.size == 1) {
                //idElderly = resp[0].id
                binding.tvLansiaDetOrgNama.text = resp[0].nama
                binding.tvLansiaDetOrgUmur.text = (Year.now().value - Integer.parseInt(resp[0].tahunLahir)).toString()
                binding.tvLansiaDetOrgJk.text = if (resp[0].jk === "L") "Laki-laki" else "Perempuan"
                binding.tvLansiaDetOrgNik.text = resp[0].nik.ifEmpty { "(tidak terdata)" }
                binding.tvLansiaDetOrgKk.text = resp[0].kk.ifEmpty { "(tidak terdata)" }
                binding.tvLansiaDetOrgBpjs.text = resp[0].bpjs.ifEmpty { "(tidak terdata)" }
                binding.llLansiaDetOrgData.visibility = View.VISIBLE
                binding.rvLansiaDetOrg.visibility = View.GONE
                binding.llLansiaDetOrg.visibility = View.VISIBLE

                when (status) {
                    "0" -> {
                        binding.llLansiaDetOrgStatus.visibility = View.VISIBLE
                        binding.rlLansiaDetOrgDropTindakan.visibility = View.GONE
                    }
                    "1" -> {
                        binding.tvLansiaDetOrgStatus.text = getText(R.string.ubah_selesai)
                        dropDownSpinner(resp[0])
                        binding.llLansiaDetOrgStatus.visibility = View.GONE
                        binding.rlLansiaDetOrgDropTindakan.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.llLansiaDetOrgStatus.visibility = View.GONE
                        binding.rlLansiaDetOrgDropTindakan.visibility = View.GONE
                    }
                }
            } else {
                binding.llLansiaDetOrgData.visibility = View.GONE
                binding.llLansiaDetOrg.visibility = View.VISIBLE
                this.showToast("Tidak ada data")
            }
        }

        viewModel.responseChangeStatus.observe(this) { resp ->
            if (!resp.error) {
                this.showToast(resp.message)
                if (status == "0") {
                    status = "1"
                }
                binding.rvLansiaDetOrg.visibility = View.GONE
                binding.llLansiaDetOrgData.visibility = View.GONE
                binding.llLansiaDetOrgStatus.visibility = View.GONE
                binding.rlLansiaDetOrgDropTindakan.visibility = View.GONE
                viewModel.getElderlyData(idUser)
            } else {
                this.showToast(resp.message)
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        val switch = binding.scLansiaDetOrgStatus
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val customDialog = findViewById<ConstraintLayout>(R.id.customDialogCancel)
                val view = LayoutInflater.from(this@ElderlyOrgDetailActivity).inflate(R.layout.custom_dialog_cancel, customDialog)
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

                val builder = AlertDialog.Builder(this@ElderlyOrgDetailActivity)
                builder.setView(view)
                val alertDialog = builder.create()

                btnYes.findViewById<Button>(R.id.dialogButtonYes).setOnClickListener {
                    alertDialog.dismiss()
                    switch.isChecked = false
                    if (AppUtils.isInternetAvailable(this)) {
                        if (status == "0") {
                            viewModel.changeStatus(idReg, "1")
                        } else if (status == "1") {
                            viewModel.changeStatus(idReg, "2")
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

        val swipeRefresh = binding.lansiaDetOrgSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llLansiaDetOrg.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llLansiaDetOrg.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.getElderlyData(idUser)
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        binding.ivLansiaDetOrgToolbarBack.setOnClickListener {
            back()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { back() }
        })

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getElderlyData(idUser)
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun dropDownSpinner(data: ElderlyPub) {
        val spinnerRiwayat: Spinner = binding.lansiaDetOrgDropTindakan
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, daftarTindakan)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerRiwayat.adapter = adapter
        spinnerRiwayat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val select = daftarTindakan[position]
                if (select == "Periksa") {
                    val intent = Intent(this@ElderlyOrgDetailActivity, ActionCheckActivity::class.java).apply {
                        putExtra("ID", data.id)
                        putExtra("ID_USER", idUser)
                        putExtra("ID_MIN", idMin)
                        putExtra("NAMA", data.nama)
                        putExtra("NAMA_SUAMI", "")
                        putExtra("TGL_HAMIL", "")
                        putExtra("TAHUN_LAHIR", data.tahunLahir)
                        putExtra("JK", data.jk)
                        putExtra("NIK", data.nik)
                        putExtra("KK", data.kk)
                        putExtra("BPJS", data.bpjs)
                        putExtra("TITLE", "Data Lansia")
                        putExtra("KODE", "3")
                    }
                    activityLauncher.launch(intent)
                } else if (select == "Obat") {
                    val intent = Intent(this@ElderlyOrgDetailActivity, ActionMedicationActivity::class.java).apply {
                        putExtra("ID", data.id)
                        putExtra("ID_USER", idUser)
                        putExtra("ID_MIN", idMin)
                        putExtra("NAMA", data.nama)
                        putExtra("NAMA_SUAMI", "")
                        putExtra("TGL_HAMIL", "")
                        putExtra("TAHUN_LAHIR", data.tahunLahir)
                        putExtra("JK", data.jk)
                        putExtra("NIK", data.nik)
                        putExtra("KK", data.kk)
                        putExtra("BPJS", data.bpjs)
                        putExtra("TITLE", "Data Lansia")
                        putExtra("KODE", "3")
                    }
                    activityLauncher.launch(intent)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setuprecyclerview(data: ArrayList<ElderlyPub>) {
        val rvElderlySelect = binding.rvLansiaDetOrg
        rvElderlySelect.layoutManager = LinearLayoutManager(this)
        val adapter = ElderlyPubSelectAdapter(data)
        adapter.setOnItemClickCallback(object : ElderlyPubSelectAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                viewModel.getElderlySelect(id)
            }
        })
        rvElderlySelect.adapter = adapter
    }

    private fun back() {
        val intentBack = Intent()
        setResult(RESULT_OK, intentBack)
        finish()
    }
}