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
import id.my.apm.posyandu.adapter.HistoryMedicationAdapter
import id.my.apm.posyandu.databinding.ActivityActionMedicationBinding
import id.my.apm.posyandu.model.DataMedication
import id.my.apm.posyandu.model.DataMidwife
import id.my.apm.posyandu.model.MedicationHistory
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.use_case.GetAllMedicationHistoryUseCase
import id.my.apm.posyandu.use_case.GetDataMedicationUseCase
import id.my.apm.posyandu.use_case.GetDataMidwifeUseCase
import id.my.apm.posyandu.use_case.SaveMedicationUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import java.time.Year
import javax.inject.Inject

@AndroidEntryPoint
class ActionMedicationActivity : AppCompatActivity() {

    private lateinit var viewModel: ActionMedicationViewModel
    private lateinit var binding: ActivityActionMedicationBinding
    @Inject
    lateinit var allRepository: AllRepository
    private lateinit var spinnerMedication: Spinner
    private lateinit var spinnerMidwife: Spinner
    private var listMedicationHistory = ArrayList<MedicationHistory>()
    private var listDataMedication = ArrayList<DataMedication>()
    private var listNameMedication = ArrayList<String>()
    private var listDataMidwife = ArrayList<DataMidwife>()
    private var listNameMidwife = ArrayList<String>()
    private var id = "0"
    private var idUser = "0"
    private var idMin = "0"
    private var idObat = ""
    private var harga = "0"
    private var idBidan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityActionMedicationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val getAllMedicationHistoryUseCase = GetAllMedicationHistoryUseCase(allRepository)
        val getDataMedicationUseCase = GetDataMedicationUseCase(allRepository)
        val getDataMidwifeUseCase = GetDataMidwifeUseCase(allRepository)
        val saveMedicationUseCase = SaveMedicationUseCase(allRepository)
        viewModel = ActionMedicationViewModel(getAllMedicationHistoryUseCase, getDataMedicationUseCase, getDataMidwifeUseCase, saveMedicationUseCase)

        spinnerMedication = binding.spinnerActObat
        spinnerMidwife = binding.spinnerActObatBidan

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

        binding.tvActObatTindakanTitle.text = title

        if (namaSuami == "") {
            binding.llActObatNamaSuami.visibility = View.GONE
        } else {
            binding.tvActObatNamaSuami.text = namaSuami
        }
        if (tglHamil == "") {
            binding.llActObatTglHamil.visibility = View.GONE
            binding.llActObatUsiaHamil.visibility = View.GONE
        } else {
            binding.tvActObatTglHamil.text = AppUtils.formatterDateDay(tglHamil)
            binding.tvActObatUsiaHamil.text = AppUtils.calculateAge(tglHamil)
        }
        if (jk == "") {
            binding.llActObatJk.visibility = View.GONE
        } else {
            binding.tvActObatJk.text = if (jk === "L") "Laki-laki" else "Perempuan"
        }

        binding.tvActObatNama.text = nama
        binding.tvActObatUmur.text = (Year.now().value - Integer.parseInt(tahunLahir)).toString()
        binding.tvActObatNik.text = nik.ifEmpty { "(tidak terdata)" }
        binding.tvActObatKk.text = kk.ifEmpty { "(tidak terdata)" }
        binding.tvActObatBpjs.text = bpjs.ifEmpty { "(tidak terdata)" }

        viewModel.responseGetMedication.observe(this) { resp ->
            listMedicationHistory.clear()
            if (resp.isNotEmpty()) {
                listMedicationHistory = resp
                setuprecyclerviewobat(listMedicationHistory)
                binding.llActObatRiwayatObat.visibility = View.VISIBLE
                binding.llActObatNoRiwayat.visibility = View.GONE
            } else {
                binding.llActObatRiwayatObat.visibility = View.GONE
                binding.llActObatNoRiwayat.visibility = View.VISIBLE
            }
        }

        viewModel.responseGetDataMedication.observe(this) { resp ->
            listDataMedication.clear()
            listNameMedication.clear()
            if (resp.isNotEmpty()) {
                if (resp[0].error) {
                    this.showToast(resp[0].message)
                } else {
                    listNameMedication.add("Silahkan pilih Obat")
                    listDataMedication = resp
                    for (item in listDataMedication) {
                        listNameMedication.add(item.nama)
                    }
                    setSpinnerMedication()
                }
            } else {
                this.showToast("Tidak ada data Obat!")
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
            binding.btnActObatProses.isClickable = true
            if (!resp.error) {
                this.showToast(resp.message)
                back()
            } else {
                this.showToast(resp.message)
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            binding.btnActObatProses.isClickable = true
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        spinnerMedication.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                if (selectedItem == "Silahkan pilih Obat") {
                    idObat = ""
                    harga = "0"
                } else {
                    idObat = listDataMedication[position - 1].id
                    harga = listDataMedication[position - 1].harga
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

        binding.btnActObatTindakan.setOnClickListener {
            idObat = ""
            idBidan = ""
            binding.llActObat.visibility = View.GONE
            binding.llActObatTindakan.visibility = View.VISIBLE
            if (listDataMedication.isEmpty()) {
                viewModel.getDataMedication()
            } else {
                setSpinnerMedication()
            }

            if (listDataMidwife.isEmpty()) {
                viewModel.getDataMidwife()
            } else {
                setSpinnerMidwife()
            }
        }

        binding.btnActObatBatal.setOnClickListener {
            idObat = ""
            idBidan = ""
            binding.llActObat.visibility = View.VISIBLE
            binding.llActObatTindakan.visibility = View.GONE
        }

        binding.btnActObatProses.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(this)) {
                val qty = binding.eTActObatQty
                if (idObat.isEmpty()) {
                    this.showToast("Obat belum dipilih!")
                    it.isClickable = true
                } else if (qty.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(qty, it, "Field ini tidak boleh kosong")
                } else if (idBidan.isEmpty()) {
                    this.showToast("Bidan belum dipilih!")
                    it.isClickable = true
                } else {
                    viewModel.process(idUser, id, idObat, qty.text.toString(), kode, idBidan, harga)
                }
            } else {
                it.isClickable = true
                this.showToast("Tidak ada koneksi internet")
            }
        }

        binding.ivActObatToolbarBack.setOnClickListener {
            back()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { back() }
        })

        val swipeRefresh = binding.actObatSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llActObat.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llActObat.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.getMedication(kode, id)
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getMedication(kode, id)
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun setSpinnerMedication() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listNameMedication)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMedication.adapter = adapter
    }

    private fun setSpinnerMidwife() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listNameMidwife)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMidwife.adapter = adapter
    }

    private fun setuprecyclerviewobat(data: ArrayList<MedicationHistory>) {
        val rvTableImun = binding.rvActObatRiwayatObat
        rvTableImun.layoutManager = LinearLayoutManager(this)
        rvTableImun.adapter = HistoryMedicationAdapter(data)
    }

    private fun back() {
        val intentBack = Intent()
        setResult(RESULT_OK, intentBack)
        finish()
    }
}