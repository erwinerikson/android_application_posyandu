package id.my.apm.posyandu.ui.medication

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.adapter.DataObatAdapter
import id.my.apm.posyandu.databinding.FragmentMedicationBinding
import id.my.apm.posyandu.model.DataMedication
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.use_case.GetDataMedicationUseCase
import id.my.apm.posyandu.use_case.SaveDataMedicationUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class MedicationFragment : Fragment() {

    private lateinit var viewModel: MedicationViewModel
    private var _binding: FragmentMedicationBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var allRepository: AllRepository
    private var listMedication = ArrayList<DataMedication>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMedicationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = Dialog(requireContext())
        AppUtils.progressDialog(progressDialog)

        val getDataMedicationUseCase = GetDataMedicationUseCase(allRepository)
        val saveDataMedicationUseCase = SaveDataMedicationUseCase(allRepository)
        viewModel = MedicationViewModel(getDataMedicationUseCase, saveDataMedicationUseCase)

        viewModel.responseGetMedication.observe(viewLifecycleOwner) { resp ->
            progressDialog.dismiss()
            listMedication.clear()
            if (resp.isNotEmpty()) {
                if (resp[0].error) {
                    this.context?.showToast(resp[0].message)
                } else {
                    listMedication = resp
                    binding.tvMedicationJumlah.text = resp.size.toString()
                    setuprecyclerview(listMedication)
                    binding.llMedication.visibility = View.VISIBLE
                    binding.llMedicationData.visibility = View.VISIBLE
                }
            } else {
                this.context?.showToast("Tidak ada data")
            }
        }

        viewModel.responseProcess.observe(viewLifecycleOwner) { resp ->
            binding.btnMedicationAddProses.isClickable = true
            this.context?.showToast(resp.message)
            if (!resp.error) {
                binding.llMedicationData.visibility = View.GONE
                binding.llMedicationDetail.visibility = View.GONE
                binding.llMedicationAdd.visibility = View.GONE
                viewModel.getMedication()
            } else {
                progressDialog.dismiss()
            }
            this.context?.showToast(resp.message)
        }

        viewModel.errorResponse.observe(viewLifecycleOwner) { resp ->
            binding.btnMedicationAddProses.isClickable = true
            progressDialog.dismiss()
            this.context?.showToast(resp)
        }

        binding.btnMedicationAdd.setOnClickListener {
            binding.llMedicationData.visibility = View.GONE
            binding.llMedicationDetail.visibility = View.GONE
            binding.llMedicationAdd.visibility = View.VISIBLE
        }

        binding.btnMedicationDetailBack.setOnClickListener {
            binding.llMedicationData.visibility = View.VISIBLE
            binding.llMedicationDetail.visibility = View.GONE
            binding.llMedicationAdd.visibility = View.GONE
        }

        binding.btnMedicationAddBatal.setOnClickListener {
            binding.llMedicationData.visibility = View.VISIBLE
            binding.llMedicationDetail.visibility = View.GONE
            binding.llMedicationAdd.visibility = View.GONE
        }

        binding.btnMedicationAddProses.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(requireContext())) {
                val nama = binding.eTAddMedicationNama
                val satuan = binding.eTAddMedicationSatuan
                val harga = binding.eTAddMedicationHarga
                val stok = binding.eTAddMedicationBanyak
                if (nama.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(nama, it, "Field ini tidak boleh kosong")
                } else if (satuan.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(satuan, it, "Field ini tidak boleh kosong")
                } else if (harga.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(harga, it, "Field ini tidak boleh kosong")
                } else if (stok.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(stok, it, "Field ini tidak boleh kosong")
                } else {
                    viewModel.process(nama.text.toString(), satuan.text.toString(), harga.text.toString(), stok.text.toString())
                    progressDialog.show()
                }
            } else {
                it.isClickable = true
                requireContext().showToast("Tidak ada koneksi internet")
            }
        }

        val swipeRefresh = binding.medicationSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llMedication.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llMedication.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(requireContext())) {
                    viewModel.getMedication()
                    progressDialog.show()
                } else {
                    requireContext().showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        if (AppUtils.isInternetAvailable(requireContext())) {
            viewModel.getMedication()
            progressDialog.show()
        } else {
            requireContext().showToast("Tidak ada koneksi internet")
        }
    }

    private fun setuprecyclerview(data: ArrayList<DataMedication>) {
        val rvTableMedication = binding.rvListMedication
        rvTableMedication.layoutManager = LinearLayoutManager(this.context)
        val adapter = DataObatAdapter(data)
        adapter.setOnItemClickCallback(object : DataObatAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataMedication) {
                binding.tvMedicationDetailNama.text = data.nama
                binding.tvMedicationDetailSatuan.text = data.satuan
                binding.tvMedicationDetailHarga.text = AppUtils.formatCurrencySymbol(data.harga)
                binding.tvMedicationDetailStok.text = data.stok
                binding.llMedicationData.visibility = View.GONE
                binding.llMedicationDetail.visibility = View.VISIBLE
            }
        })
        rvTableMedication.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}