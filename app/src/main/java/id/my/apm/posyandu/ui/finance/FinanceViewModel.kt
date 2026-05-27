package id.my.apm.posyandu.ui.finance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.DataFinance
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.SaveFinanceUseCase
import id.my.apm.posyandu.use_case.finance.GetFinanceUseCase

class FinanceViewModel(
    private val getFinanceUseCase: GetFinanceUseCase,
    private val saveFinanceUseCase: SaveFinanceUseCase
) : ViewModel() {

    private val _responseGetFinance = MutableLiveData<ArrayList<DataFinance>>()
    val responseGetFinance: LiveData<ArrayList<DataFinance>> = _responseGetFinance

    private val _responseProcess = MutableLiveData<WebResponse>()
    val responseProcess: LiveData<WebResponse> = _responseProcess

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getFinance(kode: String) {
        getFinanceUseCase(kode,
            onResult = { resp ->
                _responseGetFinance.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun process(kdTrans: String, kdSumber: String, desc: String, sumber: String, nominal: String) {
        saveFinanceUseCase(kdTrans, kdSumber, desc, sumber, nominal,
            onResult = { resp ->
                _responseProcess.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}