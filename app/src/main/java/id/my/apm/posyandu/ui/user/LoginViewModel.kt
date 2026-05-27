package id.my.apm.posyandu.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.AuthData
import id.my.apm.posyandu.use_case.user.LoginUseCase
import id.my.apm.posyandu.utils.SessionManager

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

//    private val _responseCheck = MutableLiveData<AuthData?>()
//    val responseCheck: LiveData<AuthData?> = _responseCheck

    private val _response = MutableLiveData<AuthData?>()
    val response: LiveData<AuthData?> = _response

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun login(username: String, password: String) {
        loginUseCase(username, password,
            onResult = { resp ->
                sessionManager.saveAuthData(resp)
                _response.postValue(resp)
            },
            onError = { error ->
                _errorMessage.postValue(error)
            }
        )
    }

    /*fun checkSession() {
        val user = sessionManager.getAuthData()
        _responseCheck.postValue(user)
    }*/
}