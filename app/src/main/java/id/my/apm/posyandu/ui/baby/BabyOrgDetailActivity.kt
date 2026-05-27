package id.my.apm.posyandu.ui.baby

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
import id.my.apm.posyandu.adapter.baby.BabyPubSelectAdapter
import id.my.apm.posyandu.databinding.ActivityBabyOrgDetailBinding
import id.my.apm.posyandu.model.BabyPub
import id.my.apm.posyandu.repository.BabyRepository
import id.my.apm.posyandu.repository.RegistrationRepository
import id.my.apm.posyandu.use_case.baby.GetBabiesPubUseCase
import id.my.apm.posyandu.use_case.baby.GetBabyPubUseCase
import id.my.apm.posyandu.use_case.registration.ChangeStatusRegistrationUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject
import kotlin.text.ifEmpty

@AndroidEntryPoint
class BabyOrgDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: BabyOrgDetailViewModel
    private lateinit var binding: ActivityBabyOrgDetailBinding
    @Inject
    lateinit var repository: BabyRepository
    @Inject
    lateinit var regRepository: RegistrationRepository
    private var listBabySelect = ArrayList<BabyPub>()
    private lateinit var daftarTindakan: List<String>
    private var idReg = "0"
    private var idUser = "0"
    private var idMin = "0"
    private var status = "0"

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.rvBayiDetOrg.visibility = View.GONE
            binding.llBayiDetOrgData.visibility = View.GONE
            binding.llBayiDetOrgStatus.visibility = View.GONE
            binding.rlBayiDetOrgDropTindakan.visibility = View.GONE
            viewModel.getBabies(idUser)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBabyOrgDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val getBabiesPubUseCase = GetBabiesPubUseCase(repository)
        val getBabyPubUseCase = GetBabyPubUseCase(repository)
        val changeStatusRegistrationUseCase = ChangeStatusRegistrationUseCase(regRepository)
        viewModel = BabyOrgDetailViewModel(getBabiesPubUseCase, getBabyPubUseCase, changeStatusRegistrationUseCase)

        idReg = intent.getStringExtra("ID").toString()
        idUser = intent.getStringExtra("ID_USER").toString()
        idMin = intent.getStringExtra("ID_MIN").toString()
        daftarTindakan = listOf("Silahkan pilih tindakan", "Imunisasi")
        status = intent.getStringExtra("STATUS").toString()

        viewModel.responseGetBabies.observe(this) { resp ->
            listBabySelect.clear()
            if (resp.size == 1) {
                viewModel.getBaby(resp[0].id)
            } else if (resp.size > 1) {
                listBabySelect = resp
                setuprecyclerview(listBabySelect)
                binding.rvBayiDetOrg.visibility = View.VISIBLE
                binding.llBayiDetOrgData.visibility = View.GONE
            } else {
                binding.rvBayiDetOrg.visibility = View.GONE
                binding.llBayiDetOrgData.visibility = View.GONE
            }
        }

        viewModel.responseGetBaby.observe(this) { resp ->
            if (resp != null) {
                setData(resp)
                binding.rvBayiDetOrg.visibility = View.GONE
                binding.llBayiDetOrgData.visibility = View.VISIBLE
                when (status) {
                    "0" -> {
                        binding.llBayiDetOrgStatus.visibility = View.VISIBLE
                        binding.rlBayiDetOrgDropTindakan.visibility = View.GONE
                    }
                    "1" -> {
                        binding.tvBayiDetOrgStatus.text = getText(R.string.ubah_selesai)
                        dropDownSpinner(resp)
                        binding.llBayiDetOrgStatus.visibility = View.GONE
                        binding.rlBayiDetOrgDropTindakan.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.llBayiDetOrgStatus.visibility = View.GONE
                        binding.rlBayiDetOrgDropTindakan.visibility = View.GONE
                    }
                }
            } else {
                this.showToast("Tidak ada data")
                binding.rvBayiDetOrg.visibility = View.GONE
                binding.llBayiDetOrgData.visibility = View.GONE
                binding.llBayiDetOrgStatus.visibility = View.GONE
                binding.rlBayiDetOrgDropTindakan.visibility = View.GONE
            }
        }

        viewModel.responseGetBabySelect.observe(this) { resp ->
            if (resp != null) {
                setData(resp)
                binding.rvBayiDetOrg.visibility = View.VISIBLE
                binding.llBayiDetOrgData.visibility = View.VISIBLE
                when (status) {
                    "0" -> {
                        binding.llBayiDetOrgStatus.visibility = View.VISIBLE
                        binding.rlBayiDetOrgDropTindakan.visibility = View.GONE
                    }
                    "1" -> {
                        binding.tvBayiDetOrgStatus.text = getText(R.string.ubah_selesai)
                        dropDownSpinner(resp)
                        binding.llBayiDetOrgStatus.visibility = View.GONE
                        binding.rlBayiDetOrgDropTindakan.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.llBayiDetOrgStatus.visibility = View.GONE
                        binding.rlBayiDetOrgDropTindakan.visibility = View.GONE
                    }
                }
            } else {
                this.showToast("Tidak ada data")
                binding.rvBayiDetOrg.visibility = View.GONE
                binding.llBayiDetOrgData.visibility = View.GONE
                binding.llBayiDetOrgStatus.visibility = View.GONE
                binding.rlBayiDetOrgDropTindakan.visibility = View.GONE
            }
        }

        viewModel.responseChangeStatus.observe(this) { resp ->
            if (!resp.error) {
                this.showToast(resp.message)
                if (status == "0") {
                    status = "1"
                    //viewModel.getBabies(idUser)
                    //binding.tvBayiDetOrgStatus.text = getText(R.string.ubah_selesai)
                }
                binding.rvBayiDetOrg.visibility = View.GONE
                binding.llBayiDetOrgData.visibility = View.GONE
                binding.llBayiDetOrgStatus.visibility = View.GONE
                binding.rlBayiDetOrgDropTindakan.visibility = View.GONE
                viewModel.getBabies(idUser)
            } else {
                this.showToast(resp.message)
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        val switch = binding.scBayiDetOrgStatus
        switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val customDialog = findViewById<ConstraintLayout>(R.id.customDialogCancel)
                val view = LayoutInflater.from(this@BabyOrgDetailActivity).inflate(R.layout.custom_dialog_cancel, customDialog)
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

                val builder = AlertDialog.Builder(this@BabyOrgDetailActivity)
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

        val swipeRefresh = binding.bayiDetOrgSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llBayiDetOrg.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llBayiDetOrg.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.getBabies(idUser)
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        binding.ivBayiDetOrgToolbarBack.setOnClickListener {
            back()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { back() }
        })

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getBabies(idUser)
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun dropDownSpinner(data: BabyPub) {
        val spinnerRiwayat: Spinner = binding.bayiDetOrgDropTindakan
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, daftarTindakan)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerRiwayat.adapter = adapter
        spinnerRiwayat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val select = daftarTindakan[position]
                if (select == "Imunisasi") {
                    val intent = Intent(this@BabyOrgDetailActivity, BabyActionImmunizationActivity::class.java).apply {
                        putExtra("ID", data.id)
                        putExtra("ID_USER", idUser)
                        putExtra("ID_MIN", idMin)
                        putExtra("NAMA", data.nama)
                        putExtra("NAMA_IBU", data.namaIbu)
                        putExtra("NAMA_AYAH", data.namaAyah)
                        putExtra("TGL_LAHIR", data.tglLahir)
                        putExtra("JK", data.jk)
                        putExtra("NIK", data.nik)
                        putExtra("KK", data.kk)
                    }
                    activityLauncher.launch(intent)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setuprecyclerview(data: ArrayList<BabyPub>) {
        val rvBabySelect = binding.rvBayiDetOrg
        rvBabySelect.layoutManager = LinearLayoutManager(this)
        val adapter = BabyPubSelectAdapter(data)
        adapter.setOnItemClickCallback(object : BabyPubSelectAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                viewModel.getBabySelect(data)
            }
        })
        rvBabySelect.adapter = adapter
    }

    private fun setData(data: BabyPub) {
        binding.tvBayiDetOrgNama.text = data.nama
        binding.tvBayiDetOrgNamaIbu.text = data.namaIbu
        binding.tvBayiDetOrgNamaAyah.text = data.namaAyah
        binding.tvBayiDetOrgTglLahir.text = AppUtils.formatterDateDay(data.tglLahir)
        binding.tvBayiDetOrgUmur.text = AppUtils.calculateAge(data.tglLahir)
        binding.tvBayiDetOrgJk.text = if (data.jk === "L") "Laki-laki" else "Perempuan"
        binding.tvBayiDetOrgNik.text = data.nik.ifEmpty { "(tidak terdata)" }
        binding.tvBayiDetOrgKk.text = data.kk.ifEmpty { "(tidak terdata)" }
    }

    private fun back() {
        val intentBack = Intent()
        setResult(RESULT_OK, intentBack)
        finish()
    }
}