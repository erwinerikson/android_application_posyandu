package id.my.apm.posyandu.ui.elderly

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
import id.my.apm.posyandu.adapter.elderly.ElderlyPubSelectAdapter
import id.my.apm.posyandu.databinding.ActivityElderlyPubBinding
import id.my.apm.posyandu.model.CheckHistory
import id.my.apm.posyandu.model.ElderlyPub
import id.my.apm.posyandu.model.MedicationHistory
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.repository.ElderlyRepository
import id.my.apm.posyandu.use_case.GetAllCheckHistoryUseCase
import id.my.apm.posyandu.use_case.GetAllMedicationHistoryUseCase
import id.my.apm.posyandu.use_case.elderly.GetElderlyDataUseCase
import id.my.apm.posyandu.use_case.elderly.GetElderlyPubUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import id.my.apm.posyandu.utils.SessionManager
import java.time.Year
import javax.inject.Inject

@AndroidEntryPoint
class ElderlyPubActivity : AppCompatActivity() {

    private lateinit var viewModel: ElderlyPubViewModel
    private lateinit var binding: ActivityElderlyPubBinding
    @Inject
    lateinit var repository: ElderlyRepository
    @Inject
    lateinit var allRepository: AllRepository
    private var checkHistory = 0
    private var listElderlyPub = ArrayList<ElderlyPub>()
    private var listCheckHistory = ArrayList<CheckHistory>()
    private var listMedicationHistory = ArrayList<MedicationHistory>()
    private lateinit var daftarHistory: List<String>
    private lateinit var idElderly: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityElderlyPubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val getElderlyPubUseCase = GetElderlyPubUseCase(repository)
        val getElderlyDataUseCase = GetElderlyDataUseCase(repository)
        val getAllCheckHistoryUseCase = GetAllCheckHistoryUseCase(allRepository)
        val getAllMedicationHistoryUseCase = GetAllMedicationHistoryUseCase(allRepository)
        val sessionManager = SessionManager(this)
        viewModel = ElderlyPubViewModel(getElderlyPubUseCase, getElderlyDataUseCase, getAllCheckHistoryUseCase, getAllMedicationHistoryUseCase, sessionManager)

        viewModel.responseGetElderly.observe(this) { resp ->
            if (!resp.error) {
                idElderly = resp.id
                binding.tvPubLansiaNama.text = resp.nama
                binding.tvPubLansiaUmur.text = (Year.now().value - Integer.parseInt(resp.tahunLahir)).toString()
                binding.tvPubLansiaJk.text = if (resp.jk === "L") "Laki-laki" else "Perempuan"
                binding.tvPubLansiaNik.text = resp.nik.ifEmpty { "(tidak terdata)" }
                binding.tvPubLansiaKk.text = resp.kk.ifEmpty { "(tidak terdata)" }
                binding.tvPubLansiaBpjs.text = resp.bpjs.ifEmpty { "(tidak terdata)" }
                binding.llPubLansiaData.visibility = View.VISIBLE
                binding.llPubLansiaNoData.visibility = View.GONE
                binding.rlPubLansiaDropRiwayat.visibility = View.GONE
                binding.llPubLansiaNoRiwayat.visibility = View.GONE
                binding.llPubLansiaRiwayatCheck.visibility = View.GONE
                binding.llPubLansiaRiwayatObat.visibility = View.GONE
                binding.llPubLansiaCheckHistory.visibility = View.GONE
                binding.rvLansiaPub.visibility = View.VISIBLE
                binding.llPubLansia.visibility = View.VISIBLE
                viewModel.getElderlyCheck(resp.id)
            } else {
                this.showToast("Tidak ada data")
            }
        }

        viewModel.responseGetElderlyData.observe(this) { resp ->
            if (resp.size > 1) {
                listElderlyPub.clear()
                listElderlyPub = resp
                setuprecyclerview(listElderlyPub)
                binding.llPubLansiaData.visibility = View.GONE
                binding.llPubLansiaNoData.visibility = View.GONE
                binding.rlPubLansiaDropRiwayat.visibility = View.GONE
                binding.llPubLansiaNoRiwayat.visibility = View.GONE
                binding.llPubLansiaRiwayatCheck.visibility = View.GONE
                binding.llPubLansiaRiwayatObat.visibility = View.GONE
                binding.llPubLansiaCheckHistory.visibility = View.GONE
                binding.rvLansiaPub.visibility = View.VISIBLE
                binding.llPubLansia.visibility = View.VISIBLE
            } else if (resp.size == 1) {
                idElderly = resp[0].id
                binding.tvPubLansiaNama.text = resp[0].nama
                binding.tvPubLansiaUmur.text = (Year.now().value - Integer.parseInt(resp[0].tahunLahir)).toString()
                binding.tvPubLansiaJk.text = if (resp[0].jk === "L") "Laki-laki" else "Perempuan"
                binding.tvPubLansiaNik.text = resp[0].nik.ifEmpty { "(tidak terdata)" }
                binding.tvPubLansiaKk.text = resp[0].kk.ifEmpty { "(tidak terdata)" }
                binding.tvPubLansiaBpjs.text = resp[0].bpjs.ifEmpty { "(tidak terdata)" }
                binding.llPubLansiaData.visibility = View.VISIBLE
                binding.llPubLansiaNoData.visibility = View.GONE
                binding.rlPubLansiaDropRiwayat.visibility = View.GONE
                binding.llPubLansiaNoRiwayat.visibility = View.GONE
                binding.llPubLansiaRiwayatCheck.visibility = View.GONE
                binding.llPubLansiaRiwayatObat.visibility = View.GONE
                binding.llPubLansiaCheckHistory.visibility = View.GONE
                binding.rvLansiaPub.visibility = View.GONE
                binding.llPubLansia.visibility = View.VISIBLE
                viewModel.getElderlyCheck(resp[0].id)
            } else {
                binding.rlPubLansiaDropRiwayat.visibility = View.GONE
                binding.llPubLansiaNoRiwayat.visibility = View.GONE
                binding.llPubLansiaRiwayatCheck.visibility = View.GONE
                binding.llPubLansiaRiwayatObat.visibility = View.GONE
                binding.llPubLansiaCheckHistory.visibility = View.GONE
                binding.llPubLansiaData.visibility = View.GONE
                binding.llPubLansiaNoData.visibility = View.VISIBLE
                binding.llPubLansia.visibility = View.VISIBLE
                this.showToast("Tidak ada data")
            }
        }

        viewModel.responseGetElderlyCheck.observe(this) { resp ->
            listCheckHistory.clear()
            if (resp.isNotEmpty()) {
                checkHistory = 1
                listCheckHistory = resp
                setuprecyclerviewcheck(listCheckHistory)
            }
            viewModel.getElderlyMedication(idElderly)
        }

        viewModel.responseGetElderlyMedication.observe(this) { resp ->
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
                binding.rlPubLansiaDropRiwayat.visibility = View.VISIBLE
                binding.llPubLansiaNoRiwayat.visibility = View.GONE
                dropDownSpinner()
            } else {
                if (checkHistory == 1) {
                    daftarHistory = listOf("Silahkan pilih riwayat", "Periksa")
                    dropDownSpinner()
                    binding.rlPubLansiaDropRiwayat.visibility = View.VISIBLE
                    binding.llPubLansiaNoRiwayat.visibility = View.GONE
                } else {
                    binding.rlPubLansiaDropRiwayat.visibility = View.GONE
                    binding.llPubLansiaNoRiwayat.visibility = View.VISIBLE
                }
            }
        }

        val swipeRefresh = binding.lansiaPubSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llPubLansia.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llPubLansia.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.getElderlyData()
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        binding.ivLansiaPubToolbarBack.setOnClickListener {
            finish()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getElderlyData()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun dropDownSpinner() {
        val spinnerRiwayat: Spinner = binding.lansiaPubDropRiwayat
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, daftarHistory)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerRiwayat.adapter = adapter
        spinnerRiwayat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val select = daftarHistory[position]
                when (select) {
                    "Periksa" -> {
                        binding.llPubLansiaRiwayatCheck.visibility = View.VISIBLE
                        binding.llPubLansiaRiwayatObat.visibility = View.GONE
                        binding.llPubLansiaCheckHistory.visibility = View.GONE
                    }
                    "Obat" -> {
                        binding.llPubLansiaRiwayatCheck.visibility = View.GONE
                        binding.llPubLansiaRiwayatObat.visibility = View.VISIBLE
                        binding.llPubLansiaCheckHistory.visibility = View.GONE
                    }
                    else -> {
                        binding.llPubLansiaRiwayatCheck.visibility = View.GONE
                        binding.llPubLansiaRiwayatObat.visibility = View.GONE
                        binding.llPubLansiaCheckHistory.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setuprecyclerview(data: ArrayList<ElderlyPub>) {
        val rvElderlySelect = binding.rvLansiaPub
        rvElderlySelect.layoutManager = LinearLayoutManager(this)
        val adapter = ElderlyPubSelectAdapter(data)
        adapter.setOnItemClickCallback(object : ElderlyPubSelectAdapter.OnItemClickCallback {
            override fun onItemClicked(id: String) {
                checkHistory = 0
                viewModel.getElderly(id)
            }
        })
        rvElderlySelect.adapter = adapter
    }

    private fun setuprecyclerviewcheck(data: ArrayList<CheckHistory>) {
        val rvTableImun = binding.rvPubLansiaRiwayatCheck
        rvTableImun.layoutManager = LinearLayoutManager(this)
        val adapter = HistoryCheckAdapter(data)
        adapter.setOnItemClickCallback(object : HistoryCheckAdapter.OnItemClickCallback {
            override fun onItemClicked(data: CheckHistory) {
                binding.tvPubLansiaCheckHistoryTgl.text = AppUtils.formatterDateLetter(data.tgl)
                binding.tvPubLansiaCheckHistoryNama.text = data.nama
                binding.tvPubLansiaCheckHistoryTinggi.text = data.tinggi
                binding.tvPubLansiaCheckHistoryBerat.text = data.berat
                binding.tvPubLansiaCheckHistoryTensi.text = data.tensi
                binding.tvPubLansiaCheckHistoryHasil.text = data.hasil
                binding.tvPubLansiaCheckHistoryNamaBidan.text = data.namaBidan
                binding.llPubLansiaCheckHistory.visibility = View.VISIBLE
            }
        })
        rvTableImun.adapter = adapter
    }

    private fun setuprecyclerviewobat(data: ArrayList<MedicationHistory>) {
        val rvTableImun = binding.rvPubLansiaRiwayatObat
        rvTableImun.layoutManager = LinearLayoutManager(this)
        rvTableImun.adapter = HistoryMedicationAdapter(data)
    }
}