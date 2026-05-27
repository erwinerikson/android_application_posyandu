package id.my.apm.posyandu.ui.registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.databinding.ActivityRegistrationPubBinding
import id.my.apm.posyandu.repository.RegistrationRepository
import id.my.apm.posyandu.ui.user.LoginActivity
import id.my.apm.posyandu.ui.user.PublicActivity
import id.my.apm.posyandu.ui.user.UserActivity
import id.my.apm.posyandu.use_case.registration.BabyCheckUseCase
import id.my.apm.posyandu.use_case.registration.BabyRegistrationUseCase
import id.my.apm.posyandu.use_case.registration.CheckMomUseCase
import id.my.apm.posyandu.use_case.registration.ElderlyCheckUseCase
import id.my.apm.posyandu.use_case.registration.ElderlyRegistrationUseCase
import id.my.apm.posyandu.use_case.registration.GetRegistrationPubUseCase
import id.my.apm.posyandu.use_case.registration.MotherRegistrationUseCase
import id.my.apm.posyandu.use_case.registration.RegistrationPubUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import id.my.apm.posyandu.utils.SessionManager
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationPubActivity : AppCompatActivity() {

    private lateinit var viewModel: RegistrationPubViewModel
    private lateinit var binding: ActivityRegistrationPubBinding
    private lateinit var userType: String
    private var id = "0"
    private var kode = "0"
    private var statusBayi = ""
    private var statusIbu = ""
    private var statusLansia = ""
    private var layananBayi = ""
    private var layananIbu = ""
    private var layananIbuHamil = ""
    private var layananLansia = ""
    private var tambahBayi = "0"
    private var tambahLansia = "0"
    @Inject
    lateinit var repository: RegistrationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistrationPubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        val getRegistrationPubUseCase = GetRegistrationPubUseCase(repository)
        val babyCheckUseCase = BabyCheckUseCase(repository)
        val checkMomUseCase = CheckMomUseCase(repository)
        val elderlyCheckUseCase = ElderlyCheckUseCase(repository)
        val babyRegistrationUseCase = BabyRegistrationUseCase(repository)
        val motherRegistrationUseCase = MotherRegistrationUseCase(repository)
        val elderlyRegistrationUseCase = ElderlyRegistrationUseCase(repository)
        val registrationPubUseCase = RegistrationPubUseCase(repository)
        val sessionManager = SessionManager(this)
        viewModel = RegistrationPubViewModel(getRegistrationPubUseCase, babyCheckUseCase, checkMomUseCase, elderlyCheckUseCase,
            babyRegistrationUseCase, motherRegistrationUseCase, elderlyRegistrationUseCase, registrationPubUseCase, sessionManager)

        viewModel.responseGet.observe(this) { resp ->
            userType = resp.userType
            binding.tvPubDaftarNama.text = resp.nama
            binding.tvPubDaftarTelp.text = resp.telp
            viewModel.getData()
        }

        viewModel.errorGet.observe(this) { err ->
            if (err) {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        viewModel.responseRegistrationPub.observe(this) { resp ->
            if (resp.error) {
                this.showToast(resp.message)
            } else {
                id = resp.id
                kode = resp.kode

                when (kode) {
                    "1" -> {
                        binding.llPendaftaranPub.visibility = View.VISIBLE
                        binding.llTerdaftarPub.visibility = View.GONE
                        binding.llDaftarSelesaiPub.visibility = View.GONE
                        binding.llNoLayananPub.visibility = View.GONE
                    }
                    "2" -> {
                        binding.llPendaftaranPub.visibility = View.GONE
                        binding.llTerdaftarPub.visibility = View.VISIBLE
                        binding.llDaftarSelesaiPub.visibility = View.GONE
                        binding.llNoLayananPub.visibility = View.GONE

                        binding.tvPubDaftarIdPendaftaran.text = resp.id
                        binding.tvPubDaftarLayananBayi.visibility = if (resp.bayi == "1") View.VISIBLE else View.GONE
                        binding.tvPubDaftarLayananIbu.visibility = if (resp.ibu == "1") View.VISIBLE else View.GONE
                        binding.tvPubDaftarLayananIbuHamil.visibility = if (resp.ibuhamil == "1") View.VISIBLE else View.GONE
                        binding.tvPubDaftarLayananLansia.visibility = if (resp.lansia == "1") View.VISIBLE else View.GONE
                        binding.tvPubDaftarAntri.text = resp.antri
                    }
                    "3" -> {
                        binding.llPendaftaranPub.visibility = View.GONE
                        binding.llTerdaftarPub.visibility = View.GONE
                        binding.llDaftarSelesaiPub.visibility = View.VISIBLE
                        binding.llNoLayananPub.visibility = View.GONE
                    }
                    else -> {
                        binding.llPendaftaranPub.visibility = View.GONE
                        binding.llTerdaftarPub.visibility = View.GONE
                        binding.llDaftarSelesaiPub.visibility = View.GONE
                        binding.llNoLayananPub.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.responseBabyCheck.observe(this) { resp ->
            if (resp.error) {
                this.showToast(resp.message)
            } else {
                if (resp.message == "0") {
                    statusBayi = "0"
                    binding.llDaftarPubBayi.visibility = View.VISIBLE
                    binding.llDaftarPubTambahBayi.visibility = View.GONE
                } else if (resp.message == "1") {
                    statusBayi = "1"
                    binding.llDaftarPubBayi.visibility = View.GONE
                    binding.llDaftarPubTambahBayi.visibility = View.VISIBLE
                }
            }
        }

        viewModel.responseCheckMom.observe(this) { resp ->
            if (resp.error) {
                this.showToast(resp.message)
            } else {
                if (resp.message == "0") {
                    statusIbu = "0"
                    binding.llDaftarPubIbu.visibility = View.VISIBLE
                } else if (resp.message == "1") {
                    statusIbu = "1"
                    binding.llDaftarPubIbu.visibility = View.GONE
                }
            }
        }

        viewModel.responseCheckPregnant.observe(this) { resp ->
            if (resp.error) {
                this.showToast(resp.message)
            } else {
                if (resp.message == "0") {
                    this.showToast("Silahkan isi data ibu")
                    statusIbu = "0"
                    binding.llDaftarPubIbu.visibility = View.VISIBLE
                } else if (resp.message == "1") {
                    statusIbu = "1"
                    binding.llDaftarPubIbu.visibility = View.GONE
                }
            }
        }

        viewModel.responseElderlyCheck.observe(this) { resp ->
            if (resp.error) {
                this.showToast(resp.message)
            } else {
                if (resp.message == "0") {
                    statusLansia = "0"
                    binding.llDaftarPubLansia.visibility = View.VISIBLE
                    binding.llDaftarPubTambahLansia.visibility = View.GONE
                } else if (resp.message == "1") {
                    statusLansia = "1"
                    binding.llDaftarPubLansia.visibility = View.GONE
                    binding.llDaftarPubTambahLansia.visibility = View.VISIBLE
                }
            }
        }

        viewModel.responseBabyReg.observe(this) { resp ->
            if (resp.error) {
                binding.btnPubDaftar.isClickable = true
                this.showToast(resp.message)
            } else {
                if (resp.message == "Pendaftaran Berhasil") {
                    if (layananIbu == "1" || layananIbuHamil == "1") {
                        if (statusIbu == "0") {
                            prosesPendaftaranIbu(binding.btnPubDaftar)
                        } else {
                            if (layananLansia == "1") {
                                if (statusLansia == "0") {
                                    prosesPendaftaranLansia(binding.btnPubDaftar)
                                } else {
                                    prosesPendaftaran()
                                }
                            } else {
                                prosesPendaftaran()
                            }
                        }
                    } else {
                        if (layananLansia == "1") {
                            if (statusLansia == "0") {
                                prosesPendaftaranLansia(binding.btnPubDaftar)
                            } else {
                                prosesPendaftaran()
                            }
                        } else {
                            prosesPendaftaran()
                        }
                    }
                } else {
                    binding.btnPubDaftar.isClickable = true
                    this.showToast("Some error occurred please try again")
                }
            }
        }

        viewModel.responseMotherReg.observe(this) { resp ->
            if (resp.error) {
                binding.btnPubDaftar.isClickable = true
                this.showToast(resp.message)
            } else {
                if (resp.message == "Pendaftaran Berhasil") {
                    if (layananLansia == "1") {
                        if (statusLansia == "0") {
                            prosesPendaftaranLansia(binding.btnPubDaftar)
                        } else {
                            prosesPendaftaran()
                        }
                    } else {
                        prosesPendaftaran()
                    }
                } else {
                    binding.btnPubDaftar.isClickable = true
                    this.showToast("Some error occurred please try again")
                }
            }
        }

        viewModel.responseElderlyReg.observe(this) { resp ->
            if (resp.error) {
                binding.btnPubDaftar.isClickable = true
                this.showToast(resp.message)
            } else {
                if (resp.message == "Pendaftaran Berhasil") {
                    prosesPendaftaran()
                } else {
                    binding.btnPubDaftar.isClickable = true
                    this.showToast("Some error occurred please try again")
                }
            }
        }

        viewModel.responseRegistration.observe(this) { resp ->
            if (resp.error) {
                binding.btnPubDaftar.isClickable = true
                this.showToast(resp.message)
            } else {
                if (resp.message == "Pendaftaran Berhasil") {
                    binding.btnPubDaftar.isClickable = true
                    this.showToast(resp.message)
                    viewModel.getData()
                } else {
                    binding.btnPubDaftar.isClickable = true
                    this.showToast("Some error occurred please try again")
                }
            }
        }

        viewModel.errorRegistrationPub.observe(this) { err ->
            if (err) {
                this.showToast("Some error occurred please try again")
            }
        }

        viewModel.errorRegistrationPubReg.observe(this) { err ->
            if (err) {
                binding.btnPubDaftar.isClickable = true
                this.showToast("Some error occurred please try again")
            }
        }

        viewModel.errorRegistration.observe(this) { err ->
            if (err) {
                binding.btnPubDaftar.isClickable = true
                this.showToast("Some error occurred please try again")
            } else {
                binding.btnPubDaftar.isClickable = true
            }
        }

        val navBottom = binding.daftarPubBtnMenu
        navBottom.selectedItemId = R.id.menu_daftar
        navBottom.isItemHorizontalTranslationEnabled = true
        navBottom.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        navBottom.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home -> {
                    if (userType == "1") {
                        startActivity(Intent(this, PublicActivity::class.java))
                    }
                    finish()
                    true
                }
                R.id.menu_daftar -> {
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

        val cbBayi = binding.cbPubDaftarBayi
        val cbIbu = binding.cbPubDaftarIbu
        val cbIbuHamil = binding.cbPubDaftarIbuHamil
        val cbLansia = binding.cbPubDaftarLansia

        cbBayi.setOnClickListener {
            if (cbBayi.isChecked) {
                layananBayi = "1"
                if (statusBayi == "") {
                    if (AppUtils.isInternetAvailable(this)) {
                        viewModel.cekBayi()
                    } else {
                        this.showToast("Tidak ada koneksi internet")
                    }
                } else if (statusBayi == "0") {
                    binding.llDaftarPubBayi.visibility = View.VISIBLE
                    binding.llDaftarPubTambahBayi.visibility = View.GONE
                } else if (statusBayi == "1") {
                    binding.llDaftarPubBayi.visibility = View.GONE
                    binding.llDaftarPubTambahBayi.visibility = View.VISIBLE
                }
            } else {
                layananBayi = "0"
                binding.llDaftarPubBayi.visibility = View.GONE
                binding.llDaftarPubTambahBayi.visibility = View.GONE
            }
        }

        cbIbu.setOnClickListener {
            if (cbIbu.isChecked) {
                layananIbu = "1"
                if (statusIbu == "") {
                    if (AppUtils.isInternetAvailable(this)) {
                        viewModel.cekIbu()
                    } else {
                        this.showToast("Tidak ada koneksi internet")
                    }
                } else if (statusIbu == "0") {
                    binding.llDaftarPubIbu.visibility = View.VISIBLE
                } else if (statusIbu == "1") {
                    binding.llDaftarPubIbu.visibility = View.GONE
                }
            } else {
                layananIbu = "0"
                if (layananIbuHamil == "1") {
                    if (statusIbu == "0") {
                        binding.llDaftarPubIbu.visibility = View.VISIBLE
                    } else if (statusIbu == "1") {
                        binding.llDaftarPubIbu.visibility = View.GONE
                    }
                } else {
                    binding.llDaftarPubIbu.visibility = View.GONE
                }
            }
        }

        cbIbuHamil.setOnClickListener {
            if (cbIbuHamil.isChecked) {
                layananIbuHamil = "1"
                if (statusIbu == "") {
                    if (AppUtils.isInternetAvailable(this)) {
                        viewModel.cekIbuHamil()
                    } else {
                        this.showToast("Tidak ada koneksi internet")
                    }
                } else if (statusIbu == "0") {
                    this.showToast("Silahkan isi data ibu")
                    binding.llDaftarPubIbu.visibility = View.VISIBLE
                } else if (statusIbu == "1") {
                    binding.llDaftarPubIbu.visibility = View.GONE
                }
            } else {
                layananIbuHamil = "0"
                if (layananIbu == "1") {
                    if (statusIbu == "0") {
                        binding.llDaftarPubIbu.visibility = View.VISIBLE
                    } else if (statusIbu == "1") {
                        binding.llDaftarPubIbu.visibility = View.GONE
                    }
                } else {
                    binding.llDaftarPubIbu.visibility = View.GONE
                }
            }
        }

        cbLansia.setOnClickListener {
            if (cbLansia.isChecked) {
                layananLansia = "1"
                if (statusLansia == "") {
                    if (AppUtils.isInternetAvailable(this)) {
                        viewModel.cekLansia()
                    } else {
                        this.showToast("Tidak ada koneksi internet")
                    }
                } else if (statusLansia == "0") {
                    binding.llDaftarPubLansia.visibility = View.VISIBLE
                    binding.llDaftarPubTambahLansia.visibility = View.GONE
                } else if (statusLansia == "1") {
                    binding.llDaftarPubLansia.visibility = View.GONE
                    binding.llDaftarPubTambahLansia.visibility = View.VISIBLE
                }
            } else {
                layananLansia = "0"
                binding.llDaftarPubLansia.visibility = View.GONE
                binding.llDaftarPubTambahLansia.visibility = View.GONE
            }
        }

        binding.tvDaftarPubTglLahirBayi.setOnClickListener {
            AppUtils.datePicker(this, binding.tvDaftarPubTglLahirBayi)
        }

        binding.btnPubDaftar.setOnClickListener {
            if (AppUtils.isInternetAvailable(this)) {
                it.isClickable = false
                if (layananBayi == "1") {
                    if (statusBayi == "0") {
                        // bayi belum terdaftar
                        prosesPendaftaranBayi(it)
                    } else {
                        if (tambahBayi == "1") {
                            // tambah data bayi
                            prosesPendaftaranBayi(it)
                        } else {
                            if (layananIbu == "1" || layananIbuHamil == "1") {
                                if (statusIbu == "0") {
                                    // bayi sudah terdaftar sedangkan ibu belum terdaftar
                                    prosesPendaftaranIbu(it)
                                } else {
                                    if (layananLansia == "1") {
                                        if (statusLansia == "0") {
                                            // bayi dan ibu sudah terdaftar sedangkan lansia belum terdaftar
                                            prosesPendaftaranLansia(it)
                                        } else {
                                            if (tambahLansia == "1") {
                                                // tambah data lansia
                                                prosesPendaftaranLansia(it)
                                            } else {
                                                // bayi, ibu, dan lansia sudah terdaftar
                                                prosesPendaftaran()
                                            }
                                        }
                                    } else {
                                        // bayi dan ibu sudah terdaftar
                                        prosesPendaftaran()
                                    }
                                }
                            } else {
                                if (layananLansia == "1") {
                                    if (statusLansia == "0") {
                                        // bayi sudah terdaftar sedangkan lansia belum terdaftar
                                        prosesPendaftaranLansia(it)
                                    } else {
                                        if (tambahLansia == "1") {
                                            // tambah data lansia
                                            prosesPendaftaranLansia(it)
                                        } else {
                                            // bayi dan lansia sudah terdaftar
                                            prosesPendaftaran()
                                        }
                                    }
                                } else {
                                    // hanya bayi sudah terdaftar
                                    prosesPendaftaran()
                                }
                            }
                        }
                    }
                } else {
                    if (layananIbu == "1" || layananIbuHamil == "1") {
                        if (statusIbu == "0") {
                            // hanya ibu belum terdaftar
                            prosesPendaftaranIbu(it)
                        } else {
                            if (layananLansia == "1") {
                                if (statusLansia == "0") {
                                    // ibu sudah terdaftar sedangkan lansia belum terdaftar
                                    prosesPendaftaranLansia(it)
                                } else {
                                    if (tambahLansia == "1") {
                                        // tambah data lansia
                                        prosesPendaftaranLansia(it)
                                    } else {
                                        // ibu dan lansia sudah terdaftar
                                        prosesPendaftaran()
                                    }
                                }
                            } else {
                                // hanya ibu sudah terdaftar
                                prosesPendaftaran()
                            }
                        }
                    } else {
                        // hanya lansia
                        if (layananLansia == "1") {
                            if (statusLansia == "0") {
                                // hanya lansia belum terdaftar
                                prosesPendaftaranLansia(it)
                            } else {
                                if (tambahLansia == "1") {
                                    // tambah data lansia
                                    prosesPendaftaranLansia(it)
                                } else {
                                    // hanya lansia sudah terdaftar
                                    prosesPendaftaran()
                                }
                            }
                        }
                    }
                }

            } else {
                this.showToast("Tidak ada koneksi internet")
            }
        }

        binding.llDaftarPubTambahBayi.setOnClickListener {
            tambahBayi()
        }
        binding.ivPubDaftarTambahBayi.setOnClickListener {
            tambahBayi()
        }

        binding.llDaftarPubTambahLansia.setOnClickListener {
            tambahLansia()
        }
        binding.ivPubDaftarTambahLansia.setOnClickListener {
            tambahLansia()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getSession()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun prosesPendaftaranBayi(it: View) {
        val babyNames = binding.eTDaftarPubNamaBayi
        val fathersName = binding.eTDaftarPubNamaAyah
        val mothersName = binding.eTDaftarPubNamaIbuBayi
        val dateOfBirth = binding.tvDaftarPubTglLahirBayi
        val rgGender = binding.rgDaftarPubJkBayi.checkedRadioButtonId
        val rbGender = findViewById<RadioButton>(rgGender)
        val txtGender = rbGender.text.toString()
        val gender = if (txtGender == "Laki-laki") "L" else "P"
        val familyCard = binding.eTDaftarPubKkBayi
        val populationIdentificationNumber = binding.eTDaftarPubNikBayi

        if (babyNames.text.toString().trim().isEmpty()) {
            AppUtils.checkEt(babyNames, it, "Field ini tidak boleh kosong")
        } else if (fathersName.text.toString().trim().isEmpty()) {
            AppUtils.checkEt(fathersName, it, "Field ini tidak boleh kosong")
        } else if (mothersName.text.toString().trim().isEmpty()) {
            AppUtils.checkEt(mothersName, it, "Field ini tidak boleh kosong")
        } else if (dateOfBirth.text.toString().trim().isEmpty()) {
            it.isClickable = true
            this.showToast("Tanggal lahir tidak boleh kosong")
        } else {
            viewModel.pendaftaranBayi(babyNames.text.toString().trim(), fathersName.text.toString().trim(), mothersName.text.toString().trim(),
                dateOfBirth.text.toString().trim(), gender, familyCard.text.toString().trim(), populationIdentificationNumber.text.toString().trim())
        }
    }

    private fun prosesPendaftaranIbu(it: View) {
        val mothersName = binding.eTDaftarPubNamaIbu
        val husbandsName = binding.eTDaftarPubNamaSuami
        val age = binding.eTDaftarPubUmurIbu
        val populationIdentificationNumber = binding.eTDaftarPubNikIbu
        val familyCard = binding.eTDaftarPubKkIbu
        val healthInsuranceCardNumber = binding.eTDaftarPubBpjsIbu

        if (mothersName.text.toString().trim().isEmpty()) {
            AppUtils.checkEt(mothersName, it, "Field ini tidak boleh kosong")
        } else if (husbandsName.text.toString().trim().isEmpty()) {
            AppUtils.checkEt(husbandsName, it, "Field ini tidak boleh kosong")
        } else if (age.text.toString().trim().isEmpty()) {
            AppUtils.checkEt(age, it, "Field ini tidak boleh kosong")
        } else {
            viewModel.pendaftaranIbu(mothersName.text.toString().trim(), husbandsName.text.toString().trim(), age.text.toString().trim(),
                populationIdentificationNumber.text.toString().trim(), familyCard.text.toString().trim(), healthInsuranceCardNumber.text.toString().trim())
        }
    }

    private fun prosesPendaftaranLansia(it: View) {
        val elderlyName = binding.eTDaftarPubNamaLansia
        val age = binding.eTDaftarPubUmurLansia
        val rgGender = binding.rgDaftarPubJkLansia.checkedRadioButtonId
        val rbGender = findViewById<RadioButton>(rgGender)
        val txtGender = rbGender.text.toString()
        val gender = if (txtGender == "Laki-laki") "L" else "P"
        val populationIdentificationNumber = binding.eTDaftarPubNikLansia
        val familyCard = binding.eTDaftarPubKkLansia
        val healthInsuranceCardNumber = binding.eTDaftarPubBpjsLansia

        if (elderlyName.text.toString().trim().isEmpty()) {
            AppUtils.checkEt(elderlyName, it, "Field ini tidak boleh kosong")
        } else if (age.text.toString().trim().isEmpty()) {
            AppUtils.checkEt(age, it, "Field ini tidak boleh kosong")
        } else {
            viewModel.pendaftaranLansia(elderlyName.text.toString().trim(), age.text.toString().trim(), gender, populationIdentificationNumber.text.toString().trim(),
                familyCard.text.toString().trim(), healthInsuranceCardNumber.text.toString().trim())
        }
    }

    private fun prosesPendaftaran() {
        val baby = if (layananBayi == "") "0" else layananBayi
        val mother = if (layananIbu == "") "0" else layananIbu
        val pregnantMother = if (layananIbuHamil == "") "0" else layananIbuHamil
        val elderly = if (layananLansia == "") "0" else layananLansia
        viewModel.pendaftaran(baby, mother, pregnantMother, elderly)
    }

    private fun tambahBayi() {
        if (tambahBayi == "0") {
            binding.llDaftarPubBayi.visibility = View.VISIBLE
            tambahBayi = "1"
            binding.tvPubDaftarTambahBayi.text = getText(R.string.batalkan)
        } else if (tambahBayi == "1") {
            binding.llDaftarPubBayi.visibility = View.GONE
            tambahBayi = "0"
            binding.tvPubDaftarTambahBayi.text = getText(R.string.tambah_data_bayi)

            binding.eTDaftarPubNamaBayi.setText("")
            binding.eTDaftarPubNamaAyah.setText("")
            binding.eTDaftarPubNamaIbuBayi.setText("")
            binding.tvDaftarPubTglLahirBayi.text = ""
            binding.eTDaftarPubKkBayi.setText("")
            binding.eTDaftarPubNikBayi.setText("")
        }
    }

    private fun tambahLansia() {
        if (tambahLansia == "0") {
            binding.llDaftarPubLansia.visibility = View.VISIBLE
            tambahLansia = "1"
            binding.tvPubDaftarTambahLansia.text = getText(R.string.batalkan)
        } else if (tambahLansia == "1") {
            binding.llDaftarPubLansia.visibility = View.GONE
            tambahLansia = "0"
            binding.tvPubDaftarTambahLansia.text = getText(R.string.tambah_data_lansia)

            binding.eTDaftarPubNamaLansia.setText("")
            binding.eTDaftarPubUmurLansia.setText("")
            binding.eTDaftarPubNikLansia.setText("")
            binding.eTDaftarPubKkLansia.setText("")
            binding.eTDaftarPubBpjsLansia.setText("")
        }
    }
}