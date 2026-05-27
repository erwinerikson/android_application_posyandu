package id.my.apm.posyandu.ui.pregnant

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
import id.my.apm.posyandu.adapter.pregnant.PregnantImmunizationAdapter
import id.my.apm.posyandu.databinding.ActivityPregnantActionImmunizationBinding
import id.my.apm.posyandu.model.DataImmunization
import id.my.apm.posyandu.model.DataMidwife
import id.my.apm.posyandu.model.PregnantImmunization
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.repository.MotherRepository
import id.my.apm.posyandu.use_case.GetDataImmunizationUseCase
import id.my.apm.posyandu.use_case.GetDataMidwifeUseCase
import id.my.apm.posyandu.use_case.SaveImmunPregnantUseCase
import id.my.apm.posyandu.use_case.pregnant.GetPregnantImmunizationUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import java.time.Year
import javax.inject.Inject

@AndroidEntryPoint
class PregnantActionImmunizationActivity : AppCompatActivity() {

    private lateinit var viewModel: PregnantActionImmunizationViewModel
    private lateinit var binding: ActivityPregnantActionImmunizationBinding
    @Inject
    lateinit var repository: MotherRepository
    @Inject
    lateinit var allRepository: AllRepository
    private lateinit var spinnerImmunization: Spinner
    private lateinit var spinnerMidwife: Spinner
    private var listImmunizationHistory = ArrayList<PregnantImmunization>()
    private var listDataImmunization = ArrayList<DataImmunization>()
    private var listNameImmunization = ArrayList<String>()
    private var listDataMidwife = ArrayList<DataMidwife>()
    private var listNameMidwife = ArrayList<String>()
    private var id = "0"
    private var idUser = "0"
    private var idMin = "0"
    private var idImun = ""
    private var harga = "0"
    private var idBidan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPregnantActionImmunizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val getPregnantImmunizationUseCase = GetPregnantImmunizationUseCase(repository)
        val getDataImmunizationUseCase = GetDataImmunizationUseCase(allRepository)
        val getDataMidwifeUseCase = GetDataMidwifeUseCase(allRepository)
        val saveImmunPregnantUseCase = SaveImmunPregnantUseCase(allRepository)
        viewModel = PregnantActionImmunizationViewModel(getPregnantImmunizationUseCase, getDataImmunizationUseCase, getDataMidwifeUseCase, saveImmunPregnantUseCase)

        spinnerImmunization = binding.spinnerIbuHamilActImun
        spinnerMidwife = binding.spinnerIbuHamilActImunBidan

        id = intent.getStringExtra("ID").toString()
        idUser = intent.getStringExtra("ID_USER").toString()
        idMin = intent.getStringExtra("ID_MIN").toString()
        val nama = intent.getStringExtra("NAMA").toString()
        val namaSuami = intent.getStringExtra("NAMA_SUAMI").toString()
        val tglHamil = intent.getStringExtra("TGL_HAMIL").toString()
        val tahunLahir = intent.getStringExtra("TAHUN_LAHIR").toString()
        val nik = intent.getStringExtra("NIK").toString()
        val kk = intent.getStringExtra("KK").toString()
        val bpjs = intent.getStringExtra("BPJS").toString()

        binding.tvIbuHamilActNama.text = nama
        binding.tvIbuHamilActSuami.text = namaSuami
        binding.tvIbuHamilActUmur.text = (Year.now().value - Integer.parseInt(tahunLahir)).toString()
        binding.tvIbuHamilActUsiaHamil.text = AppUtils.calculateAge(tglHamil)
        binding.tvIbuHamilActNik.text = nik.ifEmpty { "(tidak terdata)" }
        binding.tvIbuHamilActKk.text = kk.ifEmpty { "(tidak terdata)" }
        binding.tvIbuHamilActBpjs.text = bpjs.ifEmpty { "(tidak terdata)" }

        viewModel.responseGetImmunization.observe(this) { resp ->
            listImmunizationHistory.clear()
            if (resp.isNotEmpty()) {
                listImmunizationHistory = resp
                setuprecyclerviewimun(listImmunizationHistory)
                binding.llIbuHamilActRiwayatImun.visibility = View.VISIBLE
                binding.llIbuHamilActNoRiwayat.visibility = View.GONE
            } else {
                binding.llIbuHamilActRiwayatImun.visibility = View.GONE
                binding.llIbuHamilActNoRiwayat.visibility = View.VISIBLE
            }
        }

        viewModel.responseGetDataImmunization.observe(this) { resp ->
            listDataImmunization.clear()
            listNameImmunization.clear()
            if (resp.isNotEmpty()) {
                if (resp[0].error) {
                    this.showToast(resp[0].message)
                } else {
                    listNameImmunization.add("Silahkan pilih Imunisasi")
                    listDataImmunization = resp
                    for (item in listDataImmunization) {
                        listNameImmunization.add(item.nama)
                    }
                    setSpinnerImmunization()
                }
            } else {
                this.showToast("Tidak ada data Imunisasi!")
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
            binding.btnIbuHamilActImunProses.isClickable = true
            if (!resp.error) {
                this.showToast(resp.message)
                back()
            } else {
                this.showToast(resp.message)
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            binding.btnIbuHamilActImunProses.isClickable = true
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        spinnerImmunization.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                if (selectedItem == "Silahkan pilih Imunisasi") {
                    idImun = ""
                    harga = "0"
                } else {
                    idImun = listDataImmunization[position - 1].id
                    harga = listDataImmunization[position - 1].harga
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

        binding.btnIbuHamilActImunTindakan.setOnClickListener {
            idImun = ""
            idBidan = ""
            binding.llIbuHamilActImun.visibility = View.GONE
            binding.llIbuHamilActImunTindakan.visibility = View.VISIBLE
            if (listDataImmunization.isEmpty()) {
                viewModel.getDataImmunization()
            } else {
                setSpinnerImmunization()
            }

            if (listDataMidwife.isEmpty()) {
                viewModel.getDataMidwife()
            } else {
                setSpinnerMidwife()
            }
        }

        binding.btnIbuHamilActImunBatal.setOnClickListener {
            idImun = ""
            idBidan = ""
            binding.llIbuHamilActImun.visibility = View.VISIBLE
            binding.llIbuHamilActImunTindakan.visibility = View.GONE
        }

        binding.btnIbuHamilActImunProses.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(this)) {
                if (idImun.isEmpty()) {
                    this.showToast("Imunisasi belum dipilih!")
                    it.isClickable = true
                } else if (idBidan.isEmpty()) {
                    this.showToast("Bidan belum dipilih!")
                    it.isClickable = true
                } else {
                    viewModel.process(idUser, id, idImun, idBidan, harga)
                }
            } else {
                it.isClickable = true
                this.showToast("Tidak ada koneksi internet")
            }
        }

        binding.ivIbuHamilActImunToolbarBack.setOnClickListener {
            back()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { back() }
        })

        val swipeRefresh = binding.ibuHamilActImunSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llIbuHamilActImun.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llIbuHamilActImun.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.getImmunization(id)
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getImmunization(id)
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun setSpinnerImmunization() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listNameImmunization)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerImmunization.adapter = adapter
    }

    private fun setSpinnerMidwife() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listNameMidwife)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMidwife.adapter = adapter
    }

    private fun setuprecyclerviewimun(data: ArrayList<PregnantImmunization>) {
        val rvTableImmunization = binding.rvIbuHamilActRiwayatImun
        rvTableImmunization.layoutManager = LinearLayoutManager(this)
        rvTableImmunization.adapter = PregnantImmunizationAdapter(data)
    }

    private fun back() {
        val intentBack = Intent()
        setResult(RESULT_OK, intentBack)
        finish()
    }
}