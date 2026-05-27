package id.my.apm.posyandu.ui.baby

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.adapter.OrganizerAdapter
import id.my.apm.posyandu.databinding.ActivityBabyOrgBinding
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.repository.BabyRepository
import id.my.apm.posyandu.repository.RegistrationRepository
import id.my.apm.posyandu.use_case.baby.GetBabyOrgUseCase
import id.my.apm.posyandu.use_case.registration.RegistrationOrgUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class BabyOrgActivity : AppCompatActivity() {

    private lateinit var viewModel: BabyOrgViewModel
    private lateinit var binding: ActivityBabyOrgBinding
    @Inject
    lateinit var repository: BabyRepository
    @Inject
    lateinit var regRepository: RegistrationRepository
    private var listBaby = ArrayList<RegistrationPub>()
    private var idJadwal = "0"
    private var kode = "0"
    private var idMin = "0"

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.getData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBabyOrgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val registrationOrgUseCase = RegistrationOrgUseCase(regRepository)
        val getBabyOrgUseCase = GetBabyOrgUseCase(repository)
        viewModel = BabyOrgViewModel(registrationOrgUseCase, getBabyOrgUseCase)

        viewModel.responseRegistrationOrg.observe(this) { resp ->
            if (resp.error) {
                this.showToast(resp.message)
            } else {
                idJadwal = resp.id
                kode = resp.kode
                idMin = resp.idmin

                binding.llBayiOrg.visibility = View.VISIBLE

                when (kode) {
                    "1" -> {
                        binding.rlBayiOrgPendaftaran.visibility = View.GONE
                        binding.llBayiOrgData.visibility = View.GONE
                        binding.llBayiOrgNoData.visibility = View.VISIBLE
                        binding.tvBayiOrgNoData.text = getText(R.string.ada_layanan)
                    }
                    "2" -> {
                        binding.rlBayiOrgPendaftaran.visibility = View.VISIBLE
                        binding.llBayiOrgData.visibility = View.GONE
                        binding.llBayiOrgNoData.visibility = View.GONE
                        binding.tvBayiOrgNoData.text = ""
                        viewModel.getData()
                    }
                    "3" -> {
                        binding.rlBayiOrgPendaftaran.visibility = View.GONE
                        binding.llBayiOrgData.visibility = View.GONE
                        binding.llBayiOrgNoData.visibility = View.VISIBLE
                        binding.tvBayiOrgNoData.text = getText(R.string.layanan_selesai)
                    }
                    else -> {
                        binding.rlBayiOrgPendaftaran.visibility = View.GONE
                        binding.llBayiOrgData.visibility = View.GONE
                        binding.llBayiOrgNoData.visibility = View.VISIBLE
                        binding.tvBayiOrgNoData.text = getText(R.string.tidak_ada_layanan)
                    }
                }
            }
        }

        viewModel.responseGetDataBaby.observe(this) { resp ->
            listBaby.clear()
            if (resp.isNotEmpty()) {
                listBaby = resp
                setuprecyclerview(idMin, listBaby)
                binding.llBayiOrgJumlah.visibility = View.VISIBLE
                binding.tvBayiOrgJumlah.text = resp.size.toString()
                binding.llBayiOrgData.visibility = View.VISIBLE
            } else {
                binding.llBayiOrgData.visibility = View.GONE
                this.showToast("Tidak ada data")
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        val swipeRefresh = binding.bayiOrgSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llBayiOrg.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.checkRegistration()
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        binding.ivBayiOrgToolbarBack.setOnClickListener {
            finish()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.checkRegistration()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun setuprecyclerview(idMin: String, data: ArrayList<RegistrationPub>) {
        val rvBabySelect = binding.rvBayiOrg
        rvBabySelect.layoutManager = LinearLayoutManager(this)
        val adapter = OrganizerAdapter(idMin, data)
        adapter.setOnItemClickCallback(object : OrganizerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: RegistrationPub) {
                val intent = Intent(this@BabyOrgActivity, BabyOrgDetailActivity::class.java).apply {
                    putExtra("ID", data.id)
                    putExtra("ID_USER", data.idUser)
                    putExtra("ID_MIN", idMin)
                    putExtra("STATUS", data.status)
                }
                activityLauncher.launch(intent)
            }
        })
        rvBabySelect.adapter = adapter
    }
}