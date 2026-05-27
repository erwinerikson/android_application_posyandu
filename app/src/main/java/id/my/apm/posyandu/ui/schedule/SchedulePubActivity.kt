package id.my.apm.posyandu.ui.schedule

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.adapter.SchedulePubAdapter
import id.my.apm.posyandu.databinding.ActivitySchedulePubBinding
import id.my.apm.posyandu.model.SchedulePub
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.use_case.GetAllSchedulePubUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class SchedulePubActivity : AppCompatActivity() {

    private lateinit var viewModel: SchedulePubViewModel
    private lateinit var binding: ActivitySchedulePubBinding
    @Inject
    lateinit var allRepository: AllRepository
    private var listSchedule = ArrayList<SchedulePub>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySchedulePubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val getAllSchedulePubUseCase = GetAllSchedulePubUseCase(allRepository)
        viewModel = SchedulePubViewModel(getAllSchedulePubUseCase)

        viewModel.responseGetSchedule.observe(this) { resp ->
            listSchedule.clear()
            if (resp.isNotEmpty()) {
                listSchedule = resp
                setuprecyclerview(listSchedule)
            } else {
                this.showToast("Tidak ada data")
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        binding.ivJadwalPubToolbarBack.setOnClickListener {
            finish()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getSchedule()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun setuprecyclerview(data: ArrayList<SchedulePub>) {
        val rvSchedule = binding.rvJadwalPub
        rvSchedule.layoutManager = LinearLayoutManager(this)
        rvSchedule.adapter = SchedulePubAdapter(data)
    }
}