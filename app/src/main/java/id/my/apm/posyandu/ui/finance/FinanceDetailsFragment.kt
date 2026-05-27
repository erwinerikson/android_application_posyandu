package id.my.apm.posyandu.ui.finance

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.databinding.FragmentFinanceDetailsBinding
import id.my.apm.posyandu.repository.FinanceRepository
import id.my.apm.posyandu.use_case.finance.GetDetailsFinanceUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class FinanceDetailsFragment : Fragment() {

    private lateinit var viewModel: FinanceDetailsViewModel
    private var _binding: FragmentFinanceDetailsBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var repository: FinanceRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFinanceDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = Dialog(requireContext())
        AppUtils.progressDialog(progressDialog)

        val getDetailsFinanceUseCase = GetDetailsFinanceUseCase(repository)
        viewModel = FinanceDetailsViewModel(getDetailsFinanceUseCase)

        viewModel.responseGetDetailsFinance.observe(viewLifecycleOwner) { resp ->
            progressDialog.dismiss()
            if (!resp.error) {
                binding.tvRincianKeuanganObat.text = AppUtils.formatCurrency(resp.medication)
                binding.tvRincianKeuanganImunisasi.text = AppUtils.formatCurrency(resp.immunization)
                binding.tvRincianKeuanganPeriksa.text = AppUtils.formatCurrency(resp.check)

                binding.tvRincianKeuanganInputMasuk.text = AppUtils.formatCurrency(resp.processIn)
                binding.tvRincianKeuanganInputKeluar.text = AppUtils.formatCurrency(resp.processOut)
                binding.tvRincianKeuanganTotalMasuk.text = AppUtils.formatCurrency(resp.totalIn)
                binding.tvRincianKeuanganTotalKeluar.text = AppUtils.formatCurrency(resp.totalOut)
            }
        }

        viewModel.errorResponse.observe(viewLifecycleOwner) { resp ->
            progressDialog.dismiss()
            this.context?.showToast(resp)
        }

        if (AppUtils.isInternetAvailable(requireContext())) {
            viewModel.getDetailsFinance()
            progressDialog.show()
        } else {
            requireContext().showToast("Tidak ada koneksi internet")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}