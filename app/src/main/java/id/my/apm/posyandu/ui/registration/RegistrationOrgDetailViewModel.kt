package id.my.apm.posyandu.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.registration.ChangeStatusRegistrationUseCase

class RegistrationOrgDetailViewModel(private val changeStatusRegistrationUseCase: ChangeStatusRegistrationUseCase) : ViewModel() {

    private val _responseChangeStatus = MutableLiveData<WebResponse>()
    val responseChangeStatus: LiveData<WebResponse> = _responseChangeStatus

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

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