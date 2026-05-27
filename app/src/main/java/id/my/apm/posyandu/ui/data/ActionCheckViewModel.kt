package id.my.apm.posyandu.ui.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.CheckHistory
import id.my.apm.posyandu.model.DataCheck
import id.my.apm.posyandu.model.DataMidwife
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.GetAllCheckHistoryUseCase
import id.my.apm.posyandu.use_case.GetDataCheckUseCase
import id.my.apm.posyandu.use_case.GetDataMidwifeUseCase
import id.my.apm.posyandu.use_case.SaveCheckUseCase

class ActionCheckViewModel(
    private val getAllCheckHistoryUseCase: GetAllCheckHistoryUseCase,
    private val getDataCheckUseCase: GetDataCheckUseCase,
    private val getDataMidwifeUseCase: GetDataMidwifeUseCase,
    private val saveCheckUseCase: SaveCheckUseCase,
) : ViewModel() {

    private val _responseGetCheck = MutableLiveData<ArrayList<CheckHistory>>()
    val responseGetCheck: LiveData<ArrayList<CheckHistory>> = _responseGetCheck

    private val _responseGetDataCheck = MutableLiveData<ArrayList<DataCheck>>()
    val responseGetDataCheck: LiveData<ArrayList<DataCheck>> = _responseGetDataCheck

    private val _responseGetDataMidwife = MutableLiveData<ArrayList<DataMidwife>>()
    val responseGetDataMidwife: LiveData<ArrayList<DataMidwife>> = _responseGetDataMidwife

    private val _responseProcess = MutableLiveData<WebResponse>()
    val responseProcess: LiveData<WebResponse> = _responseProcess

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getCheck(kode: String, id: String) {
        getAllCheckHistoryUseCase(kode, id,
            onResult = { resp ->
                _responseGetCheck.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun getDataCheck() {
        getDataCheckUseCase(
            onResult = { resp ->
                _responseGetDataCheck.postValue(resp)
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

    fun process(idUser: String, idBayi: String, idPeriksa: String, berat: String, tinggi: String, tensi: String, hasil: String, idBidan: String, harga: String, kode: String) {
        saveCheckUseCase(idUser, idBayi, idPeriksa, berat, tinggi, tensi, hasil, idBidan, harga, kode,
            onResult = { resp ->
                _responseProcess.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}