package id.my.apm.posyandu.ui.schedule

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.SchedulePub
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.EditScheduleUseCase
import id.my.apm.posyandu.use_case.GetAllSchedulePubUseCase
import id.my.apm.posyandu.use_case.SaveScheduleUseCase

class ScheduleOrgViewModel(
    private val getAllSchedulePubUseCase: GetAllSchedulePubUseCase,
    private val editScheduleUseCase: EditScheduleUseCase,
    private val saveScheduleUseCase: SaveScheduleUseCase
) : ViewModel() {

    private val _responseGetSchedule = MutableLiveData<ArrayList<SchedulePub>>()
    val responseGetSchedule: LiveData<ArrayList<SchedulePub>> = _responseGetSchedule

    private val _responseSaveSchedule = MutableLiveData<WebResponse>()
    val responseSaveSchedule: LiveData<WebResponse> = _responseSaveSchedule

    private val _responseEditSchedule = MutableLiveData<WebResponse>()
    val responseEditSchedule: LiveData<WebResponse> = _responseEditSchedule

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

    fun addSchedule(tgl: String, ket: String) {
        saveScheduleUseCase(tgl, ket,
            onResult = { resp ->
                _responseSaveSchedule.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun editSchedule(tgl: String, ket: String, status: String, id: String) {
        editScheduleUseCase(tgl, ket, status, id,
            onResult = { resp ->
                _responseEditSchedule.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}