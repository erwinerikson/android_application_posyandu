package id.my.apm.posyandu.ui.medication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.DataCheck
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.GetDataCheckUseCase
import id.my.apm.posyandu.use_case.SaveDataCheckUseCase

class CheckViewModel(
    private val getDataCheckUseCase: GetDataCheckUseCase,
    private val saveDataCheckUseCase: SaveDataCheckUseCase
) : ViewModel() {

    private val _responseGetCheck = MutableLiveData<ArrayList<DataCheck>>()
    val responseGetCheck: LiveData<ArrayList<DataCheck>> = _responseGetCheck

    private val _responseProcess = MutableLiveData<WebResponse>()
    val responseProcess: LiveData<WebResponse> = _responseProcess

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getCheck() {
        getDataCheckUseCase(
            onResult = { resp ->
                _responseGetCheck.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun process(nama: String, harga: String) {
        saveDataCheckUseCase(nama, harga,
            onResult = { resp ->
                _responseProcess.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}