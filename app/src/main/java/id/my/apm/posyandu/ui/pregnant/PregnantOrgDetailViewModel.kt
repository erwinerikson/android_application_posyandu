package id.my.apm.posyandu.ui.pregnant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.MotherPub
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.pregnant.GetPregnantPubUseCase
import id.my.apm.posyandu.use_case.registration.ChangeStatusRegistrationUseCase

class PregnantOrgDetailViewModel(
    private val getPregnantPubUseCase: GetPregnantPubUseCase,
    private val changeStatusRegistrationUseCase: ChangeStatusRegistrationUseCase,
) : ViewModel() {

    private val _responseGetPregnant = MutableLiveData<MotherPub>()
    val responseGetPregnant: LiveData<MotherPub> = _responseGetPregnant

    private val _responseChangeStatus = MutableLiveData<WebResponse>()
    val responseChangeStatus: LiveData<WebResponse> = _responseChangeStatus

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getPregnant(user: String) {
        getPregnantPubUseCase(user,
            onResult = { resp ->
                _responseGetPregnant.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun changeStatus(id: String, status: String) {
        changeStatusRegistrationUseCase(id, status,
            onResult = { resp ->
                _responseChangeStatus.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}