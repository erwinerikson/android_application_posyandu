package id.my.apm.posyandu.ui.pregnant

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.DataImmunization
import id.my.apm.posyandu.model.DataMidwife
import id.my.apm.posyandu.model.PregnantImmunization
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.GetDataImmunizationUseCase
import id.my.apm.posyandu.use_case.GetDataMidwifeUseCase
import id.my.apm.posyandu.use_case.SaveImmunPregnantUseCase
import id.my.apm.posyandu.use_case.pregnant.GetPregnantImmunizationUseCase

class PregnantActionImmunizationViewModel(
    private val getPregnantImmunizationUseCase: GetPregnantImmunizationUseCase,
    private val getDataImmunizationUseCase: GetDataImmunizationUseCase,
    private val getDataMidwifeUseCase: GetDataMidwifeUseCase,
    private val saveImmunPregnantUseCase: SaveImmunPregnantUseCase
) : ViewModel() {

    private val _responseGetImmunization = MutableLiveData<ArrayList<PregnantImmunization>>()
    val responseGetImmunization: LiveData<ArrayList<PregnantImmunization>> = _responseGetImmunization

    private val _responseGetDataImmunization = MutableLiveData<ArrayList<DataImmunization>>()
    val responseGetDataImmunization: LiveData<ArrayList<DataImmunization>> = _responseGetDataImmunization

    private val _responseGetDataMidwife = MutableLiveData<ArrayList<DataMidwife>>()
    val responseGetDataMidwife: LiveData<ArrayList<DataMidwife>> = _responseGetDataMidwife

    private val _responseProcess = MutableLiveData<WebResponse>()
    val responseProcess: LiveData<WebResponse> = _responseProcess

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getImmunization(id: String) {
        getPregnantImmunizationUseCase(id,
            onResult = { resp ->
                _responseGetImmunization.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun getDataImmunization() {
        getDataImmunizationUseCase("2",
            onResult = { resp ->
                _responseGetDataImmunization.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun getDataMidwife() {
        getDataMidwifeUseCase(
            onResult = { resp ->
                _responseGetDataMidwife.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun process(idUser: String, idIbu: String, idImun: String, idBidan: String, harga: String) {
        saveImmunPregnantUseCase(idUser, idIbu, idImun, idBidan, harga,
            onResult = { resp ->
                _responseProcess.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}