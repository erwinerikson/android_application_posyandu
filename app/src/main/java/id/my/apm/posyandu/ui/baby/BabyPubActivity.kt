package id.my.apm.posyandu.ui.baby

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
import id.my.apm.posyandu.adapter.baby.BabyImmunizationAdapter
import id.my.apm.posyandu.adapter.baby.BabyPubSelectAdapter
import id.my.apm.posyandu.databinding.ActivityBabyPubBinding
import id.my.apm.posyandu.model.BabyPub
import id.my.apm.posyandu.model.ChildImmunization
import id.my.apm.posyandu.repository.BabyRepository
import id.my.apm.posyandu.use_case.baby.CheckBabyPubUseCase
import id.my.apm.posyandu.use_case.baby.GetBabiesPubUseCase
import id.my.apm.posyandu.use_case.baby.GetBabyImmunizationUseCase
import id.my.apm.posyandu.use_case.baby.GetBabyPubUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import id.my.apm.posyandu.utils.SessionManager
import javax.inject.Inject

@AndroidEntryPoint
class BabyPubActivity : AppCompatActivity() {

    private lateinit var viewModel: BabyPubViewModel
    private lateinit var binding: ActivityBabyPubBinding
    @Inject
    lateinit var repository: BabyRepository
    private var listBabySelect = ArrayList<BabyPub>()
    private var listImmunization = ArrayList<ChildImmunization>()
    private lateinit var daftarRiwayat: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBabyPubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val checkBabyPubUseCase = CheckBabyPubUseCase(repository)
        val getBabyPubUseCase = GetBabyPubUseCase(repository)
        val getBabiesPubUseCase = GetBabiesPubUseCase(repository)
        val getBabyImmunizationUseCase = GetBabyImmunizationUseCase(repository)
        val sessionManager = SessionManager(this)
        viewModel = BabyPubViewModel(checkBabyPubUseCase, getBabyPubUseCase, getBabiesPubUseCase, getBabyImmunizationUseCase, sessionManager)

        viewModel.responseCheckBaby.observe(this) { resp ->
            if (resp.error) {
                this.showToast(resp.message)
            } else {
                when (resp.kode) {
                    "2" -> {
                        viewModel.getBabies()
                    }
                    "1" -> {
                        viewModel.getBaby(resp.id)
                    }
                    else -> {
                        this.showToast(resp.message)
                        binding.rvBayiPub.visibility = View.GONE
                        binding.llPubBayiData.visibility = View.GONE
                        binding.llPubBayiNoData.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.responseGetBaby.observe(this) { resp ->
            if (resp != null) {
                binding.tvPubBayiNama.text = resp.nama
                binding.tvPubBayiNamaIbu.text = resp.namaIbu
                binding.tvPubBayiNamaAyah.text = resp.namaAyah
                binding.tvPubBayiTglLahir.text = AppUtils.formatterDateDay(resp.tglLahir)
                binding.tvPubBayiUmur.text = AppUtils.calculateAge(resp.tglLahir)
                binding.tvPubBayiJk.text = if (resp.jk === "L") "Laki-laki" else "Perempuan"
                binding.tvPubBayiNik.text = resp.nik.ifEmpty { "(tidak terdata)" }
                binding.tvPubBayiKk.text = resp.kk.ifEmpty { "(tidak terdata)" }
                binding.rvBayiPub.visibility = View.GONE
                binding.llPubBayiData.visibility = View.VISIBLE
                binding.llPubBayiNoData.visibility = View.GONE
                viewModel.getImmunization(resp.id)
            } else {
                this.showToast("Tidak ada data")
            }
        }

        viewModel.responseGetBabySelect.observe(this) { resp ->
            if (resp != null) {
                binding.tvPubBayiNama.text = resp.nama
                binding.tvPubBayiNamaIbu.text = resp.namaIbu
                binding.tvPubBayiNamaAyah.text = resp.namaAyah
                binding.tvPubBayiTglLahir.text = AppUtils.formatterDateDay(resp.tglLahir)
                binding.tvPubBayiUmur.text = AppUtils.calculateAge(resp.tglLahir)
                binding.tvPubBayiJk.text = if (resp.jk === "L") "Laki-laki" else "Perempuan"
                binding.tvPubBayiNik.text = resp.nik.ifEmpty { "(tidak terdata)" }
                binding.tvPubBayiKk.text = resp.kk.ifEmpty { "(tidak terdata)" }
                binding.rvBayiPub.visibility = View.VISIBLE
                binding.llPubBayiData.visibility = View.VISIBLE
                binding.llPubBayiNoData.visibility = View.GONE
                viewModel.getImmunization(resp.id)
            } else {
                this.showToast("Tidak ada data")
            }
        }

        viewModel.responseGetBabies.observe(this) { resp ->
            listBabySelect.clear()
            listBabySelect = resp
            binding.rvBayiPub.visibility = View.VISIBLE
            binding.llPubBayiData.visibility = View.GONE
            binding.llPubBayiNoData.visibility = View.GONE

            binding.rlPubBayiDropRiwayat.visibility = View.GONE
            setuprecyclerview(listBabySelect)
        }

        viewModel.responseGetImmunization.observe(this) { resp ->
            listImmunization.clear()
            if (resp.isNotEmpty()) {
                listImmunization = resp
                daftarRiwayat = listOf("Silahkan pilih riwayat", "Imunisasi")
                dropDownSpinner()
                binding.rlPubBayiDropRiwayat.visibility = View.VISIBLE
                binding.llPubBayiNoRiwayat.visibility = View.GONE
                binding.llPubBayiRiwayatImun.visibility = View.GONE
                setuprecyclerviewimun(listImmunization)
            } else {
                binding.rlPubBayiDropRiwayat.visibility = View.GONE
                binding.llPubBayiNoRiwayat.visibility = View.VISIBLE
                binding.llPubBayiRiwayatImun.visibility = View.GONE
            }
        }

        viewModel.errorCheckBaby.observe(this) { resp ->
            if (resp) {
                this.showToast("Some error occurred please try again")
            }
        }

        val swipeRefresh = binding.bayiPubSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llPubBayi.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llPubBayi.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.checkBaby()
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        binding.ivBayiPubToolbarBack.setOnClickListener {
            finish()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.checkBaby()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun dropDownSpinner() {
        val spinnerRiwayat: Spinner = binding.pubBayiDropRiwayat
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, daftarRiwayat)
        adapter.setDropDownViewResource(R.layout.item_spinner_dropdown)
        spinnerRiwayat.adapter = adapter
        spinnerRiwayat.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val select = daftarRiwayat[position]
                if (select == "Imunisasi") {
                    binding.llPubBayiRiwayatImun.visibility = View.VISIBLE
                } else {
                    binding.llPubBayiRiwayatImun.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setuprecyclerview(data: ArrayList<BabyPub>) {
        val rvBabySelect = binding.rvBayiPub
        rvBabySelect.layoutManager = LinearLayoutManager(this)
        val adapter = BabyPubSelectAdapter(data)
        adapter.setOnItemClickCallback(object : BabyPubSelectAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                viewModel.getBabySelect(data)
            }
        })
        rvBabySelect.adapter = adapter
    }

    private fun setuprecyclerviewimun(data: ArrayList<ChildImmunization>) {
        val rvTableImun = binding.rvPubBayiRiwayatImun
        rvTableImun.layoutManager = LinearLayoutManager(this)
        rvTableImun.adapter = BabyImmunizationAdapter(data)
    }
}