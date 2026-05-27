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
import id.my.apm.posyandu.databinding.ActivityCounselingOrgBinding

class CounselingOrgActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCounselingOrgBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCounselingOrgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val swipeRefresh = binding.penyuluhanOrgSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.rlOrgPenyuluhan.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.rlOrgPenyuluhan.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
            }, 1500)
        }

        binding.ivPenyuluhanOrgToolbarBack.setOnClickListener {
            finish()
        }
    }
}