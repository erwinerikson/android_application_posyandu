package id.my.apm.posyandu.ui.medication

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.adapter.DataPeriksaAdapter
import id.my.apm.posyandu.databinding.FragmentCheckBinding
import id.my.apm.posyandu.model.DataCheck
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.use_case.GetDataCheckUseCase
import id.my.apm.posyandu.use_case.SaveDataCheckUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class CheckFragment : Fragment() {

    private lateinit var viewModel: CheckViewModel
    private var _binding: FragmentCheckBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var allRepository: AllRepository
    private var listCheck = ArrayList<DataCheck>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCheckBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = Dialog(requireContext())
        AppUtils.progressDialog(progressDialog)

        val getDataCheckUseCase = GetDataCheckUseCase(allRepository)
        val saveDataCheckUseCase = SaveDataCheckUseCase(allRepository)
        viewModel = CheckViewModel(getDataCheckUseCase, saveDataCheckUseCase)

        viewModel.responseGetCheck.observe(viewLifecycleOwner) { resp ->
            progressDialog.dismiss()
            listCheck.clear()
            if (resp.isNotEmpty()) {
                if (resp[0].error) {
                    this.context?.showToast(resp[0].message)
                } else {
                    listCheck = resp
                    binding.tvCheckJumlah.text = resp.size.toString()
                    setuprecyclerview(listCheck)
                    binding.llCheck.visibility = View.VISIBLE
                    binding.llCheckData.visibility = View.VISIBLE
                }
            } else {
                this.context?.showToast("Tidak ada data")
            }
        }

        viewModel.responseProcess.observe(viewLifecycleOwner) { resp ->
            binding.btnCheckAddProses.isClickable = true
            this.context?.showToast(resp.message)
            if (!resp.error) {
                binding.llCheckData.visibility = View.GONE
                binding.llCheckDetail.visibility = View.GONE
                binding.llCheckAdd.visibility = View.GONE
                viewModel.getCheck()
            } else {
                progressDialog.dismiss()
            }
            this.context?.showToast(resp.message)
        }

        viewModel.errorResponse.observe(viewLifecycleOwner) { resp ->
            binding.btnCheckAddProses.isClickable = true
            progressDialog.dismiss()
            this.context?.showToast(resp)
        }

        binding.btnCheckAdd.setOnClickListener {
            binding.llCheckData.visibility = View.GONE
            binding.llCheckDetail.visibility = View.GONE
            binding.llCheckAdd.visibility = View.VISIBLE
        }

        binding.btnCheckDetailBack.setOnClickListener {
            binding.llCheckData.visibility = View.VISIBLE
            binding.llCheckDetail.visibility = View.GONE
            binding.llCheckAdd.visibility = View.GONE
        }

        binding.btnCheckAddBatal.setOnClickListener {
            binding.llCheckData.visibility = View.VISIBLE
            binding.llCheckDetail.visibility = View.GONE
            binding.llCheckAdd.visibility = View.GONE
        }

        binding.btnCheckAddProses.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(requireContext())) {
                val nama = binding.eTAddCheckNama
                val harga = binding.eTAddCheckHarga
                if (nama.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(nama, it, "Field ini tidak boleh kosong")
                } else if (harga.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(harga, it, "Field ini tidak boleh kosong")
                } else {
                    viewModel.process(nama.text.toString(), harga.text.toString())
                    progressDialog.show()
                }
            } else {
                it.isClickable = true
                requireContext().showToast("Tidak ada koneksi internet")
            }
        }

        val swipeRefresh = binding.checkSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llCheck.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llCheck.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(requireContext())) {
                    viewModel.getCheck()
                    progressDialog.show()
                } else {
                    requireContext().showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        if (AppUtils.isInternetAvailable(requireContext())) {
            viewModel.getCheck()
            progressDialog.show()
        } else {
            requireContext().showToast("Tidak ada koneksi internet")
        }
    }

    private fun setuprecyclerview(data: ArrayList<DataCheck>) {
        val rvTableCheck = binding.rvListCheck
        rvTableCheck.layoutManager = LinearLayoutManager(this.context)
        val adapter = DataPeriksaAdapter(data)
        adapter.setOnItemClickCallback(object : DataPeriksaAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataCheck) {
                binding.tvCheckDetailNama.text = data.nama
                binding.tvCheckDetailHarga.text = AppUtils.formatCurrencySymbol(data.harga)
                binding.llCheckData.visibility = View.GONE
                binding.llCheckDetail.visibility = View.VISIBLE
            }
        })
        rvTableCheck.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}