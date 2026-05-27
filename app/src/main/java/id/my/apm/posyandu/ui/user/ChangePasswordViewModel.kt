package id.my.apm.posyandu.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.user.ChangePasswordUseCase
import id.my.apm.posyandu.utils.SessionManager

class ChangePasswordViewModel(
    private val useCase: ChangePasswordUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _response = MutableLiveData<WebResponse>()
    val response: LiveData<WebResponse> = _response

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun change(password: String, passwordNew: String) {
        val user = sessionManager.getAuthData()
        if (user != null) {
            useCase(
                user.username, password, passwordNew,
                onResult = { resp ->
                    _response.postValue(resp)
                },
                onError = { error ->
                    _errorMessage.postValue(error)
                }
            )
        } else {
            _errorMessage.postValue("error viewModel")
        }
    }
}