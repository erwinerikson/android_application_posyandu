package id.my.apm.posyandu.ui.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView
import id.my.apm.posyandu.R
import id.my.apm.posyandu.databinding.ActivityPublicBinding
import id.my.apm.posyandu.ui.baby.BabyPubActivity
import id.my.apm.posyandu.ui.counseling.CounselingPubActivity
import id.my.apm.posyandu.ui.elderly.ElderlyPubActivity
import id.my.apm.posyandu.ui.history.HistoryPubActivity
import id.my.apm.posyandu.ui.mother.MotherPubActivity
import id.my.apm.posyandu.ui.pregnant.PregnantPubActivity
import id.my.apm.posyandu.ui.registration.RegistrationPubActivity
import id.my.apm.posyandu.ui.schedule.SchedulePubActivity
import id.my.apm.posyandu.ui.sdidtk.SdidtkPubActivity
import id.my.apm.posyandu.utils.SessionManager

class PublicActivity : AppCompatActivity() {

    private lateinit var viewModel: PublicViewModel
    private lateinit var binding: ActivityPublicBinding
    private lateinit var userType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPublicBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val sessionManager = SessionManager(this)
        viewModel = PublicViewModel(sessionManager)

        viewModel.responseGet.observe(this) { resp ->
            userType = resp.userType
            //binding.tvUserNama.text = resp.nama
            //binding.tvUserTelp.text = resp.telp
            //binding.tvUserAlamat.text = resp.alamat
        }

        viewModel.errorGet.observe(this) { err ->
            if (err) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        val navBottom = binding.pubBtnMenu
        navBottom.selectedItemId = R.id.menu_home
        navBottom.isItemHorizontalTranslationEnabled = true
        navBottom.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        navBottom.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    //navBottom.selectedItemId = R.id.menu_home
                    true
                }
                R.id.menu_daftar -> {
                    startActivity(Intent(this, RegistrationPubActivity::class.java))
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

        binding.cvPubBayiBalita.setOnClickListener {
            startActivity(Intent(this, BabyPubActivity::class.java))
        }

        binding.cvPubIbu.setOnClickListener {
            startActivity(Intent(this, MotherPubActivity::class.java))
        }

        binding.cvPubLansia.setOnClickListener {
            startActivity(Intent(this, ElderlyPubActivity::class.java))
        }

        binding.cvPubIbuHamil.setOnClickListener {
            startActivity(Intent(this, PregnantPubActivity::class.java))
        }

        binding.cvPubPenyuluhan.setOnClickListener {
            startActivity(Intent(this, CounselingPubActivity::class.java))
        }

        binding.cvPubTumbuh.setOnClickListener {
            startActivity(Intent(this, SdidtkPubActivity::class.java))
        }

        binding.cvPubRiwayat.setOnClickListener {
            startActivity(Intent(this, HistoryPubActivity::class.java))
        }

        binding.cvPubJadwal.setOnClickListener {
            startActivity(Intent(this, SchedulePubActivity::class.java))
        }

        /*val recyclerView = binding.rvHomePub
        val arrayList = ArrayList<Any>()
        arrayList.add(R.string.bayi_balita, R.drawable.bayi)
        arrayList.add(R.string.ibu, R.drawable.ibu)
        arrayList.add(R.string.lansia, R.drawable.lansia)
        arrayList.add(R.string.ibu_hamil, R.drawable.ibu_hamil)
        arrayList.add(R.string.penyuluhan, R.drawable.penyuluhan)
        arrayList.add(R.string.sdidtk, R.drawable.tumbuh)
        arrayList.add(R.string.riwayat, R.drawable.riwayat)
        arrayList.add(R.string.jadwal, R.drawable.jadwal)

        recyclerView.run {
            arrayList.add(R.string.bayi_balita, R.drawable.bayi)
            arrayList.add(R.string.ibu, R.drawable.ibu)
            arrayList.add(R.string.lansia, R.drawable.lansia)
            arrayList.add(R.string.ibu_hamil, R.drawable.ibu_hamil)
            arrayList.add(R.string.penyuluhan, R.drawable.penyuluhan)
            arrayList.add(R.string.sdidtk, R.drawable.tumbuh)
            arrayList.add(R.string.riwayat, R.drawable.riwayat)
            arrayList.add(R.string.jadwal, R.drawable.jadwal)

            setLayoutManager(LinearLayoutManager(context))
        }*/

        viewModel.getSession()
    }
}