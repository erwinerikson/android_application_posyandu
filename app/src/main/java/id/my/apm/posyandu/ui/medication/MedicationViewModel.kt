package id.my.apm.posyandu.ui.medication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.DataMedication
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.GetDataMedicationUseCase
import id.my.apm.posyandu.use_case.SaveDataMedicationUseCase

class MedicationViewModel(
    private val getDataMedicationUseCase: GetDataMedicationUseCase,
    private val saveDataMedicationUseCase: SaveDataMedicationUseCase
) : ViewModel() {

    private val _responseGetMedication = MutableLiveData<ArrayList<DataMedication>>()
    val responseGetMedication: LiveData<ArrayList<DataMedication>> = _responseGetMedication

    private val _responseProcess = MutableLiveData<WebResponse>()
    val responseProcess: LiveData<WebResponse> = _responseProcess

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getMedication() {
        getDataMedicationUseCase(
            onResult = { resp ->
                _responseGetMedication.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun process(nama: String, satuan: String, harga: String, stok: String) {
        saveDataMedicationUseCase(nama, satuan, harga, stok,
            onResult = { resp ->
                _responseProcess.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}