package id.my.apm.posyandu.ui.mother

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.RegistrationOrg
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.use_case.mother.GetMotherOrgUseCase
import id.my.apm.posyandu.use_case.registration.RegistrationOrgUseCase

class MotherOrgViewModel(
    private val registrationOrgUseCase: RegistrationOrgUseCase,
    private val getMotherOrgUseCase: GetMotherOrgUseCase,
) : ViewModel() {

    private val _responseRegistrationOrg = MutableLiveData<RegistrationOrg>()
    val responseRegistrationOrg: LiveData<RegistrationOrg> = _responseRegistrationOrg

    private val _responseGetDataMother = MutableLiveData<ArrayList<RegistrationPub>>()
    val responseGetDataMother: LiveData<ArrayList<RegistrationPub>> = _responseGetDataMother

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
        getMotherOrgUseCase(
            onResult = { resp ->
                _responseGetDataMother.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}