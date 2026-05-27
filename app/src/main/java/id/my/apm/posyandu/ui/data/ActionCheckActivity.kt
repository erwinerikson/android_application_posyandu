package id.my.apm.posyandu.ui.data

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.adapter.HistoryCheckAdapter
import id.my.apm.posyandu.databinding.ActivityActionCheckBinding
import id.my.apm.posyandu.model.CheckHistory
import id.my.apm.posyandu.model.DataCheck
import id.my.apm.posyandu.model.DataMidwife
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.use_case.GetAllCheckHistoryUseCase
import id.my.apm.posyandu.use_case.GetDataCheckUseCase
import id.my.apm.posyandu.use_case.GetDataMidwifeUseCase
import id.my.apm.posyandu.use_case.SaveCheckUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import java.time.Year
import javax.inject.Inject

@AndroidEntryPoint
class ActionCheckActivity : AppCompatActivity() {

    private lateinit var viewModel: ActionCheckViewModel
    private lateinit var binding: ActivityActionCheckBinding
    @Inject
    lateinit var allRepository: AllRepository
    private lateinit var spinnerCheck: Spinner
    private lateinit var spinnerMidwife: Spinner
    private var listCheck = ArrayList<CheckHistory>()
    private var listDataCheck = ArrayList<DataCheck>()
    private var listNameCheck = ArrayList<String>()
    private var listDataMidwife = ArrayList<DataMidwife>()
    private var listNameMidwife = ArrayList<String>()
    private var id = "0"
    private var idUser = "0"
    private var idMin = "0"
    private var idPeriksa = ""
    private var harga = "0"
    private var idBidan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityActionCheckBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val getAllCheckHistoryUseCase = GetAllCheckHistoryUseCase(allRepository)
        val getDataCheckUseCase = GetDataCheckUseCase(allRepository)
        val getDataMidwifeUseCase = GetDataMidwifeUseCase(allRepository)
        val saveCheckUseCase = SaveCheckUseCase(allRepository)
        viewModel = ActionCheckViewModel(getAllCheckHistoryUseCase, getDataCheckUseCase, getDataMidwifeUseCase, saveCheckUseCase)

        spinnerCheck = binding.spinnerActPeriksa
        spinnerMidwife = binding.spinnerActPeriksaBidan

        id = intent.getStringExtra("ID").toString()
        idUser = intent.getStringExtra("ID_USER").toString()
        idMin = intent.getStringExtra("ID_MIN").toString()
        val nama = intent.getStringExtra("NAMA").toString()
        val namaSuami = intent.getStringExtra("NAMA_SUAMI").toString()
        val tglHamil = intent.getStringExtra("TGL_HAMIL").toString()
        val tahunLahir = intent.getStringExtra("TAHUN_LAHIR").toString()
        val jk = intent.getStringExtra("JK").toString()
        val nik = intent.getStringExtra("NIK").toString()
        val kk = intent.getStringExtra("KK").toString()
        val bpjs = intent.getStringExtra("BPJS").toString()
        val title = intent.getStringExtra("TITLE").toString()
        val kode = intent.getStringExtra("KODE").toString()

        binding.tvActPeriksaTindakanTitle.text = title

        if (namaSuami == "") {
            binding.llActPeriksaNamaSuami.visibility = View.GONE
        } else {
            binding.tvActPeriksaNamaSuami.text = namaSuami
        }
        if (tglHamil == "") {
            binding.llActPeriksaTglHamil.visibility = View.GONE
            binding.llActPeriksaUsiaHamil.visibility = View.GONE
        } else {
            binding.tvActPeriksaTglHamil.text = AppUtils.formatterDateDay(tglHamil)
            binding.tvActPeriksaUsiaHamil.text = AppUtils.calculateAge(tglHamil)
        }
        if (jk == "") {
            binding.llActPeriksaJk.visibility = View.GONE
        } else {
            binding.tvActPeriksaJk.text = if (jk === "L") "Laki-laki" else "Perempuan"
        }

        binding.tvActPeriksaNama.text = nama
        binding.tvActPeriksaUmur.text = (Year.now().value - Integer.parseInt(tahunLahir)).toString()
        binding.tvActPeriksaNik.text = nik.ifEmpty { "(tidak terdata)" }
        binding.tvActPeriksaKk.text = kk.ifEmpty { "(tidak terdata)" }
        binding.tvActPeriksaBpjs.text = bpjs.ifEmpty { "(tidak terdata)" }

        viewModel.responseGetCheck.observe(this) { resp ->
            listCheck.clear()
            if (resp.isNotEmpty()) {
                listCheck = resp
                setuprecyclerviewcheck(listCheck)
                binding.llActPeriksaRiwayatPeriksa.visibility = View.VISIBLE
                binding.llActPeriksaNoRiwayat.visibility = View.GONE
            } else {
                binding.llActPeriksaRiwayatPeriksa.visibility = View.GONE
                binding.llActPeriksaNoRiwayat.visibility = View.VISIBLE
            }
        }

        viewModel.responseGetDataCheck.observe(this) { resp ->
            listDataCheck.clear()
            listNameCheck.clear()
            if (resp.isNotEmpty()) {
                if (resp[0].error) {
                    this.showToast(resp[0].message)
                } else {
                    listNameCheck.add("Silahkan pilih Periksa")
                    listDataCheck = resp
                    for (item in listDataCheck) {
                        listNameCheck.add(item.nama)
                    }
                    setSpinnerCheck()
                }
            } else {
                this.showToast("Tidak ada data Periksa!")
            }
        }

        viewModel.responseGetDataMidwife.observe(this) { resp ->
            listDataMidwife.clear()
            listNameMidwife.clear()
            if (resp.isNotEmpty()) {
                if (resp[0].error) {
                    this.showToast(resp[0].message)
                } else {
                    listNameMidwife.add("Silahkan pilih Bidan")
                    listDataMidwife = resp
                    for (item in listDataMidwife) {
                        listNameMidwife.add(item.nama)
                    }
                    setSpinnerMidwife()
                }
            } else {
                this.showToast("Tidak ada data Bidan!")
            }
        }

        viewModel.responseProcess.observe(this) { resp ->
            binding.btnActPeriksaProses.isClickable = true
            if (!resp.error) {
                this.showToast(resp.message)
                back()
            } else {
                this.showToast(resp.message)
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            binding.btnActPeriksaProses.isClickable = true
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        spinnerCheck.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                if (selectedItem == "Silahkan pilih Periksa") {
                    idPeriksa = ""
                    harga = "0"
                } else {
                    idPeriksa = listDataCheck[position - 1].id
                    harga = listDataCheck[position - 1].harga
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerMidwife.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                idBidan = if (selectedItem == "Silahkan pilih Bidan") {
                    ""
                } else {
                    listDataMidwife[position - 1].id
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.btnActPeriksaTindakan.setOnClickListener {
            idPeriksa = ""
            idBidan = ""
            binding.llActPeriksa.visibility = View.GONE
            binding.llActPeriksaTindakan.visibility = View.VISIBLE
            if (listDataCheck.isEmpty()) {
                viewModel.getDataCheck()
            } else {
                setSpinnerCheck()
            }

            if (listDataMidwife.isEmpty()) {
                viewModel.getDataMidwife()
            } else {
                setSpinnerMidwife()
            }
        }

        binding.btnActPeriksaBatal.setOnClickListener {
            idPeriksa = ""
            idBidan = ""
            binding.llActPeriksa.visibility = View.VISIBLE
            binding.llActPeriksaTindakan.visibility = View.GONE
        }

        binding.btnActPeriksaProses.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(this)) {
                val tinggi = binding.eTActPeriksaTinggi
                val berat = binding.eTActPeriksaBerat
                val tensi = binding.eTActPeriksaTensi
                val hasil = binding.eTActPeriksaHasil
                if (idPeriksa.isEmpty()) {
                    this.showToast("Pemeriksaan belum dipilih!")
                    it.isClickable = true
                } else if (tinggi.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(tinggi, it, "Field ini tidak boleh kosong")
                } else if (berat.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(berat, it, "Field ini tidak boleh kosong")
                } else if (tensi.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(tensi, it, "Field ini tidak boleh kosong")
                } else if (idBidan.isEmpty()) {
                    this.showToast("Bidan belum dipilih!")
                    it.isClickable = true
                } else {
                    viewModel.process(idUser, id, idPeriksa, berat.text.toString(), tinggi.text.toString(), tensi.text.toString(), hasil.text.toString(), idBidan, harga, kode)
                }
            } else {
                it.isClickable = true
                this.showToast("Tidak ada koneksi internet")
            }
        }

        binding.ivActPeriksaToolbarBack.setOnClickListener {
            back()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { back() }
        })

        val swipeRefresh = binding.actPeriksaSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llActPeriksa.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llActPeriksa.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.getCheck(kode, id)
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getCheck(kode, id)
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun setSpinnerCheck() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listNameCheck)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCheck.adapter = adapter
    }

    private fun setSpinnerMidwife() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listNameMidwife)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMidwife.adapter = adapter
    }

    private fun setuprecyclerviewcheck(data: ArrayList<CheckHistory>) {
        val rvTableImun = binding.rvActPeriksaRiwayatPeriksa
        rvTableImun.layoutManager = LinearLayoutManager(this)
        val adapter = HistoryCheckAdapter(data)
        adapter.setOnItemClickCallback(object : HistoryCheckAdapter.OnItemClickCallback {
            override fun onItemClicked(data: CheckHistory) {
                binding.tvActPeriksaHistoryTgl.text = AppUtils.formatterDateLetter(data.tgl)
                binding.tvActPeriksaHistoryNama.text = data.nama
                binding.tvActPeriksaHistoryTinggi.text = data.tinggi
                binding.tvActPeriksaHistoryBerat.text = data.berat
                binding.tvActPeriksaHistoryTensi.text = data.tensi
                binding.tvActPeriksaHistoryHasil.text = data.hasil
                binding.tvActPeriksaHistoryNamaBidan.text = data.namaBidan
                binding.llActPeriksaHistory.visibility = View.VISIBLE
            }
        })
        rvTableImun.adapter = adapter
    }

    private fun back() {
        val intentBack = Intent()
        setResult(RESULT_OK, intentBack)
        finish()
    }
}