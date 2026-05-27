package id.my.apm.posyandu.ui.counseling

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import id.my.apm.posyandu.R
import id.my.apm.posyandu.databinding.ActivityCounselingPubBinding

class CounselingPubActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCounselingPubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCounselingPubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val swipeRefresh = binding.penyuluhanPubSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.rlPubPenyuluhan.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.rlPubPenyuluhan.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
            }, 1500)
        }

        binding.ivPenyuluhanPubToolbarBack.setOnClickListener {
            finish()
        }
    }
}