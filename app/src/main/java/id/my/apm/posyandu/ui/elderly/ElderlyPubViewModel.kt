package id.my.apm.posyandu.ui.elderly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.CheckHistory
import id.my.apm.posyandu.model.ElderlyPub
import id.my.apm.posyandu.model.MedicationHistory
import id.my.apm.posyandu.use_case.GetAllCheckHistoryUseCase
import id.my.apm.posyandu.use_case.GetAllMedicationHistoryUseCase
import id.my.apm.posyandu.use_case.elderly.GetElderlyDataUseCase
import id.my.apm.posyandu.use_case.elderly.GetElderlyPubUseCase
import id.my.apm.posyandu.utils.SessionManager

class ElderlyPubViewModel(
    private val getElderlyPubUseCase: GetElderlyPubUseCase,
    private val getElderlyDataUseCase: GetElderlyDataUseCase,
    private val getAllCheckHistoryUseCase: GetAllCheckHistoryUseCase,
    private val getAllMedicationHistoryUseCase: GetAllMedicationHistoryUseCase,
    sessionManager: SessionManager
) : ViewModel() {

    private val user = sessionManager.getAuthData()?.id.toString()

    private val _responseGetElderly = MutableLiveData<ElderlyPub>()
    val responseGetElderly: LiveData<ElderlyPub> = _responseGetElderly

    private val _responseGetElderlyData = MutableLiveData<ArrayList<ElderlyPub>>()
    val responseGetElderlyData: LiveData<ArrayList<ElderlyPub>> = _responseGetElderlyData

    private val _responseGetElderlyCheck = MutableLiveData<ArrayList<CheckHistory>>()
    val responseGetElderlyCheck: LiveData<ArrayList<CheckHistory>> = _responseGetElderlyCheck

    private val _responseGetElderlyMedication = MutableLiveData<ArrayList<MedicationHistory>>()
    val responseGetElderlyMedication: LiveData<ArrayList<MedicationHistory>> = _responseGetElderlyMedication

    private val _errorResponse = MutableLiveData<Boolean>()
    val errorResponse: LiveData<Boolean> = _errorResponse

    fun getElderly(id: String) {
        getElderlyPubUseCase(id,
            onResult = { resp ->
                _responseGetElderly.postValue(resp)
                _errorResponse.postValue(false)
            },
            onError = {
                _errorResponse.postValue(true)
            }
        )
    }

    fun getElderlyData() {
        getElderlyDataUseCase(user,
            onResult = { resp ->
                _responseGetElderlyData.postValue(resp)
                _errorResponse.postValue(false)
            },
            onError = {
                _errorResponse.postValue(true)
            }
        )
    }

    fun getElderlyCheck(id: String) {
        getAllCheckHistoryUseCase("3", id,
            onResult = { resp ->
                _responseGetElderlyCheck.postValue(resp)
                _errorResponse.postValue(false)
            },
            onError = {
                _errorResponse.postValue(true)
            }
        )
    }

    fun getElderlyMedication(id: String) {
        //val user = sessionManager.getAuthData()
        getAllMedicationHistoryUseCase("3", id,
            onResult = { resp ->
                _responseGetElderlyMedication.postValue(resp)
                _errorResponse.postValue(false)
            },
            onError = {
                _errorResponse.postValue(true)
            }
        )
    }
}