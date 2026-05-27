package id.my.apm.posyandu.ui.pregnant

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.adapter.HistoryCheckAdapter
import id.my.apm.posyandu.adapter.HistoryMedicationAdapter
import id.my.apm.posyandu.adapter.pregnant.PregnantImmunizationAdapter
import id.my.apm.posyandu.databinding.ActivityMotherPubBinding
import id.my.apm.posyandu.databinding.ActivityPregnantPubBinding
import id.my.apm.posyandu.model.CheckHistory
import id.my.apm.posyandu.model.MedicationHistory
import id.my.apm.posyandu.model.PregnantImmunization
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.repository.MotherRepository
import id.my.apm.posyandu.ui.mother.MotherPubViewModel
import id.my.apm.posyandu.use_case.GetAllCheckHistoryUseCase
import id.my.apm.posyandu.use_case.GetAllMedicationHistoryUseCase
import id.my.apm.posyandu.use_case.pregnant.GetPregnantImmunizationUseCase
import id.my.apm.posyandu.use_case.pregnant.GetPregnantPubUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import id.my.apm.posyandu.utils.SessionManager
import java.time.Year
import javax.inject.Inject
import kotlin.text.ifEmpty

@AndroidEntryPoint
class PregnantPubActivity : AppCompatActivity() {

    private lateinit var viewModel: PregnantPubViewModel
    private lateinit var binding: ActivityPregnantPubBinding
    @Inject
    lateinit var repository: MotherRepository
    @Inject
    lateinit var allRepository: AllRepository
    private var checkHistory = 0
    private var immunizationHistory = 0
    private var listCheckHistory = ArrayList<CheckHistory>()
    private var listImmunizationHistory = ArrayList<PregnantImmunization>()
    private var listMedicationHistory = ArrayList<MedicationHistory>()
    private lateinit var daftarHistory: List<String>
    private lateinit var idPregnant: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPregnantPubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val getPregnantPubUseCase = GetPregnantPubUseCase(repository)
        val getAllCheckHistoryUseCase = GetAllCheckHistoryUseCase(allRepository)
        val getAllMedicationHistoryUseCase = GetAllMedicationHistoryUseCase(allRepository)
        val getPregnantImmunizationUseCase = GetPregnantImmunizationUseCase(repository)
        val sessionManager = SessionManager(this)
        viewModel = PregnantPubViewModel(getPregnantPubUseCase, getAllCheckHistoryUseCase, getAllMedicationHistoryUseCase, getPregnantImmunizationUseCase, sessionManager)

        viewModel.responseGetPregnant.observe(this) { resp ->
            if (!resp.error) {
                idPregnant = resp.id
                binding.tvPubIbuHamilNama.text = resp.nama
                binding.tvPubIbuHamilUmur.text = (Year.now().value - Integer.parseInt(resp.tahunLahir)).toString()
                binding.tvPubIbuHamilSuami.text = resp.suami
                binding.tvPubIbuHamilUsiaHamil.text = if (resp.tglHamil == null) "---" else AppUtils.calculateAge(resp.tglHamil)
                binding.tvPubIbuHamilNik.text = resp.nik.ifEmpty { "(tidak terdata)" }
                binding.tvPubIbuHamilKk.text = resp.kk.ifEmpty { "(tidak terdata)" }
                binding.tvPubIbuHamilBpjs.text = resp.bpjs.ifEmpty { "(tidak terdata)" }
                binding.rvIbuHamilPub.visibility = View.GONE
                binding.llPubIbuHamilData.visibility = View.VISIBLE
                binding.llPubIbuHamilNoData.visibility = View.GONE
                viewModel.getPregnantCheck(idPregnant)
            } else {
                this.showToast(resp.message)
            }
        }

        viewModel.responseGetPregnantCheck.observe(this) { resp ->
            checkHistory = 0
            listCheckHistory.clear()
            if (resp.isNotEmpty()) {
                checkHistory = 1
                listCheckHistory = resp
                setuprecyclerviewcheck(listCheckHistory)
            }
            viewModel.getPregnantImmunization(idPregnant)
        }

        viewModel.responseGetPregnantImmunization.observe(this) { resp ->
            immunizationHistory = 0
            listImmunizationHistory.clear()
            if (resp.isNotEmpty()) {
                immunizationHistory = 1
                listImmunizationHistory = resp
                setuprecyclerviewimun(listImmunizationHistory)
            }
            viewModel.getPregnantMedication(idPregnant)
        }

        viewModel.responseGetPregnantMedication.observe(this) { resp ->
            daftarHistory = emptyList()
            listMedicationHistory.clear()
            if (resp.isNotEmpty()) {
                listMedicationHistory = resp
                setuprecyclerviewobat(listMedicationHistory)
                daftarHistory = if (checkHistory == 1 && immunizationHistory == 1) {
                    listOf("Silahkan pilih riwayat", "Periksa", "Imunisasi", "Obat")
                } else if (checkHistory == 1) {
                    listOf("Silahkan pilih riwayat", "Periksa", "Obat")
                } else if (immunizationHistory == 1) {
                    listOf("Silahkan pilih riwayat", "Imunisasi", "Obat")
                } else {
                    listOf("Silahkan pilih riwayat", "Obat")
                }
                /*daftarHistory = if (checkHistory == 1) {
                    listOf("Silahkan pilih riwayat", "Periksa", "Obat")
                } else {
                    listOf("Silahkan pilih riwayat", "Obat")
                }*/
                dropDownSpinner()
                binding.rlPubIbuHamilDropRiwayat.visibility = View.VISIBLE
                binding.llPubIbuHamilNoRiwayat.visibility = View.GONE
            } else {
                if (checkHistory == 1 && immunizationHistory == 1) {
                    daftarHistory = listOf("Silahkan pilih riwayat", "Periksa", "Imunisasi")
                    dropDownSpinner()
                    binding.rlPubIbuHamilDropRiwayat.visibility = View.VISIBLE
                    binding.llPubIbuHamilNoRiwayat.visibility = View.GONE
                } else if (checkHistory == 1) {
                    daftarHistory = listOf("Silahkan pilih riwayat", "Periksa")
                    dropDownSpinner()
                    binding.rlPubIbuHamilDropRiwayat.visibility = View.VISIBLE
                    binding.llPubIbuHamilNoRiwayat.visibility = View.GONE
                } else if (immunizationHistory == 1) {
                    daftarHistory = listOf("Silahkan pilih riwayat", "Imunisasi")
                    dropDownSpinner()
                    binding.rlPubIbuHamilDropRiwayat.visibility = View.VISIBLE
                    binding.llPubIbuHamilNoRiwayat.visibility = View.GONE
                } else {
                    binding.rlPubIbuHamilDropRiwayat.visibility = View.GONE
                    binding.llPubIbuHamilNoRiwayat.visibility = View.VISIBLE
                }
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        val swipeRefresh = binding.ibuHamilPubSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llPubIbuHamil.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llPubIbuHamil.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.getPregnant()
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        binding.ivIbuHamilPubToolbarBack.setOnClickListener {
            finish()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getPregnant()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun dropDownSpinner() {
        val spinnerRiwayat: Spinner = binding.ibuHamilPubDropRiwayat
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, daftarHistory)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerRiwayat.adapter = adapter
        spinnerRiwayat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val select = daftarHistory[position]
                when (select) {
                    "Periksa" -> {
                        binding.llPubIbuHamilRiwayatCheck.visibility = View.VISIBLE
                        binding.llPubIbuHamilRiwayatImun.visibility = View.GONE
                        binding.llPubIbuHamilRiwayatObat.visibility = View.GONE
                        binding.llPubIbuHamilCheckHistory.visibility = View.GONE
                    }
                    "Imunisasi" -> {
                        binding.llPubIbuHamilRiwayatCheck.visibility = View.GONE
                        binding.llPubIbuHamilRiwayatImun.visibility = View.VISIBLE
                        binding.llPubIbuHamilRiwayatObat.visibility = View.GONE
                        binding.llPubIbuHamilCheckHistory.visibility = View.GONE
                    }
                    "Obat" -> {
                        binding.llPubIbuHamilRiwayatCheck.visibility = View.GONE
                        binding.llPubIbuHamilRiwayatImun.visibility = View.GONE
                        binding.llPubIbuHamilRiwayatObat.visibility = View.VISIBLE
                        binding.llPubIbuHamilCheckHistory.visibility = View.GONE
                    }
                    else -> {
                        binding.llPubIbuHamilRiwayatCheck.visibility = View.GONE
                        binding.llPubIbuHamilRiwayatImun.visibility = View.GONE
                        binding.llPubIbuHamilRiwayatObat.visibility = View.GONE
                        binding.llPubIbuHamilCheckHistory.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setuprecyclerviewcheck(data: ArrayList<CheckHistory>) {
        val rvTableCheck = binding.rvPubIbuHamilRiwayatCheck
        rvTableCheck.layoutManager = LinearLayoutManager(this)
        val adapter = HistoryCheckAdapter(data)
        adapter.setOnItemClickCallback(object : HistoryCheckAdapter.OnItemClickCallback {
            override fun onItemClicked(data: CheckHistory) {
                binding.tvPubIbuHamilCheckHistoryTgl.text = AppUtils.formatterDateLetter(data.tgl)
                binding.tvPubIbuHamilCheckHistoryNama.text = data.nama
                binding.tvPubIbuHamilCheckHistoryTinggi.text = data.tinggi
                binding.tvPubIbuHamilCheckHistoryBerat.text = data.berat
                binding.tvPubIbuHamilCheckHistoryTensi.text = data.tensi
                binding.tvPubIbuHamilCheckHistoryHasil.text = data.hasil
                binding.tvPubIbuHamilCheckHistoryNamaBidan.text = data.namaBidan
                binding.llPubIbuHamilCheckHistory.visibility = View.VISIBLE
            }
        })
        rvTableCheck.adapter = adapter
    }

    private fun setuprecyclerviewimun(data: ArrayList<PregnantImmunization>) {
        val rvTableImmunization = binding.rvPubIbuHamilRiwayatImun
        rvTableImmunization.layoutManager = LinearLayoutManager(this)
        rvTableImmunization.adapter = PregnantImmunizationAdapter(data)
    }

    private fun setuprecyclerviewobat(data: ArrayList<MedicationHistory>) {
        val rvTableMedication = binding.rvPubIbuHamilRiwayatObat
        rvTableMedication.layoutManager = LinearLayoutManager(this)
        rvTableMedication.adapter = HistoryMedicationAdapter(data)
    }
}