package id.my.apm.posyandu.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.AuthData
import id.my.apm.posyandu.model.RegistrationOrg
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.registration.ChangeStatusScheduleUseCase
import id.my.apm.posyandu.use_case.registration.GetRegistrationOrgUseCase
import id.my.apm.posyandu.use_case.registration.RegistrationOrgUseCase
import id.my.apm.posyandu.utils.SessionManager

class RegistrationOrgViewModel(
    private val registrationOrgUseCase: RegistrationOrgUseCase,
    private val changeStatusScheduleUseCase: ChangeStatusScheduleUseCase,
    private val getRegistrationOrgUseCase: GetRegistrationOrgUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _responseGet = MutableLiveData<AuthData>()
    val responseGet: LiveData<AuthData> = _responseGet

    private val _responseRegistrationOrg = MutableLiveData<RegistrationOrg>()
    val responseRegistrationOrg: LiveData<RegistrationOrg> = _responseRegistrationOrg

    private val _responseGetDataRegistration = MutableLiveData<ArrayList<RegistrationPub>>()
    val responseGetDataRegistration: LiveData<ArrayList<RegistrationPub>> = _responseGetDataRegistration

    private val _responseChangeStatus = MutableLiveData<WebResponse>()
    val responseChangeStatus: LiveData<WebResponse> = _responseChangeStatus

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getSession() {
        val auth = sessionManager.getAuthData()
        if (auth != null) {
            _responseGet.postValue(auth)
        } else {
            _errorResponse.postValue("GAGAL, silahkan ulangi!")
        }
    }

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

    fun getDataRegistration() {
        getRegistrationOrgUseCase(
            onResult = { resp ->
                _responseGetDataRegistration.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun changeStatus(id: String, status: String) {
        changeStatusScheduleUseCase(id, status,
            onResult = { resp ->
                _responseChangeStatus.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}