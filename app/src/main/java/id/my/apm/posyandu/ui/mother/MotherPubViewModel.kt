package id.my.apm.posyandu.ui.mother

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.CheckHistory
import id.my.apm.posyandu.model.MedicationHistory
import id.my.apm.posyandu.model.MotherCheck
import id.my.apm.posyandu.model.MotherPub
import id.my.apm.posyandu.use_case.GetAllMedicationHistoryUseCase
import id.my.apm.posyandu.use_case.GetAllCheckHistoryUseCase
import id.my.apm.posyandu.use_case.mother.GetMotherCheckHistoryUseCase
import id.my.apm.posyandu.use_case.mother.GetMotherPubUseCase
import id.my.apm.posyandu.utils.SessionManager

class MotherPubViewModel(
    private val getMotherPubUseCase: GetMotherPubUseCase,
    private val getAllCheckHistoryUseCase: GetAllCheckHistoryUseCase,
    private val getAllMedicationHistoryUseCase: GetAllMedicationHistoryUseCase,
    sessionManager: SessionManager
) : ViewModel() {

    private val user = sessionManager.getAuthData()?.id.toString()

    private val _responseGetMother = MutableLiveData<MotherPub>()
    val responseGetMother: LiveData<MotherPub> = _responseGetMother

    private val _responseGetMotherCheck = MutableLiveData<ArrayList<CheckHistory>>()
    val responseGetMotherCheck: LiveData<ArrayList<CheckHistory>> = _responseGetMotherCheck

    private val _responseGetMotherMedication = MutableLiveData<ArrayList<MedicationHistory>>()
    val responseGetMotherMedication: LiveData<ArrayList<MedicationHistory>> = _responseGetMotherMedication

    private val _errorResponse = MutableLiveData<Boolean>()
    val errorResponse: LiveData<Boolean> = _errorResponse

    fun getMother() {
        getMotherPubUseCase(user,
            onResult = { resp ->
                _responseGetMother.postValue(resp)
                _errorResponse.postValue(false)
            },
            onError = {
                _errorResponse.postValue(true)
            }
        )
    }

    fun getMotherCheck(id: String) {
        getAllCheckHistoryUseCase("2", id,
            onResult = { resp ->
                _responseGetMotherCheck.postValue(resp)
                _errorResponse.postValue(false)
            },
            onError = {
                _errorResponse.postValue(true)
            }
        )
    }

    fun getMotherMedication(id: String) {
        //val user = sessionManager.getAuthData()
        getAllMedicationHistoryUseCase("2", id,
            onResult = { resp ->
                _responseGetMotherMedication.postValue(resp)
                _errorResponse.postValue(false)
            },
            onError = {
                _errorResponse.postValue(true)
            }
        )
    }
}