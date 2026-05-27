package id.my.apm.posyandu.ui.medication

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
import id.my.apm.posyandu.adapter.ViewPagerEquipmentAdapter
import id.my.apm.posyandu.databinding.ActivityMedicationBinding

@AndroidEntryPoint
class MedicationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMedicationBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMedicationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        tabLayout = binding.tabLayoutEquipment
        viewPager = binding.viewPagerEquipment

        viewPager.adapter = ViewPagerEquipmentAdapter(this)
        //viewPager.offscreenPageLimit = 3

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Obat"
                1 -> "Imunisasi"
                2 -> "Periksa"
                3 -> "Bidan"
                else -> "Tabs"
            }
        }.attach()

        binding.ivEquipmentToolbarBack.setOnClickListener {
            finish()
        }
    }
}