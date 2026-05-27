package id.my.apm.posyandu.ui.mother

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.MotherPub
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.mother.GetMotherPubUseCase
import id.my.apm.posyandu.use_case.registration.ChangeStatusRegistrationUseCase

class MotherOrgDetailViewModel(
    private val getMotherPubUseCase: GetMotherPubUseCase,
    private val changeStatusRegistrationUseCase: ChangeStatusRegistrationUseCase,
) : ViewModel() {

    private val _responseGetMother = MutableLiveData<MotherPub>()
    val responseGetMother: LiveData<MotherPub> = _responseGetMother

    private val _responseChangeStatus = MutableLiveData<WebResponse>()
    val responseChangeStatus: LiveData<WebResponse> = _responseChangeStatus

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getMother(user: String) {
        getMotherPubUseCase(user,
            onResult = { resp ->
                _responseGetMother.postValue(resp)
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