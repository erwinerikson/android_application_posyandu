package id.my.apm.posyandu.ui.medication

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.my.apm.posyandu.R
import id.my.apm.posyandu.adapter.DataImunisasiAdapter
import id.my.apm.posyandu.databinding.FragmentImmunizationBinding
import id.my.apm.posyandu.model.DataImmunization
import id.my.apm.posyandu.repository.AllRepository
import id.my.apm.posyandu.use_case.GetAllDataImmunizationUseCase
import id.my.apm.posyandu.use_case.SaveDataImmunizationUseCase
import id.my.apm.posyandu.utils.AppUtils
import id.my.apm.posyandu.utils.AppUtils.showToast
import javax.inject.Inject

@AndroidEntryPoint
class ImmunizationFragment : Fragment() {

    private lateinit var viewModel: ImmunizationViewModel
    private var _binding: FragmentImmunizationBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var allRepository: AllRepository
    private lateinit var spinnerKode: Spinner
    private var listImmunization = ArrayList<DataImmunization>()
    private lateinit var listNameKode: List<String>
    private var kode = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentImmunizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressDialog = Dialog(requireContext())
        AppUtils.progressDialog(progressDialog)

        val getAllDataImmunizationUseCase = GetAllDataImmunizationUseCase(allRepository)
        val saveDataImmunizationUseCase = SaveDataImmunizationUseCase(allRepository)
        viewModel = ImmunizationViewModel(getAllDataImmunizationUseCase, saveDataImmunizationUseCase)

        spinnerKode = binding.spinnerAddImmunizationKode

        viewModel.responseGetImmunization.observe(viewLifecycleOwner) { resp ->
            progressDialog.dismiss()
            listImmunization.clear()
            if (resp.isNotEmpty()) {
                if (resp[0].error) {
                    this.context?.showToast(resp[0].message)
                } else {
                    listImmunization = resp
                    binding.tvImmunizationJumlah.text = resp.size.toString()
                    setuprecyclerview(listImmunization)
                    binding.llImmunization.visibility = View.VISIBLE
                    binding.llImmunizationData.visibility = View.VISIBLE
                }
            } else {
                this.context?.showToast("Tidak ada data")
            }
        }

        viewModel.responseProcess.observe(viewLifecycleOwner) { resp ->
            binding.btnImmunizationAddProses.isClickable = true
            this.context?.showToast(resp.message)
            if (!resp.error) {
                binding.llImmunizationData.visibility = View.GONE
                binding.llImmunizationDetail.visibility = View.GONE
                binding.llImmunizationAdd.visibility = View.GONE
                viewModel.getImmunization()
            } else {
                progressDialog.dismiss()
            }
            this.context?.showToast(resp.message)
        }

        viewModel.errorResponse.observe(viewLifecycleOwner) { resp ->
            binding.btnImmunizationAddProses.isClickable = true
            progressDialog.dismiss()
            this.context?.showToast(resp)
        }

        spinnerKode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()

                kode = when (selectedItem) {
                    "Silahkan pilih Periksa" -> {
                        ""
                    }
                    "Bayi" -> {
                        "1"
                    }
                    "Ibu Hamil" -> {
                        "1"
                    }
                    else -> {
                        ""
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.btnImmunizationAdd.setOnClickListener {
            kode = ""
            listNameKode = emptyList()
            listNameKode = listOf("Silahkan pilih", "Bayi", "Ibu Hamil")
            setSpinnerKode()
            binding.llImmunizationData.visibility = View.GONE
            binding.llImmunizationDetail.visibility = View.GONE
            binding.llImmunizationAdd.visibility = View.VISIBLE
        }

        binding.btnImmunizationDetailBack.setOnClickListener {
            binding.llImmunizationData.visibility = View.VISIBLE
            binding.llImmunizationDetail.visibility = View.GONE
            binding.llImmunizationAdd.visibility = View.GONE
        }

        binding.btnImmunizationAddBatal.setOnClickListener {
            kode = ""
            listNameKode = emptyList()
            binding.llImmunizationData.visibility = View.VISIBLE
            binding.llImmunizationDetail.visibility = View.GONE
            binding.llImmunizationAdd.visibility = View.GONE
        }

        binding.btnImmunizationAddProses.setOnClickListener {
            it.isClickable = false
            if (AppUtils.isInternetAvailable(requireContext())) {
                val nama = binding.eTAddImmunizationNama
                val harga = binding.eTAddImmunizationHarga
                val ket = binding.eTAddImmunizationKet
                if (nama.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(nama, it, "Field ini tidak boleh kosong")
                } else if (harga.text.toString().trim().isEmpty()) {
                    AppUtils.checkEt(harga, it, "Field ini tidak boleh kosong")
                } else if (kode.isEmpty()) {
                    requireContext().showToast("Kode belum dipilih!")
                    it.isClickable = true
                } else {
                    viewModel.process(nama.text.toString(), harga.text.toString(), kode, ket.text.toString())
                    progressDialog.show()
                }
            } else {
                it.isClickable = true
                requireContext().showToast("Tidak ada koneksi internet")
            }
        }

        val swipeRefresh = binding.immunizationSwipeRefresh
        swipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            binding.llImmunization.visibility = View.GONE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.llImmunization.visibility = View.VISIBLE
                swipeRefresh.isRefreshing = false
                if (AppUtils.isInternetAvailable(requireContext())) {
                    viewModel.getImmunization()
                    progressDialog.show()
                    requireContext().showToast("Fragment 2")
                } else {
                    requireContext().showToast("Tidak ada koneksi internet")
                }
            }, 1500)
        }

        if (AppUtils.isInternetAvailable(requireContext())) {
            viewModel.getImmunization()
            progressDialog.show()
        } else {
            requireContext().showToast("Tidak ada koneksi internet")
        }
    }

    private fun setSpinnerKode() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listNameKode)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerKode.adapter = adapter
    }

    private fun setuprecyclerview(data: ArrayList<DataImmunization>) {
        val rvTableImmunization = binding.rvListImmunization
        rvTableImmunization.layoutManager = LinearLayoutManager(this.context)
        val adapter = DataImunisasiAdapter(data)
        adapter.setOnItemClickCallback(object : DataImunisasiAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataImmunization) {
                binding.tvImmunizationDetailNama.text = data.nama
                binding.tvImmunizationDetailKode.text = if (data.kode == "1") "Bayi" else "Ibu Hamil"
                binding.tvImmunizationDetailHarga.text = AppUtils.formatCurrencySymbol(data.harga)
                binding.tvImmunizationDetailKet.text = data.ket.ifEmpty { "(tidak ada keterangan)" }
                binding.llImmunizationData.visibility = View.GONE
                binding.llImmunizationDetail.visibility = View.VISIBLE
            }
        })
        rvTableImmunization.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}