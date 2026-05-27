package id.my.apm.posyandu.ui.pregnant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.RegistrationOrg
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.use_case.pregnant.GetPregnantOrgUseCase
import id.my.apm.posyandu.use_case.registration.RegistrationOrgUseCase

class PregnantOrgViewModel(
    private val registrationOrgUseCase: RegistrationOrgUseCase,
    private val getPregnantOrgUseCase: GetPregnantOrgUseCase,
) : ViewModel() {

    private val _responseRegistrationOrg = MutableLiveData<RegistrationOrg>()
    val responseRegistrationOrg: LiveData<RegistrationOrg> = _responseRegistrationOrg

    private val _responseGetDataPregnant = MutableLiveData<ArrayList<RegistrationPub>>()
    val responseGetDataPregnant: LiveData<ArrayList<RegistrationPub>> = _responseGetDataPregnant

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun checkRegistration() {
        registrationOrgUseCase(
            onResult = { resp ->
                _responseRegistrationOrg.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun getData() {
        getPregnantOrgUseCase(
            onResult = { resp ->
                _responseGetDataPregnant.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}