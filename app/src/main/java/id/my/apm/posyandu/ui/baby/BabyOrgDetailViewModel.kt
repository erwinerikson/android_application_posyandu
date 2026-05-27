package id.my.apm.posyandu.ui.baby

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.BabyPub
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.baby.GetBabiesPubUseCase
import id.my.apm.posyandu.use_case.baby.GetBabyPubUseCase
import id.my.apm.posyandu.use_case.registration.ChangeStatusRegistrationUseCase

class BabyOrgDetailViewModel(
    private val getBabiesPubUseCase: GetBabiesPubUseCase,
    private val getBabyPubUseCase: GetBabyPubUseCase,
    private val changeStatusRegistrationUseCase: ChangeStatusRegistrationUseCase,
) : ViewModel() {

    private val _responseGetBabies = MutableLiveData<ArrayList<BabyPub>>()
    val responseGetBabies: LiveData<ArrayList<BabyPub>> = _responseGetBabies

    private val _responseGetBaby = MutableLiveData<BabyPub>()
    val responseGetBaby: LiveData<BabyPub> = _responseGetBaby

    private val _responseGetBabySelect = MutableLiveData<BabyPub>()
    val responseGetBabySelect: LiveData<BabyPub> = _responseGetBabySelect

    private val _responseChangeStatus = MutableLiveData<WebResponse>()
    val responseChangeStatus: LiveData<WebResponse> = _responseChangeStatus

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getBabies(id: String) {
        getBabiesPubUseCase(id,
            onResult = { resp ->
                _responseGetBabies.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun getBaby(id: String) {
        getBabyPubUseCase(id,
            onResult = { resp ->
                _responseGetBaby.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun getBabySelect(id: String) {
        getBabyPubUseCase(id,
            onResult = { resp ->
                _responseGetBabySelect.postValue(resp)
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