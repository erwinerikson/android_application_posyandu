package id.my.apm.posyandu.ui.medication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.DataImmunization
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.GetAllDataImmunizationUseCase
import id.my.apm.posyandu.use_case.SaveDataImmunizationUseCase

class ImmunizationViewModel(
    private val getAllDataImmunizationUseCase: GetAllDataImmunizationUseCase,
    private val saveDataImmunizationUseCase: SaveDataImmunizationUseCase
) : ViewModel() {

    private val _responseGetImmunization = MutableLiveData<ArrayList<DataImmunization>>()
    val responseGetImmunization: LiveData<ArrayList<DataImmunization>> = _responseGetImmunization

    private val _responseProcess = MutableLiveData<WebResponse>()
    val responseProcess: LiveData<WebResponse> = _responseProcess

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getImmunization() {
        getAllDataImmunizationUseCase(
            onResult = { resp ->
                _responseGetImmunization.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun process(nama: String, harga: String, kode: String, ket: String) {
        saveDataImmunizationUseCase(nama, harga, kode, ket,
            onResult = { resp ->
                _responseProcess.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}