package id.my.apm.posyandu.ui.schedule

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.adapter.ScheduleOrgAdapter
import id.my.apm.posyandu.databinding.ActivityScheduleOrgBinding
import id.my.apm.posyandu.model.SchedulePub
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.use_case.EditScheduleUseCase
import id.my.apm.posyandu.use_case.GetAllSchedulePubUseCase
import id.my.apm.posyandu.use_case.SaveScheduleUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class ScheduleOrgActivity : AppCompatActivity() {

    private lateinit var viewModel: ScheduleOrgViewModel
    private lateinit var binding: ActivityScheduleOrgBinding
    @Inject
    lateinit var allRepository: AllRepository
    private lateinit var spinnerStatus: Spinner
    private lateinit var listStatus: List<String>
    private lateinit var adapter: ArrayAdapter<String>
    private var listSchedule = ArrayList<SchedulePub>()
    private var idSchedule = ""
    private var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScheduleOrgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val progressDialog = Dialog(this)
        AppUtils.progressDialog(progressDialog)

        val getAllSchedulePubUseCase = GetAllSchedulePubUseCase(allRepository)
        val editScheduleUseCase = EditScheduleUseCase(allRepository)
        val saveScheduleUseCase = SaveScheduleUseCase(allRepository)
        viewModel = ScheduleOrgViewModel(getAllSchedulePubUseCase, editScheduleUseCase, saveScheduleUseCase)

        spinnerStatus = binding.spinnerJadwalOrgStatus

        viewModel.responseGetSchedule.observe(this) { resp ->
            progressDialog.dismiss()
            listSchedule.clear()
            if (resp.isNotEmpty()) {
                listSchedule = resp
                setuprecyclerview(listSchedule)
                binding.llJadwalOrgData.visibility = View.VISIBLE
                binding.llJadwalOrgEdit.visibility = View.GONE
            } else {
                this.showToast("Tidak ada data")
            }
        }

        viewModel.responseSaveSchedule.observe(this) { resp ->
            binding.btnJadwalOrgProses.isClickable = true
            if (resp.error) {
                progressDialog.dismiss()
                this.showToast(resp.message)
            } else {
                viewModel.getSchedule()
                binding.tvJadwalOrgEditTgl.text = ""
                binding.eTJadwalOrgEditKet.setText("")
                binding.llSpinnerJadwalOrgStatus.visibility = View.GONE
                binding.llJadwalOrgData.visibility = View.GONE
                binding.llJadwalOrgEdit.visibility = View.GONE
                binding.btnJadwalOrgEdit.visibility = View.GONE
                binding.btnJadwalOrgProses.visibility = View.GONE
            }
        }

        viewModel.responseEditSchedule.observe(this) { resp ->
            binding.btnJadwalOrgEdit.isClickable = true
            if (resp.error) {
                progressDialog.dismiss()
                this.showToast(resp.message)
            } else {
                viewModel.getSchedule()
                binding.tvJadwalOrgEditTgl.text = ""
                binding.eTJadwalOrgEditKet.setText("")
                binding.llSpinnerJadwalOrgStatus.visibility = View.GONE
                binding.llJadwalOrgData.visibility = View.GONE
                binding.llJadwalOrgEdit.visibility = View.GONE
                binding.btnJadwalOrgEdit.visibility = View.GONE
                binding.btnJadwalOrgProses.visibility = View.GONE
            }
        }

        viewModel.errorResponse.observe(this) { resp ->
            progressDialog.dismiss()
            binding.btnJadwalOrgProses.isClickable = true
            binding.btnJadwalOrgEdit.isClickable = true
            if (resp.isNotEmpty()) {
                this.showToast(resp)
            }
        }

        spinnerStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                status = when (selectedItem) {
                    "Akan Datang" -> {
                        "0"
                    }
                    "Berlangsung" -> {
                        "1"
                    }
                    "Selesai" -> {
                        "2"
                    }
                    else -> {
                        ""
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.tvJadwalOrgEditTgl.setOnClickListener {
            AppUtils.datePicker(this, binding.tvJadwalOrgEditTgl)
        }

        binding.btnJadwalOrgTambah.setOnClickListener {
            binding.tvJadwalOrgEditTgl.text = ""
            binding.eTJadwalOrgEditKet.setText("")
            binding.tvJadwalOrgEdit.text = getText(R.string.tambah_data)
            binding.llSpinnerJadwalOrgStatus.visibility = View.GONE
            binding.llJadwalOrgData.visibility = View.GONE
            binding.llJadwalOrgEdit.visibility = View.VISIBLE
            binding.btnJadwalOrgEdit.visibility = View.GONE
            binding.btnJadwalOrgProses.visibility = View.VISIBLE
        }

        binding.btnJadwalOrgBatal.setOnClickListener {
            binding.tvJadwalOrgEditTgl.text = ""
            binding.eTJadwalOrgEditKet.setText("")
            binding.llSpinnerJadwalOrgStatus.visibility = View.GONE
            binding.llJadwalOrgData.visibility = View.VISIBLE
            binding.llJadwalOrgEdit.visibility = View.GONE
            binding.btnJadwalOrgEdit.visibility = View.GONE
            binding.btnJadwalOrgProses.visibility = View.GONE
        }

        binding.btnJadwalOrgEdit.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(this)) {
                val tgl = binding.tvJadwalOrgEditTgl
                val ket = binding.eTJadwalOrgEditKet
                if (tgl.text.toString().trim().isEmpty()) {
                    it.isClickable = true
                    this.showToast("Tanggal belum dipilih!")
                } else if (ket.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(ket, it, "Field ini tidak boleh kosong")
                } else if (status == "") {
                    it.isClickable = true
                    this.showToast("Terjadi kesalahan!")
                } else if (idSchedule == "") {
                    it.isClickable = true
                    this.showToast("Terjadi kesalahan!")
                } else {
                    viewModel.editSchedule(tgl.text.toString(), ket.text.toString(), status, idSchedule)
                    progressDialog.show()
                }
            } else {
                it.isClickable = true
                this.showToast("Tidak ada koneksi internet")
            }
        }

        binding.btnJadwalOrgProses.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(this)) {
                val tgl = binding.tvJadwalOrgEditTgl
                val ket = binding.eTJadwalOrgEditKet
                if (tgl.text.toString().trim().isEmpty()) {
                    it.isClickable = true
                    this.showToast("Tanggal belum dipilih!")
                } else if (ket.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(ket, it, "Field ini tidak boleh kosong")
                } else {
                    viewModel.addSchedule(tgl.text.toString(), ket.text.toString())
                    progressDialog.show()
                }
            } else {
                it.isClickable = true
                this.showToast("Tidak ada koneksi internet")
            }
        }

        binding.ivJadwalOrgToolbarBack.setOnClickListener {
            finish()
        }

        if (AppUtils.isInternetAvailable(this)) {
            viewModel.getSchedule()
            progressDialog.show()
        } else {
            this.showToast("Tidak ada koneksi internet")
        }
    }

    private fun setSpinner() {
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listStatus)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStatus.adapter = adapter
    }

    private fun setuprecyclerview(data: ArrayList<SchedulePub>) {
        val rvSchedule = binding.rvJadwalOrg
        rvSchedule.layoutManager = LinearLayoutManager(this)
        val adapter = ScheduleOrgAdapter(data)
        adapter.setOnItemClickCallback(object : ScheduleOrgAdapter.OnItemClickCallback {
            override fun onItemClicked(data: SchedulePub) {
                idSchedule = data.id
                binding.tvJadwalOrgEditTgl.text = data.tgl
                binding.eTJadwalOrgEditKet.setText(data.ket)
                binding.tvJadwalOrgEdit.text = getText(R.string.ubah_data)

                listStatus = emptyList()
                listStatus = listOf("Akan Datang", "Berlangsung", "Selesai")
                setSpinner()
                status = data.status
                spinnerStatus.setSelection(Integer.parseInt(data.status))

                binding.llSpinnerJadwalOrgStatus.visibility = View.VISIBLE
                binding.llJadwalOrgData.visibility = View.GONE
                binding.llJadwalOrgEdit.visibility = View.VISIBLE
                binding.btnJadwalOrgEdit.visibility = View.VISIBLE
                binding.btnJadwalOrgProses.visibility = View.GONE
            }
        })
        rvSchedule.adapter = adapter
    }
}