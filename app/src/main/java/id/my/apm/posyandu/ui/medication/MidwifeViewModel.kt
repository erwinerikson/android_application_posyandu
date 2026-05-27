package id.my.apm.posyandu.ui.medication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.DataMidwife
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.GetDataMidwifeUseCase
import id.my.apm.posyandu.use_case.SaveDataMidwifeUseCase

class MidwifeViewModel(
    private val getDataMidwifeUseCase: GetDataMidwifeUseCase,
    private val saveDataMidwifeUseCase: SaveDataMidwifeUseCase
) : ViewModel() {

    private val _responseGetMidwife = MutableLiveData<ArrayList<DataMidwife>>()
    val responseGetMidwife: LiveData<ArrayList<DataMidwife>> = _responseGetMidwife

    private val _responseProcess = MutableLiveData<WebResponse>()
    val responseProcess: LiveData<WebResponse> = _responseProcess

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getMidwife() {
        getDataMidwifeUseCase(
            onResult = { resp ->
                _responseGetMidwife.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun process(nama: String) {
        saveDataMidwifeUseCase(nama,
            onResult = { resp ->
                _responseProcess.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}