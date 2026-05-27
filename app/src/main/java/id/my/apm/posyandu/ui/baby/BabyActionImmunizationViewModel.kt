package id.my.apm.posyandu.ui.baby

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.ChildImmunization
import id.my.apm.posyandu.model.DataImmunization
import id.my.apm.posyandu.model.DataMidwife
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.GetDataImmunizationUseCase
import id.my.apm.posyandu.use_case.GetDataMidwifeUseCase
import id.my.apm.posyandu.use_case.SaveImmunizationUseCase
import id.my.apm.posyandu.use_case.baby.GetBabyImmunizationUseCase

class BabyActionImmunizationViewModel(
    private val getBabyImmunizationUseCase: GetBabyImmunizationUseCase,
    private val getDataImmunizationUseCase: GetDataImmunizationUseCase,
    private val getDataMidwifeUseCase: GetDataMidwifeUseCase,
    private val saveImmunizationUseCase: SaveImmunizationUseCase
) : ViewModel() {

    private val _responseGetImmunization = MutableLiveData<ArrayList<ChildImmunization>>()
    val responseGetImmunization: LiveData<ArrayList<ChildImmunization>> = _responseGetImmunization

    private val _responseGetDataImmunization = MutableLiveData<ArrayList<DataImmunization>>()
    val responseGetDataImmunization: LiveData<ArrayList<DataImmunization>> = _responseGetDataImmunization

    private val _responseGetDataMidwife = MutableLiveData<ArrayList<DataMidwife>>()
    val responseGetDataMidwife: LiveData<ArrayList<DataMidwife>> = _responseGetDataMidwife

    private val _responseProcess = MutableLiveData<WebResponse>()
    val responseProcess: LiveData<WebResponse> = _responseProcess

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getImmunization(id: String) {
        getBabyImmunizationUseCase(id,
            onResult = { resp ->
                _responseGetImmunization.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun getDataImmunization() {
        getDataImmunizationUseCase("1",
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

    fun process(idUser: String, idBayi: String, idImun: String, berat: String, tinggi: String, idBidan: String, harga: String) {
        saveImmunizationUseCase(idUser, idBayi, idImun, berat, tinggi, idBidan, harga,
            onResult = { resp ->
                _responseProcess.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}