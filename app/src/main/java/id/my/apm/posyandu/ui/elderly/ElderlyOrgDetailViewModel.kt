package id.my.apm.posyandu.ui.elderly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.ElderlyPub
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.elderly.GetElderlyDataUseCase
import id.my.apm.posyandu.use_case.elderly.GetElderlyPubUseCase
import id.my.apm.posyandu.use_case.registration.ChangeStatusRegistrationUseCase

class ElderlyOrgDetailViewModel(
    private val getElderlyPubUseCase: GetElderlyPubUseCase,
    private val getElderlyDataUseCase: GetElderlyDataUseCase,
    private val changeStatusRegistrationUseCase: ChangeStatusRegistrationUseCase,
) : ViewModel() {

    private val _responseGetElderlySelect = MutableLiveData<ElderlyPub>()
    val responseGetElderlySelect: LiveData<ElderlyPub> = _responseGetElderlySelect

    private val _responseGetElderlyData = MutableLiveData<ArrayList<ElderlyPub>>()
    val responseGetElderlyData: LiveData<ArrayList<ElderlyPub>> = _responseGetElderlyData

    private val _responseChangeStatus = MutableLiveData<WebResponse>()
    val responseChangeStatus: LiveData<WebResponse> = _responseChangeStatus

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getElderlySelect(id: String) {
        getElderlyPubUseCase(id,
            onResult = { resp ->
                _responseGetElderlySelect.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun getElderlyData(user: String) {
        getElderlyDataUseCase(user,
            onResult = { resp ->
                _responseGetElderlyData.postValue(resp)
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