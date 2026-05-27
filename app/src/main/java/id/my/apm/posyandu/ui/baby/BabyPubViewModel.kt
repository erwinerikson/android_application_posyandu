package id.my.apm.posyandu.ui.baby

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.BabyPub
import id.my.apm.posyandu.model.ChildImmunization
import id.my.apm.posyandu.use_case.baby.CheckBabyPubUseCase
import id.my.apm.posyandu.use_case.baby.GetBabiesPubUseCase
import id.my.apm.posyandu.use_case.baby.GetBabyImmunizationUseCase
import id.my.apm.posyandu.use_case.baby.GetBabyPubUseCase
import id.my.apm.posyandu.utils.SessionManager

class BabyPubViewModel(
    private val checkBabyPubUseCase: CheckBabyPubUseCase,
    private val getBabyPubUseCase: GetBabyPubUseCase,
    private val getBabiesPubUseCase: GetBabiesPubUseCase,
    private val getBabyImmunizationUseCase: GetBabyImmunizationUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _responseCheckBaby = MutableLiveData<BabyPub>()
    val responseCheckBaby: LiveData<BabyPub> = _responseCheckBaby

    private val _responseGetBaby = MutableLiveData<BabyPub>()
    val responseGetBaby: LiveData<BabyPub> = _responseGetBaby

    private val _responseGetBabySelect = MutableLiveData<BabyPub>()
    val responseGetBabySelect: LiveData<BabyPub> = _responseGetBabySelect

    private val _responseGetBabies = MutableLiveData<ArrayList<BabyPub>>()
    val responseGetBabies: LiveData<ArrayList<BabyPub>> = _responseGetBabies

    private val _responseGetImmunization = MutableLiveData<ArrayList<ChildImmunization>>()
    val responseGetImmunization: LiveData<ArrayList<ChildImmunization>> = _responseGetImmunization

    private val _errorCheckBaby = MutableLiveData<Boolean>()
    val errorCheckBaby: LiveData<Boolean> = _errorCheckBaby

    fun checkBaby() {
        checkBabyPubUseCase(
            onResult = { resp ->
                _responseCheckBaby.postValue(resp)
                _errorCheckBaby.postValue(false)
            },
            onError = {
                _errorCheckBaby.postValue(true)
            }
        )
    }

    fun getBaby(id: String) {
        getBabyPubUseCase(id,
            onResult = { resp ->
                _responseGetBaby.postValue(resp)
                _errorCheckBaby.postValue(false)
            },
            onError = {
                _errorCheckBaby.postValue(true)
            }
        )
    }

    fun getBabySelect(id: String) {
        getBabyPubUseCase(id,
            onResult = { resp ->
                _responseGetBabySelect.postValue(resp)
                _errorCheckBaby.postValue(false)
            },
            onError = {
                _errorCheckBaby.postValue(true)
            }
        )
    }

    fun getBabies() {
        val user = sessionManager.getAuthData()
        getBabiesPubUseCase(user?.id.toString(),
            onResult = { resp ->
                _responseGetBabies.postValue(resp)
                _errorCheckBaby.postValue(false)
            },
            onError = {
                _errorCheckBaby.postValue(true)
            }
        )
    }

    fun getImmunization(id: String) {
        getBabyImmunizationUseCase(id,
            onResult = { resp ->
                _responseGetImmunization.postValue(resp)
                _errorCheckBaby.postValue(false)
            },
            onError = {
                _errorCheckBaby.postValue(true)
            }
        )
    }
}