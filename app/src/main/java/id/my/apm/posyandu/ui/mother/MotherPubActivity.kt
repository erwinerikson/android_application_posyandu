package id.my.apm.posyandu.ui.mother

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
import id.my.apm.posyandu.databinding.ActivityMotherPubBinding
import id.my.apm.posyandu.model.CheckHistory
import id.my.apm.posyandu.model.MedicationHistory
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.repository.MotherRepository
import id.my.apm.posyandu.use_case.GetAllCheckHistoryUseCase
import id.my.apm.posyandu.use_case.GetAllMedicationHistoryUseCase
import id.my.apm.posyandu.use_case.mother.GetMotherPubUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import id.my.apm.posyandu.utils.SessionManager
import java.time.Year
import javax.inject.Inject

@AndroidEntryPoint
class MotherPubActivity : AppCompatActivity() {

    private lateinit var viewModel: MotherPubViewModel
    private lateinit var binding: ActivityMotherPubBinding
    @Inject
    lateinit var repository: MotherRepository
    @Inject
    lateinit var allRepository: AllRepository
    private var checkHistory = 0
    private var listCheckHistory = ArrayList<CheckHistory>()
    private var listMedicationHistory = ArrayList<MedicationHistory>()
    private lateinit var daftarHistory: List<String>
    private lateinit var idMother: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMotherPubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val getMotherPubUseCase = GetMotherPubUseCase(repository)
        val getAllCheckHistoryUseCase = GetAllCheckHistoryUseCase(allRepository)
        val getAllMedicationHistoryUseCase = GetAllMedicationHistoryUseCase(allRepository)
        val sessionManager = SessionManager(this)
        viewModel = MotherPubViewModel(getMotherPubUseCase, getAllCheckHistoryUseCase, getAllMedicationHistoryUseCase, sessionManager)

        viewModel.responseGetMother.observe(this) { resp ->
            if (!resp.error) {
                idMother = resp.id
                binding.tvPubIbuNama.text = resp.nama
                binding.tvPubIbuUmur.text = (Year.now().value - Integer.parseInt(resp.tahunLahir)).toString()
                binding.tvPubIbuSuami.text = resp.suami
                binding.tvPubIbuNik.text = resp.nik.ifEmpty { "(tidak terdata)" }
                binding.tvPubIbuKk.text = resp.kk.ifEmpty { "(tidak terdata)" }
                binding.tvPubIbuBpjs.text = resp.bpjs.ifEmpty { "(tidak terdata)" }
                binding.rvIbuPub.visibility = View.GONE
                binding.llPubIbuData.visibility = View.VISIBLE
                binding.llPubIbuNoData.visibility = View.GONE
                viewModel.getMotherCheck(idMother)
            } else {
                this.showToast("Tidak ada data")
            }
        }

        viewModel.responseGetMotherCheck.observe(this) { resp ->
            checkHistory = 0
            listCheckHistory.clear()
            if (resp.isNotEmpty()) {
                checkHistory = 1
                listCheckHistory = resp
                setuprecyclerviewcheck(listCheckHistory)
            }
            viewModel.getMotherMedication(idMother)
        }

        viewModel.responseGetMotherMedication.observe(this) { resp ->
            daftarHistory = emptyList()
            listMedicationHistory.clear()
            if (resp.isNotEmpty()) {
                listMedicationHistory = resp
                setuprecyclerviewobat(listMedicationHistory)
                daftarHistory = if (checkHistory == 1) {
                    listOf("Silahkan pilih riwayat", "Periksa", "Obat")
                } else {
                    listOf("Silahkan pilih riwayat", "Obat")
                }
                binding.rlPubIbuDropRiwayat.visibility = View.VISIBLE
                binding.llPubIbuNoRiwayat.visibility = View.GONE
                dropDownSpinner()
            } else {
                if (checkHistory == 1) {
                    daftarHistory = listOf("Silahkan pilih riwayat", "Periksa")
                    dropDownSpinner()
                    binding.rlPubIbuDropRiwayat.visibility = View.VISIBLE
                    binding.llPubIbuNoRiwayat.visibility = View.GONE
                } else {
                    binding.rlPubIbuDropRiwayat.visibility = View.GONE
                    binding.llPubIbuNoRiwayat.visibility = View.VISIBLE
                }
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp) {
                this.showToast("Some error occurred please try again")
            }
        }

        val swipeRefresh = binding.ibuPubSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llPubIbu.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llPubIbu.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.getMother()
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        binding.ivIbuPubToolbarBack.setOnClickListener {
            finish()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getMother()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun dropDownSpinner() {
        val spinnerRiwayat: Spinner = binding.ibuPubDropRiwayat
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, daftarHistory)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerRiwayat.adapter = adapter
        spinnerRiwayat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val select = daftarHistory[position]
                when (select) {
                    "Periksa" -> {
                        binding.llPubIbuRiwayatCheck.visibility = View.VISIBLE
                        binding.llPubIbuRiwayatObat.visibility = View.GONE
                        binding.llPubIbuCheckHistory.visibility = View.GONE
                    }
                    "Obat" -> {
                        binding.llPubIbuRiwayatCheck.visibility = View.GONE
                        binding.llPubIbuRiwayatObat.visibility = View.VISIBLE
                        binding.llPubIbuCheckHistory.visibility = View.GONE
                    }
                    else -> {
                        binding.llPubIbuRiwayatCheck.visibility = View.GONE
                        binding.llPubIbuRiwayatObat.visibility = View.GONE
                        binding.llPubIbuCheckHistory.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setuprecyclerviewcheck(data: ArrayList<CheckHistory>) {
        val rvTableCheck = binding.rvPubIbuRiwayatCheck
        rvTableCheck.layoutManager = LinearLayoutManager(this)
        val adapter = HistoryCheckAdapter(data)
        adapter.setOnItemClickCallback(object : HistoryCheckAdapter.OnItemClickCallback {
            override fun onItemClicked(data: CheckHistory) {
                binding.tvPubIbuCheckHistoryTgl.text = AppUtils.formatterDateLetter(data.tgl)
                binding.tvPubIbuCheckHistoryNama.text = data.nama
                binding.tvPubIbuCheckHistoryTinggi.text = data.tinggi
                binding.tvPubIbuCheckHistoryBerat.text = data.berat
                binding.tvPubIbuCheckHistoryTensi.text = data.tensi
                binding.tvPubIbuCheckHistoryHasil.text = data.hasil
                binding.tvPubIbuCheckHistoryNamaBidan.text = data.namaBidan
                binding.llPubIbuCheckHistory.visibility = View.VISIBLE
            }
        })
        rvTableCheck.adapter = adapter
    }

    private fun setuprecyclerviewobat(data: ArrayList<MedicationHistory>) {
        val rvTableMedication = binding.rvPubIbuRiwayatObat
        rvTableMedication.layoutManager = LinearLayoutManager(this)
        rvTableMedication.adapter = HistoryMedicationAdapter(data)
    }
}