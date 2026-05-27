package id.my.apm.posyandu.ui.baby

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
import id.my.apm.posyandu.adapter.baby.BabyImmunizationAdapter
import id.my.apm.posyandu.databinding.ActivityBabyActionImmunizationBinding
import id.my.apm.posyandu.model.ChildImmunization
import id.my.apm.posyandu.model.DataImmunization
import id.my.apm.posyandu.model.DataMidwife
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.repository.BabyRepository
import id.my.apm.posyandu.use_case.GetDataImmunizationUseCase
import id.my.apm.posyandu.use_case.GetDataMidwifeUseCase
import id.my.apm.posyandu.use_case.SaveImmunizationUseCase
import id.my.apm.posyandu.use_case.baby.GetBabyImmunizationUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject
import kotlin.text.isNotEmpty

@AndroidEntryPoint
class BabyActionImmunizationActivity : AppCompatActivity() {

    private lateinit var viewModel: BabyActionImmunizationViewModel
    private lateinit var binding: ActivityBabyActionImmunizationBinding
    @Inject
    lateinit var repository: BabyRepository
    @Inject
    lateinit var allRepository: AllRepository
    private lateinit var spinnerImmunization: Spinner
    private lateinit var spinnerMidwife: Spinner
    private var listImmunization = ArrayList<ChildImmunization>()
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
        binding = ActivityBabyActionImmunizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val getBabyImmunizationUseCase = GetBabyImmunizationUseCase(repository)
        val getDataImmunizationUseCase = GetDataImmunizationUseCase(allRepository)
        val getDataMidwifeUseCase = GetDataMidwifeUseCase(allRepository)
        val saveImmunizationUseCase = SaveImmunizationUseCase(allRepository)
        viewModel = BabyActionImmunizationViewModel(getBabyImmunizationUseCase, getDataImmunizationUseCase, getDataMidwifeUseCase, saveImmunizationUseCase)

        spinnerImmunization = binding.spinnerBayiActImun
        spinnerMidwife = binding.spinnerBayiActImunBidan

        id = intent.getStringExtra("ID").toString()
        idUser = intent.getStringExtra("ID_USER").toString()
        idMin = intent.getStringExtra("ID_MIN").toString()
        val nama = intent.getStringExtra("NAMA").toString()
        val namaIbu = intent.getStringExtra("NAMA_IBU").toString()
        val namaAyah = intent.getStringExtra("NAMA_AYAH").toString()
        val tglLahir = intent.getStringExtra("TGL_LAHIR").toString()
        val jk = intent.getStringExtra("JK").toString()
        val nik = intent.getStringExtra("NIK").toString()
        val kk = intent.getStringExtra("KK").toString()

        binding.tvBayiBayiActNama.text = nama
        binding.tvBayiBayiActNamaIbu.text = namaIbu
        binding.tvBayiBayiActNamaAyah.text = namaAyah
        binding.tvBayiBayiActTglLahir.text = AppUtils.formatterDateDay(tglLahir)
        binding.tvBayiBayiActUmur.text = AppUtils.calculateAge(tglLahir)
        binding.tvBayiBayiActJk.text = if (jk === "L") "Laki-laki" else "Perempuan"
        binding.tvBayiBayiActNik.text = nik.ifEmpty { "(tidak terdata)" }
        binding.tvBayiBayiActKk.text = kk.ifEmpty { "(tidak terdata)" }

        viewModel.responseGetImmunization.observe(this) { resp ->
            listImmunization.clear()
            if (resp.isNotEmpty()) {
                listImmunization = resp
                binding.llBayiDetOrgNoRiwayat.visibility = View.GONE
                binding.llBayiDetOrgRiwayatImun.visibility = View.VISIBLE
                setuprecyclerviewimun(listImmunization)
            } else {
                binding.llBayiDetOrgNoRiwayat.visibility = View.VISIBLE
                binding.llBayiDetOrgRiwayatImun.visibility = View.GONE
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
            binding.btnBayiActImunProses.isClickable = true
            if (!resp.error) {
                this.showToast(resp.message)
                back()
            } else {
                this.showToast(resp.message)
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            binding.btnBayiActImunProses.isClickable = true
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

        binding.btnBayiActImunTindakan.setOnClickListener {
            idImun = ""
            idBidan = ""
            binding.llBayiActImun.visibility = View.GONE
            binding.llBayiActImunTindakan.visibility = View.VISIBLE
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

        binding.btnBayiActImunBatal.setOnClickListener {
            idImun = ""
            idBidan = ""
            binding.llBayiActImun.visibility = View.VISIBLE
            binding.llBayiActImunTindakan.visibility = View.GONE
        }

        binding.btnBayiActImunProses.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(this)) {
                val tinggi = binding.eTBayiActImunTinggi
                val berat = binding.eTBayiActImunBerat
                if (idImun.isEmpty()) {
                    this.showToast("Imunisasi belum dipilih!")
                    it.isClickable = true
                } else if (tinggi.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(tinggi, it, "Field ini tidak boleh kosong")
                } else if (berat.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(berat, it, "Field ini tidak boleh kosong")
                } else if (idBidan.isEmpty()) {
                    this.showToast("Bidan belum dipilih!")
                    it.isClickable = true
                } else {
                    viewModel.process(idUser, id, idImun, berat.text.toString(), tinggi.text.toString(), idBidan, harga)
                }
            } else {
                it.isClickable = true
                this.showToast("Tidak ada koneksi internet")
            }
        }

        binding.ivBayiActImunToolbarBack.setOnClickListener {
            back()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { back() }
        })

        val swipeRefresh = binding.bayiActImunSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llBayiActImun.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llBayiActImun.visibility = View.VISIBLE
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

    private fun setuprecyclerviewimun(data: ArrayList<ChildImmunization>) {
        val rvTableImun = binding.rvBayiDetOrgRiwayatImun
        rvTableImun.layoutManager = LinearLayoutManager(this)
        rvTableImun.adapter = BabyImmunizationAdapter(data)
    }

    private fun back() {
        val intentBack = Intent()
        setResult(RESULT_OK, intentBack)
        finish()
    }
}