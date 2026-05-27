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
import id.my.apm.posyandu.adapter.DataBidanAdapter
import id.my.apm.posyandu.databinding.FragmentMidwifeBinding
import id.my.apm.posyandu.model.DataMidwife
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.use_case.GetDataMidwifeUseCase
import id.my.apm.posyandu.use_case.SaveDataMidwifeUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class MidwifeFragment : Fragment() {

    private lateinit var viewModel: MidwifeViewModel
    private var _binding: FragmentMidwifeBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var allRepository: AllRepository
    private var listMidwife = ArrayList<DataMidwife>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMidwifeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = Dialog(requireContext())
        AppUtils.progressDialog(progressDialog)

        val getDataMidwifeUseCase = GetDataMidwifeUseCase(allRepository)
        val saveDataMidwifeUseCase = SaveDataMidwifeUseCase(allRepository)
        viewModel = MidwifeViewModel(getDataMidwifeUseCase, saveDataMidwifeUseCase)

        viewModel.responseGetMidwife.observe(viewLifecycleOwner) { resp ->
            progressDialog.dismiss()
            listMidwife.clear()
            if (resp.isNotEmpty()) {
                if (resp[0].error) {
                    this.context?.showToast(resp[0].message)
                } else {
                    listMidwife = resp
                    binding.tvMidwifeJumlah.text = resp.size.toString()
                    setuprecyclerview(listMidwife)
                    binding.llMidwife.visibility = View.VISIBLE
                    binding.llMidwifeData.visibility = View.VISIBLE
                }
            } else {
                this.context?.showToast("Tidak ada data")
            }
        }

        viewModel.responseProcess.observe(viewLifecycleOwner) { resp ->
            binding.btnMidwifeAddProses.isClickable = true
            this.context?.showToast(resp.message)
            if (!resp.error) {
                binding.llMidwifeData.visibility = View.GONE
                binding.llMidwifeAdd.visibility = View.GONE
                viewModel.getMidwife()
            } else {
                progressDialog.dismiss()
            }
            this.context?.showToast(resp.message)
        }

        viewModel.errorResponse.observe(viewLifecycleOwner) { resp ->
            binding.btnMidwifeAddProses.isClickable = true
            progressDialog.dismiss()
            this.context?.showToast(resp)
        }

        binding.btnMidwifeAdd.setOnClickListener {
            binding.llMidwifeData.visibility = View.GONE
            binding.llMidwifeAdd.visibility = View.VISIBLE
        }

        binding.btnMidwifeAddBatal.setOnClickListener {
            binding.llMidwifeData.visibility = View.VISIBLE
            binding.llMidwifeAdd.visibility = View.GONE
        }

        binding.btnMidwifeAddProses.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(requireContext())) {
                val nama = binding.eTAddMidwifeNama
                if (nama.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(nama, it, "Field ini tidak boleh kosong")
                } else {
                    viewModel.process(nama.text.toString())
                    progressDialog.show()
                }
            } else {
                it.isClickable = true
                requireContext().showToast("Tidak ada koneksi internet")
            }
        }

        val swipeRefresh = binding.midwifeSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llMidwife.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llMidwife.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(requireContext())) {
                    viewModel.getMidwife()
                    progressDialog.show()
                } else {
                    requireContext().showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        if (AppUtils.isInternetAvailable(requireContext())) {
            viewModel.getMidwife()
            progressDialog.show()
        } else {
            requireContext().showToast("Tidak ada koneksi internet")
        }
    }

    private fun setuprecyclerview(data: ArrayList<DataMidwife>) {
        val rvTableMidwife = binding.rvListMidwife
        rvTableMidwife.layoutManager = LinearLayoutManager(this.context)
        val adapter = DataBidanAdapter(data)
        rvTableMidwife.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}