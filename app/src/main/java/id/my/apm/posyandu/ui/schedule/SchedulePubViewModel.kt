package id.my.apm.posyandu.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.SchedulePub
import id.my.apm.posyandu.use_case.GetAllSchedulePubUseCase

class SchedulePubViewModel(private val getAllSchedulePubUseCase: GetAllSchedulePubUseCase) : ViewModel() {

    private val _responseGetSchedule = MutableLiveData<ArrayList<SchedulePub>>()
    val responseGetSchedule: LiveData<ArrayList<SchedulePub>> = _responseGetSchedule

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getSchedule() {
        getAllSchedulePubUseCase(
            onResult = { resp ->
                _responseGetSchedule.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}