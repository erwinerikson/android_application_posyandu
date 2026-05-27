package id.my.apm.posyandu.ui.mother

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.databinding.ActivityMotherOrgDetailBinding
import id.my.apm.posyandu.model.MotherPub
import id.my.apm.posyandu.repository.MotherRepository
import id.my.apm.posyandu.repository.RegistrationRepository
import id.my.apm.posyandu.ui.data.ActionCheckActivity
import id.my.apm.posyandu.ui.data.ActionMedicationActivity
import id.my.apm.posyandu.use_case.mother.GetMotherPubUseCase
import id.my.apm.posyandu.use_case.registration.ChangeStatusRegistrationUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import java.time.Year
import javax.inject.Inject

@AndroidEntryPoint
class MotherOrgDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: MotherOrgDetailViewModel
    private lateinit var binding: ActivityMotherOrgDetailBinding
    @Inject
    lateinit var repository: MotherRepository
    @Inject
    lateinit var regRepository: RegistrationRepository
    private lateinit var daftarTindakan: List<String>
    private var idReg = "0"
    private var idUser = "0"
    private var idMin = "0"
    private var status = "0"

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            binding.rvIbuDetOrg.visibility = View.GONE
            binding.llIbuDetOrgData.visibility = View.GONE
            binding.llIbuDetOrgStatus.visibility = View.GONE
            binding.rlIbuDetOrgDropTindakan.visibility = View.GONE
            viewModel.getMother(idUser)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMotherOrgDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val getMotherPubUseCase = GetMotherPubUseCase(repository)
        val changeStatusRegistrationUseCase = ChangeStatusRegistrationUseCase(regRepository)
        viewModel = MotherOrgDetailViewModel(getMotherPubUseCase, changeStatusRegistrationUseCase)

        idReg = intent.getStringExtra("ID").toString()
        idUser = intent.getStringExtra("ID_USER").toString()
        idMin = intent.getStringExtra("ID_MIN").toString()
        daftarTindakan = listOf("Silahkan pilih tindakan", "Periksa", "Obat")
        status = intent.getStringExtra("STATUS").toString()

        viewModel.responseGetMother.observe(this) { resp ->
            if (resp != null) {
                binding.tvIbuDetOrgNama.text = resp.nama
                binding.tvIbuDetOrgNamaSuami.text = resp.suami
                binding.tvIbuDetOrgUmur.text = (Year.now().value - Integer.parseInt(resp.tahunLahir)).toString()
                binding.tvIbuDetOrgNik.text = resp.nik.ifEmpty { "(tidak terdata)" }
                binding.tvIbuDetOrgKk.text = resp.kk.ifEmpty { "(tidak terdata)" }
                binding.tvIbuDetOrgBpjs.text = resp.bpjs.ifEmpty { "(tidak terdata)" }
                binding.llIbuDetOrgData.visibility = View.VISIBLE
                binding.llIbuDetOrg.visibility = View.VISIBLE

                when (status) {
                    "0" -> {
                        binding.llIbuDetOrgStatus.visibility = View.VISIBLE
                        binding.rlIbuDetOrgDropTindakan.visibility = View.GONE
                    }
                    "1" -> {
                        binding.tvIbuDetOrgStatus.text = getText(R.string.ubah_selesai)
                        dropDownSpinner(resp)
                        binding.llIbuDetOrgStatus.visibility = View.GONE
                        binding.rlIbuDetOrgDropTindakan.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.llIbuDetOrgStatus.visibility = View.GONE
                        binding.rlIbuDetOrgDropTindakan.visibility = View.GONE
                    }
                }
            } else {
                this.showToast("Tidak ada data")
            }
        }

        viewModel.responseChangeStatus.observe(this) { resp ->
            if (!resp.error) {
                this.showToast(resp.message)
                if (status == "0") {
                    status = "1"
                }
                binding.rvIbuDetOrg.visibility = View.GONE
                binding.llIbuDetOrgData.visibility = View.GONE
                binding.llIbuDetOrgStatus.visibility = View.GONE
                binding.rlIbuDetOrgDropTindakan.visibility = View.GONE
                viewModel.getMother(idUser)
            } else {
                this.showToast(resp.message)
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        val swipeRefresh = binding.ibuDetOrgSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llIbuDetOrg.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llIbuDetOrg.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.getMother(idUser)
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        binding.ivIbuDetOrgToolbarBack.setOnClickListener {
            back()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { back() }
        })

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getMother(idUser)
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun dropDownSpinner(data: MotherPub) {
        val spinnerRiwayat: Spinner = binding.ibuDetOrgDropTindakan
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, daftarTindakan)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerRiwayat.adapter = adapter
        spinnerRiwayat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val select = daftarTindakan[position]
                if (select == "Periksa") {
                    val intent = Intent(this@MotherOrgDetailActivity, ActionCheckActivity::class.java).apply {
                        putExtra("ID", data.id)
                        putExtra("ID_USER", idUser)
                        putExtra("ID_MIN", idMin)
                        putExtra("NAMA", data.nama)
                        putExtra("NAMA_SUAMI", data.suami)
                        putExtra("TGL_HAMIL", "")
                        putExtra("TAHUN_LAHIR", data.tahunLahir)
                        putExtra("JK", "")
                        putExtra("NIK", data.nik)
                        putExtra("KK", data.kk)
                        putExtra("BPJS", data.bpjs)
                        putExtra("TITLE", "Data Ibu")
                        putExtra("KODE", "2")
                    }
                    activityLauncher.launch(intent)
                } else if (select == "Obat") {
                    val intent = Intent(this@MotherOrgDetailActivity, ActionMedicationActivity::class.java).apply {
                        putExtra("ID", data.id)
                        putExtra("ID_USER", idUser)
                        putExtra("ID_MIN", idMin)
                        putExtra("NAMA", data.nama)
                        putExtra("NAMA_SUAMI", data.suami)
                        putExtra("TGL_HAMIL", "")
                        putExtra("TAHUN_LAHIR", data.tahunLahir)
                        putExtra("JK", "")
                        putExtra("NIK", data.nik)
                        putExtra("KK", data.kk)
                        putExtra("BPJS", data.bpjs)
                        putExtra("TITLE", "Data Ibu")
                        putExtra("KODE", "2")
                    }
                    activityLauncher.launch(intent)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun back() {
        val intentBack = Intent()
        setResult(RESULT_OK, intentBack)
        finish()
    }
}