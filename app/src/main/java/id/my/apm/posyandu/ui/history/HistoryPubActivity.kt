package id.my.apm.posyandu.ui.history

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.adapter.HistoryPubAdapter
import id.my.apm.posyandu.databinding.ActivityHistoryPubBinding
import id.my.apm.posyandu.model.HistoryPub
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.use_case.GetAllHistoryPubUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import id.my.apm.posyandu.utils.SessionManager
import javax.inject.Inject

@AndroidEntryPoint
class HistoryPubActivity : AppCompatActivity() {

    private lateinit var viewModel: HistoryPubViewModel
    private lateinit var binding: ActivityHistoryPubBinding
    @Inject
    lateinit var allRepository: AllRepository
    private var listHistory = ArrayList<HistoryPub>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryPubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val getAllHistoryPubUseCase = GetAllHistoryPubUseCase(allRepository)
        val sessionManager = SessionManager(this)
        viewModel = HistoryPubViewModel(getAllHistoryPubUseCase, sessionManager)

        viewModel.responseGetHistory.observe(this) { resp ->
            listHistory.clear()
            if (resp.isNotEmpty()) {
                listHistory = resp
                setuprecyclerview(listHistory)
            } else {
                this.showToast("Tidak ada data")
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        binding.ivRiwayatPubToolbarBack.setOnClickListener {
            finish()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getHistory()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun setuprecyclerview(data: ArrayList<HistoryPub>) {
        val rvHistory = binding.rvRiwayatPub
        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = HistoryPubAdapter(data)
    }
}