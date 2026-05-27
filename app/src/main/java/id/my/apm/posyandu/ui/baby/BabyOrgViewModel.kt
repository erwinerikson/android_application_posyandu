package id.my.apm.posyandu.ui.baby

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.RegistrationOrg
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.use_case.baby.GetBabyOrgUseCase
import id.my.apm.posyandu.use_case.registration.RegistrationOrgUseCase

class BabyOrgViewModel(
    private val registrationOrgUseCase: RegistrationOrgUseCase,
    private val getBabyOrgUseCase: GetBabyOrgUseCase,
) : ViewModel() {

    private val _responseRegistrationOrg = MutableLiveData<RegistrationOrg>()
    val responseRegistrationOrg: LiveData<RegistrationOrg> = _responseRegistrationOrg

    private val _responseGetDataBaby = MutableLiveData<ArrayList<RegistrationPub>>()
    val responseGetDataBaby: LiveData<ArrayList<RegistrationPub>> = _responseGetDataBaby

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
        getBabyOrgUseCase(
            onResult = { resp ->
                _responseGetDataBaby.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}