package id.my.apm.posyandu.ui.finance

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.adapter.ViewPagerFinanceAdapter
import id.my.apm.posyandu.databinding.ActivityFinanceBinding

@AndroidEntryPoint
class FinanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinanceBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFinanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        tabLayout = binding.tabLayoutFinance
        viewPager = binding.viewPagerFinance

        viewPager.adapter = ViewPagerFinanceAdapter(this)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Masuk"
                1 -> "Keluar"
                2 -> "Rincian"
                else -> "Tabs"
            }
        }.attach()

        binding.ivFinanceToolbarBack.setOnClickListener {
            finish()
        }
    }
}