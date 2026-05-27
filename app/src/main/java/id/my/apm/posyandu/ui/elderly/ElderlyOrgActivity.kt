package id.my.apm.posyandu.ui.elderly

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
import id.my.apm.posyandu.databinding.ActivityElderlyOrgBinding
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.repository.ElderlyRepository
import id.my.apm.posyandu.repository.RegistrationRepository
import id.my.apm.posyandu.ui.registration.RegistrationOrgDetailActivity
import id.my.apm.posyandu.use_case.elderly.GetElderlyOrgUseCase
import id.my.apm.posyandu.use_case.registration.RegistrationOrgUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class ElderlyOrgActivity : AppCompatActivity() {

    private lateinit var viewModel: ElderlyOrgViewModel
    private lateinit var binding: ActivityElderlyOrgBinding
    @Inject
    lateinit var repository: ElderlyRepository
    @Inject
    lateinit var regRepository: RegistrationRepository
    private var listElderly = ArrayList<RegistrationPub>()
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
        binding = ActivityElderlyOrgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val registrationOrgUseCase = RegistrationOrgUseCase(regRepository)
        val getElderlyOrgUseCase = GetElderlyOrgUseCase(repository)
        viewModel = ElderlyOrgViewModel(registrationOrgUseCase, getElderlyOrgUseCase)

        viewModel.responseRegistrationOrg.observe(this) { resp ->
            if (resp.error) {
                this.showToast(resp.message)
            } else {
                idJadwal = resp.id
                kode = resp.kode
                idMin = resp.idmin

                binding.llLansiaOrg.visibility = View.VISIBLE

                when (kode) {
                    "1" -> {
                        binding.rlLansiaOrgPendaftaran.visibility = View.GONE
                        binding.llLansiaOrgData.visibility = View.GONE
                        binding.llLansiaOrgNoData.visibility = View.VISIBLE
                        binding.tvLansiaOrgNoData.text = getText(R.string.ada_layanan)
                    }
                    "2" -> {
                        binding.rlLansiaOrgPendaftaran.visibility = View.VISIBLE
                        binding.llLansiaOrgData.visibility = View.GONE
                        binding.llLansiaOrgNoData.visibility = View.GONE
                        binding.tvLansiaOrgNoData.text = ""
                        viewModel.getData()
                    }
                    "3" -> {
                        binding.rlLansiaOrgPendaftaran.visibility = View.GONE
                        binding.llLansiaOrgData.visibility = View.GONE
                        binding.llLansiaOrgNoData.visibility = View.VISIBLE
                        binding.tvLansiaOrgNoData.text = getText(R.string.layanan_selesai)
                    }
                    else -> {
                        binding.rlLansiaOrgPendaftaran.visibility = View.GONE
                        binding.llLansiaOrgData.visibility = View.GONE
                        binding.llLansiaOrgNoData.visibility = View.VISIBLE
                        binding.tvLansiaOrgNoData.text = getText(R.string.tidak_ada_layanan)
                    }
                }
            }
        }

        viewModel.responseGetDataElderly.observe(this) { resp ->
            listElderly.clear()
            if (resp.isNotEmpty()) {
                listElderly = resp
                setuprecyclerview(idMin, listElderly)
                binding.llLansiaOrgJumlah.visibility = View.VISIBLE
                binding.tvLansiaOrgJumlah.text = resp.size.toString()
                binding.llLansiaOrgData.visibility = View.VISIBLE
            } else {
                binding.llLansiaOrgData.visibility = View.GONE
                this.showToast("Tidak ada data")
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        val swipeRefresh = binding.lansiaOrgSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llLansiaOrg.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.checkRegistration()
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        binding.ivLansiaOrgToolbarBack.setOnClickListener {
            finish()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.checkRegistration()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun setuprecyclerview(idMin: String, data: ArrayList<RegistrationPub>) {
        val rvBabySelect = binding.rvLansiaOrg
        rvBabySelect.layoutManager = LinearLayoutManager(this)
        val adapter = OrganizerAdapter(idMin, data)
        adapter.setOnItemClickCallback(object : OrganizerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: RegistrationPub) {
                val intent = Intent(this@ElderlyOrgActivity, ElderlyOrgDetailActivity::class.java).apply {
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