package id.my.apm.posyandu.ui.pregnant

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
import id.my.apm.posyandu.databinding.ActivityPregnantOrgBinding
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.repository.MotherRepository
import id.my.apm.posyandu.repository.RegistrationRepository
import id.my.apm.posyandu.ui.registration.RegistrationOrgDetailActivity
import id.my.apm.posyandu.use_case.pregnant.GetPregnantOrgUseCase
import id.my.apm.posyandu.use_case.registration.RegistrationOrgUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class PregnantOrgActivity : AppCompatActivity() {

    private lateinit var viewModel: PregnantOrgViewModel
    private lateinit var binding: ActivityPregnantOrgBinding
    @Inject
    lateinit var repository: MotherRepository
    @Inject
    lateinit var regRepository: RegistrationRepository
    private var listPregnant = ArrayList<RegistrationPub>()
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
        binding = ActivityPregnantOrgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val registrationOrgUseCase = RegistrationOrgUseCase(regRepository)
        val getPregnantOrgUseCase = GetPregnantOrgUseCase(repository)
        viewModel = PregnantOrgViewModel(registrationOrgUseCase, getPregnantOrgUseCase)

        viewModel.responseRegistrationOrg.observe(this) { resp ->
            if (resp.error) {
                this.showToast(resp.message)
            } else {
                idJadwal = resp.id
                kode = resp.kode
                idMin = resp.idmin

                binding.llIbuHamilOrg.visibility = View.VISIBLE

                when (kode) {
                    "1" -> {
                        binding.rlIbuHamilOrgPendaftaran.visibility = View.GONE
                        binding.llIbuHamilOrgData.visibility = View.GONE
                        binding.llIbuHamilOrgNoData.visibility = View.VISIBLE
                        binding.tvIbuHamilOrgNoData.text = getText(R.string.ada_layanan)
                    }
                    "2" -> {
                        binding.rlIbuHamilOrgPendaftaran.visibility = View.VISIBLE
                        binding.llIbuHamilOrgData.visibility = View.GONE
                        binding.llIbuHamilOrgNoData.visibility = View.GONE
                        binding.tvIbuHamilOrgNoData.text = ""
                        viewModel.getData()
                    }
                    "3" -> {
                        binding.rlIbuHamilOrgPendaftaran.visibility = View.GONE
                        binding.llIbuHamilOrgData.visibility = View.GONE
                        binding.llIbuHamilOrgNoData.visibility = View.VISIBLE
                        binding.tvIbuHamilOrgNoData.text = getText(R.string.layanan_selesai)
                    }
                    else -> {
                        binding.rlIbuHamilOrgPendaftaran.visibility = View.GONE
                        binding.llIbuHamilOrgData.visibility = View.GONE
                        binding.llIbuHamilOrgNoData.visibility = View.VISIBLE
                        binding.tvIbuHamilOrgNoData.text = getText(R.string.tidak_ada_layanan)
                    }
                }
            }
        }

        viewModel.responseGetDataPregnant.observe(this) { resp ->
            listPregnant.clear()
            if (resp.isNotEmpty()) {
                listPregnant = resp
                setuprecyclerview(idMin, listPregnant)
                binding.llIbuHamilOrgJumlah.visibility = View.VISIBLE
                binding.tvIbuHamilOrgJumlah.text = resp.size.toString()
                binding.llIbuHamilOrgData.visibility = View.VISIBLE
            } else {
                binding.llIbuHamilOrgData.visibility = View.GONE
                this.showToast("Tidak ada data")
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        val swipeRefresh = binding.ibuHamilOrgSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llIbuHamilOrg.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.checkRegistration()
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        binding.ivIbuHamilOrgToolbarBack.setOnClickListener {
            finish()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.checkRegistration()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun setuprecyclerview(idMin: String, data: ArrayList<RegistrationPub>) {
        val rvBabySelect = binding.rvIbuHamilOrg
        rvBabySelect.layoutManager = LinearLayoutManager(this)
        val adapter = OrganizerAdapter(idMin, data)
        adapter.setOnItemClickCallback(object : OrganizerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: RegistrationPub) {
                val intent = Intent(this@PregnantOrgActivity, PregnantOrgDetailActivity::class.java).apply {
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