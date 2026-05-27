package id.my.apm.posyandu.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.AuthData
import id.my.apm.posyandu.use_case.user.LogoutUseCase
import id.my.apm.posyandu.utils.SessionManager

class UserViewModel(
    private val logoutUseCase: LogoutUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _responseGet = MutableLiveData<AuthData>()
    val responseGet: LiveData<AuthData> = _responseGet

    private val _errorGet = MutableLiveData<Boolean>()
    val errorGet: LiveData<Boolean> = _errorGet

    fun getSession() {
        val auth = sessionManager.getAuthData()
        if (auth != null) {
            _responseGet.postValue(auth)
            _errorGet.postValue(false)
        } else {
            _errorGet.postValue(true)
        }
    }

    fun logout() {
        logoutUseCase(
            onResult = { resp ->
                if (!resp.error) {
                    sessionManager.logout()
                    getSession()
                } else {
                    _errorGet.postValue(true)
                }
            },
            onError = {
                _errorGet.postValue(true)
            }
        )
    }
}