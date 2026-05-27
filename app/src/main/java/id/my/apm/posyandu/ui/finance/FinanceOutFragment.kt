package id.my.apm.posyandu.ui.finance

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
import id.my.apm.posyandu.adapter.FinanceAdapter
import id.my.apm.posyandu.databinding.FragmentFinanceOutBinding
import id.my.apm.posyandu.model.DataFinance
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.repository.FinanceRepository
import id.my.apm.posyandu.use_case.SaveFinanceUseCase
import id.my.apm.posyandu.use_case.finance.GetFinanceUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class FinanceOutFragment : Fragment() {

    private lateinit var viewModel: FinanceViewModel
    private var _binding: FragmentFinanceOutBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var repository: FinanceRepository
    @Inject
    lateinit var allRepository: AllRepository
    private var listFinance = ArrayList<DataFinance>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFinanceOutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = Dialog(requireContext())
        AppUtils.progressDialog(progressDialog)

        val getFinanceUseCase = GetFinanceUseCase(repository)
        val saveFinanceUseCase = SaveFinanceUseCase(allRepository)
        viewModel = FinanceViewModel(getFinanceUseCase, saveFinanceUseCase)

        viewModel.responseGetFinance.observe(viewLifecycleOwner) { resp ->
            progressDialog.dismiss()
            listFinance.clear()
            if (resp.isNotEmpty()) {
                if (resp[0].error) {
                    this.context?.showToast(resp[0].message)
                } else {
                    listFinance = resp
                    val totalHarga = listFinance.sumOf { Integer.parseInt(it.nominal) }
                    binding.tvFinanceOutTotal.text = AppUtils.formatCurrencySymbol(totalHarga.toString())
                    setuprecyclerview(listFinance)
                    binding.llFinanceOutData.visibility = View.VISIBLE
                }
            } else {
                this.context?.showToast("Tidak ada data")
            }
            /*var total: Int = 0
            for (dt in listFinance) {
                total += Integer.parseInt(dt.nominal)
            }*/
        }

        viewModel.responseProcess.observe(viewLifecycleOwner) { resp ->
            binding.btnFinanceOutAddProses.isClickable = true
            this.context?.showToast(resp.message)
            if (!resp.error) {
                binding.llFinanceOutData.visibility = View.GONE
                binding.llFinanceOutDetail.visibility = View.GONE
                binding.llFinanceOutAdd.visibility = View.GONE
                viewModel.getFinance("D")
            } else {
                progressDialog.dismiss()
            }
            this.context?.showToast(resp.message)
        }

        viewModel.errorResponse.observe(viewLifecycleOwner) { resp ->
            binding.btnFinanceOutAddProses.isClickable = true
            progressDialog.dismiss()
            this.context?.showToast(resp)
        }

        binding.btnFinanceOutAdd.setOnClickListener {
            binding.llFinanceOutData.visibility = View.GONE
            binding.llFinanceOutDetail.visibility = View.GONE
            binding.llFinanceOutAdd.visibility = View.VISIBLE
        }

        binding.btnFinanceOutDetailBack.setOnClickListener {
            binding.llFinanceOutData.visibility = View.VISIBLE
            binding.llFinanceOutDetail.visibility = View.GONE
            binding.llFinanceOutAdd.visibility = View.GONE
        }

        binding.btnFinanceOutAddBatal.setOnClickListener {
            binding.llFinanceOutData.visibility = View.VISIBLE
            binding.llFinanceOutDetail.visibility = View.GONE
            binding.llFinanceOutAdd.visibility = View.GONE
        }

        binding.btnFinanceOutAddProses.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(requireContext())) {
                val desc = binding.eTAddFinanceOutDesc
                val sumber = binding.eTAddFinanceOutSumber
                val nominal = binding.eTAddFinanceOutNominal
                if (desc.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(desc, it, "Field ini tidak boleh kosong")
                } else if (sumber.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(sumber, it, "Field ini tidak boleh kosong")
                } else if (nominal.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(nominal, it, "Field ini tidak boleh kosong")
                } else {
                    progressDialog.show()
                    viewModel.process("D", "0", desc.text.toString(), sumber.text.toString(), nominal.text.toString())
                }
            } else {
                it.isClickable = true
                requireContext().showToast("Tidak ada koneksi internet")
            }
        }

        val swipeRefresh = binding.financeOutSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llFinanceOut.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llFinanceOut.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(requireContext())) {
                    viewModel.getFinance("D")
                    progressDialog.show()
                } else {
                    requireContext().showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        if (AppUtils.isInternetAvailable(requireContext())) {
            viewModel.getFinance("D")
            progressDialog.show()
        } else {
            requireContext().showToast("Tidak ada koneksi internet")
        }
    }

    private fun setuprecyclerview(data: ArrayList<DataFinance>) {
        val rvTableFinance = binding.rvFinanceOut
        rvTableFinance.layoutManager = LinearLayoutManager(this.context)
        val adapter = FinanceAdapter(data)
        adapter.setOnItemClickCallback(object : FinanceAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataFinance) {
                binding.tvFinanceOutDetailTanggal.text = AppUtils.formatterDateLetter(data.tgl)
                binding.tvFinanceOutDetailDesc.text = data.desc
                binding.tvFinanceOutDetailNominal.text = AppUtils.formatCurrencySymbol(data.nominal)
                binding.tvFinanceOutDetailSumber.text = data.sumber
                binding.tvFinanceOutDetailPemroses.text = data.pemroses
                binding.llFinanceOutData.visibility = View.GONE
                binding.llFinanceOutDetail.visibility = View.VISIBLE
            }
        })
        rvTableFinance.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}