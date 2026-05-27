package id.my.apm.posyandu.ui.elderly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.RegistrationOrg
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.use_case.elderly.GetElderlyOrgUseCase
import id.my.apm.posyandu.use_case.registration.RegistrationOrgUseCase

class ElderlyOrgViewModel(
    private val registrationOrgUseCase: RegistrationOrgUseCase,
    private val getElderlyOrgUseCase: GetElderlyOrgUseCase,
) : ViewModel() {

    private val _responseRegistrationOrg = MutableLiveData<RegistrationOrg>()
    val responseRegistrationOrg: LiveData<RegistrationOrg> = _responseRegistrationOrg

    private val _responseGetDataElderly = MutableLiveData<ArrayList<RegistrationPub>>()
    val responseGetDataElderly: LiveData<ArrayList<RegistrationPub>> = _responseGetDataElderly

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
        getElderlyOrgUseCase(
            onResult = { resp ->
                _responseGetDataElderly.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}