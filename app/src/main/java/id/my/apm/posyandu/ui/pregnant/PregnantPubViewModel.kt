package id.my.apm.posyandu.ui.pregnant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.CheckHistory
import id.my.apm.posyandu.model.MedicationHistory
import id.my.apm.posyandu.model.MotherPub
import id.my.apm.posyandu.model.PregnantImmunization
import id.my.apm.posyandu.use_case.GetAllCheckHistoryUseCase
import id.my.apm.posyandu.use_case.GetAllMedicationHistoryUseCase
import id.my.apm.posyandu.use_case.pregnant.GetPregnantImmunizationUseCase
import id.my.apm.posyandu.use_case.pregnant.GetPregnantPubUseCase
import id.my.apm.posyandu.utils.SessionManager

class PregnantPubViewModel(
    private val getPregnantPubUseCase: GetPregnantPubUseCase,
    private val getAllCheckHistoryUseCase: GetAllCheckHistoryUseCase,
    private val getAllMedicationHistoryUseCase: GetAllMedicationHistoryUseCase,
    private val getPregnantImmunizationUseCase: GetPregnantImmunizationUseCase,
    sessionManager: SessionManager
) : ViewModel() {

    val user = sessionManager.getAuthData()?.id.toString()

    private val _responseGetPregnant = MutableLiveData<MotherPub>()
    val responseGetPregnant: LiveData<MotherPub> = _responseGetPregnant

    private val _responseGetPregnantCheck = MutableLiveData<ArrayList<CheckHistory>>()
    val responseGetPregnantCheck: LiveData<ArrayList<CheckHistory>> = _responseGetPregnantCheck

    private val _responseGetPregnantImmunization = MutableLiveData<ArrayList<PregnantImmunization>>()
    val responseGetPregnantImmunization: LiveData<ArrayList<PregnantImmunization>> = _responseGetPregnantImmunization

    private val _responseGetPregnantMedication = MutableLiveData<ArrayList<MedicationHistory>>()
    val responseGetPregnantMedication: LiveData<ArrayList<MedicationHistory>> = _responseGetPregnantMedication

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getPregnant() {
        getPregnantPubUseCase(user,
            onResult = { resp ->
                _responseGetPregnant.postValue(resp)
                //_errorResponse.postValue(false)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun getPregnantCheck(id: String) {
        getAllCheckHistoryUseCase("4", id,
            onResult = { resp ->
                _responseGetPregnantCheck.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun getPregnantImmunization(id: String) {
        getPregnantImmunizationUseCase(id,
            onResult = { resp ->
                _responseGetPregnantImmunization.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun getPregnantMedication(id: String) {
        getAllMedicationHistoryUseCase("4", id,
            onResult = { resp ->
                _responseGetPregnantMedication.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}