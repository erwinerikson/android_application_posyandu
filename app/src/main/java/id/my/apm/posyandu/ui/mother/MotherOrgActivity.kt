package id.my.apm.posyandu.ui.mother

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
import id.my.apm.posyandu.databinding.ActivityMotherOrgBinding
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.repository.MotherRepository
import id.my.apm.posyandu.repository.RegistrationRepository
import id.my.apm.posyandu.ui.registration.RegistrationOrgDetailActivity
import id.my.apm.posyandu.use_case.mother.GetMotherOrgUseCase
import id.my.apm.posyandu.use_case.registration.RegistrationOrgUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class MotherOrgActivity : AppCompatActivity() {

    private lateinit var viewModel: MotherOrgViewModel
    private lateinit var binding: ActivityMotherOrgBinding
    @Inject
    lateinit var repository: MotherRepository
    @Inject
    lateinit var regRepository: RegistrationRepository
    private var listMother = ArrayList<RegistrationPub>()
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
        binding = ActivityMotherOrgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val registrationOrgUseCase = RegistrationOrgUseCase(regRepository)
        val getMotherOrgUseCase = GetMotherOrgUseCase(repository)
        viewModel = MotherOrgViewModel(registrationOrgUseCase, getMotherOrgUseCase)

        viewModel.responseRegistrationOrg.observe(this) { resp ->
            if (resp.error) {
                this.showToast(resp.message)
            } else {
                idJadwal = resp.id
                kode = resp.kode
                idMin = resp.idmin

                binding.llIbuOrg.visibility = View.VISIBLE

                when (kode) {
                    "1" -> {
                        binding.rlIbuOrgPendaftaran.visibility = View.GONE
                        binding.llIbuOrgData.visibility = View.GONE
                        binding.llIbuOrgNoData.visibility = View.VISIBLE
                        binding.tvIbuOrgNoData.text = getText(R.string.ada_layanan)
                    }
                    "2" -> {
                        binding.rlIbuOrgPendaftaran.visibility = View.VISIBLE
                        binding.llIbuOrgData.visibility = View.GONE
                        binding.llIbuOrgNoData.visibility = View.GONE
                        binding.tvIbuOrgNoData.text = ""
                        viewModel.getData()
                    }
                    "3" -> {
                        binding.rlIbuOrgPendaftaran.visibility = View.GONE
                        binding.llIbuOrgData.visibility = View.GONE
                        binding.llIbuOrgNoData.visibility = View.VISIBLE
                        binding.tvIbuOrgNoData.text = getText(R.string.layanan_selesai)
                    }
                    else -> {
                        binding.rlIbuOrgPendaftaran.visibility = View.GONE
                        binding.llIbuOrgData.visibility = View.GONE
                        binding.llIbuOrgNoData.visibility = View.VISIBLE
                        binding.tvIbuOrgNoData.text = getText(R.string.tidak_ada_layanan)
                    }
                }
            }
        }

        viewModel.responseGetDataMother.observe(this) { resp ->
            listMother.clear()
            if (resp.isNotEmpty()) {
                listMother = resp
                setuprecyclerview(idMin, listMother)
                binding.llIbuOrgJumlah.visibility = View.VISIBLE
                binding.tvIbuOrgJumlah.text = resp.size.toString()
                binding.llIbuOrgData.visibility = View.VISIBLE
            } else {
                binding.llIbuOrgData.visibility = View.GONE
                this.showToast("Tidak ada data")
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        val swipeRefresh = binding.ibuOrgSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llIbuOrg.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(this)) {
                    viewModel.checkRegistration()
                } else {
                    this.showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        binding.ivIbuOrgToolbarBack.setOnClickListener {
            finish()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.checkRegistration()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun setuprecyclerview(idMin: String, data: ArrayList<RegistrationPub>) {
        val rvBabySelect = binding.rvIbuOrg
        rvBabySelect.layoutManager = LinearLayoutManager(this)
        val adapter = OrganizerAdapter(idMin, data)
        adapter.setOnItemClickCallback(object : OrganizerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: RegistrationPub) {
                val intent = Intent(this@MotherOrgActivity, MotherOrgDetailActivity::class.java).apply {
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