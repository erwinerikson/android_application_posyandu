package id.my.apm.posyandu.ui.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.DataMedication
import id.my.apm.posyandu.model.DataMidwife
import id.my.apm.posyandu.model.MedicationHistory
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.GetAllMedicationHistoryUseCase
import id.my.apm.posyandu.use_case.GetDataMedicationUseCase
import id.my.apm.posyandu.use_case.GetDataMidwifeUseCase
import id.my.apm.posyandu.use_case.SaveMedicationUseCase

class ActionMedicationViewModel(
    private val getAllMedicationHistoryUseCase: GetAllMedicationHistoryUseCase,
    private val getDataMedicationUseCase: GetDataMedicationUseCase,
    private val getDataMidwifeUseCase: GetDataMidwifeUseCase,
    private val saveMedicationUseCase: SaveMedicationUseCase,
) : ViewModel() {

    private val _responseGetMedication = MutableLiveData<ArrayList<MedicationHistory>>()
    val responseGetMedication: LiveData<ArrayList<MedicationHistory>> = _responseGetMedication

    private val _responseGetDataMedication = MutableLiveData<ArrayList<DataMedication>>()
    val responseGetDataMedication: LiveData<ArrayList<DataMedication>> = _responseGetDataMedication

    private val _responseGetDataMidwife = MutableLiveData<ArrayList<DataMidwife>>()
    val responseGetDataMidwife: LiveData<ArrayList<DataMidwife>> = _responseGetDataMidwife

    private val _responseProcess = MutableLiveData<WebResponse>()
    val responseProcess: LiveData<WebResponse> = _responseProcess

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getMedication(kode: String, id: String) {
        //val user = sessionManager.getAuthData()
        getAllMedicationHistoryUseCase(kode, id,
            onResult = { resp ->
                _responseGetMedication.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun getDataMedication() {
        getDataMedicationUseCase(
            onResult = { resp ->
                _responseGetDataMedication.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun getDataMidwife() {
        getDataMidwifeUseCase(
            onResult = { resp ->
                _responseGetDataMidwife.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun process(idUser: String, idBayi: String, idObat: String, qty: String, kode: String, idBidan: String, harga: String) {
        saveMedicationUseCase(idUser, idBayi, idObat, qty, kode, idBidan, harga,
            onResult = { resp ->
                _responseProcess.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}