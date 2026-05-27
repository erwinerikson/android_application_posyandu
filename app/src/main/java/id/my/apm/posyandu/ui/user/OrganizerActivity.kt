package id.my.apm.posyandu.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView
import id.my.apm.posyandu.R
import id.my.apm.posyandu.databinding.ActivityOrganizerBinding
import id.my.apm.posyandu.ui.baby.BabyOrgActivity
import id.my.apm.posyandu.ui.counseling.CounselingOrgActivity
import id.my.apm.posyandu.ui.elderly.ElderlyOrgActivity
import id.my.apm.posyandu.ui.finance.FinanceActivity
import id.my.apm.posyandu.ui.medication.MedicationActivity
import id.my.apm.posyandu.ui.mother.MotherOrgActivity
import id.my.apm.posyandu.ui.pregnant.PregnantOrgActivity
import id.my.apm.posyandu.ui.registration.RegistrationOrgActivity
import id.my.apm.posyandu.ui.schedule.ScheduleOrgActivity
import id.my.apm.posyandu.ui.sdidtk.SdidtkOrgActivity

class OrganizerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrganizerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOrganizerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val navBottom = binding.orgBtnMenu
        navBottom.selectedItemId = R.id.menu_home
        navBottom.isItemHorizontalTranslationEnabled = true
        navBottom.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        navBottom.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    true
                }
                R.id.menu_daftar -> {
                    startActivity(Intent(this, RegistrationOrgActivity::class.java))
                    finish()
                    true
                }
                R.id.menu_user -> {
                    startActivity(Intent(this, UserActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }

        binding.cvOrgBayiBalita.setOnClickListener {
            startActivity(Intent(this, BabyOrgActivity::class.java))
        }

        binding.cvOrgIbu.setOnClickListener {
            startActivity(Intent(this, MotherOrgActivity::class.java))
        }

        binding.cvOrgLansia.setOnClickListener {
            startActivity(Intent(this, ElderlyOrgActivity::class.java))
        }

        binding.cvOrgIbuHamil.setOnClickListener {
            startActivity(Intent(this, PregnantOrgActivity::class.java))
        }

        binding.cvOrgPenyuluhan.setOnClickListener {
            startActivity(Intent(this, CounselingOrgActivity::class.java))
        }

        binding.cvOrgTumbuh.setOnClickListener {
            startActivity(Intent(this, SdidtkOrgActivity::class.java))
        }

        binding.cvOrgKeuangan.setOnClickListener {
            startActivity(Intent(this, FinanceActivity::class.java))
        }

        binding.cvOrgDataObat.setOnClickListener {
            startActivity(Intent(this, MedicationActivity::class.java))
        }

        binding.cvOrgJadwal.setOnClickListener {
            startActivity(Intent(this, ScheduleOrgActivity::class.java))
        }
    }
}