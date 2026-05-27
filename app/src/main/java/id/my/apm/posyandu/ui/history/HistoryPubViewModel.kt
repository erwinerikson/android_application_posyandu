package id.my.apm.posyandu.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.HistoryPub
import id.my.apm.posyandu.use_case.GetAllHistoryPubUseCase
import id.my.apm.posyandu.utils.SessionManager

class HistoryPubViewModel(
    private val getAllHistoryPubUseCase: GetAllHistoryPubUseCase,
    sessionManager: SessionManager
) : ViewModel() {

    private val user = sessionManager.getAuthData()?.id.toString()

    private val _responseGetHistory = MutableLiveData<ArrayList<HistoryPub>>()
    val responseGetHistory: LiveData<ArrayList<HistoryPub>> = _responseGetHistory

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getHistory() {
        getAllHistoryPubUseCase(user,
            onResult = { resp ->
                _responseGetHistory.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}