package id.my.apm.posyandu.ui.finance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.DetailsFinance
import id.my.apm.posyandu.use_case.finance.GetDetailsFinanceUseCase

class FinanceDetailsViewModel(private val getDetailsFinanceUseCase: GetDetailsFinanceUseCase) : ViewModel() {

    private val _responseGetDetailsFinance = MutableLiveData<DetailsFinance>()
    val responseGetDetailsFinance: LiveData<DetailsFinance> = _responseGetDetailsFinance

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getDetailsFinance() {
        getDetailsFinanceUseCase(
            onResult = { resp ->
                _responseGetDetailsFinance.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}