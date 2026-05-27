package id.my.apm.posyandu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.AuthData
import id.my.apm.posyandu.utils.SessionManager

class MainViewModel(private val sessionManager: SessionManager) : ViewModel() {

    private val _responseCheck = MutableLiveData<AuthData?>()
    val responseCheck: LiveData<AuthData?> = _responseCheck

    fun checkSession() {
        val user = sessionManager.getAuthData()
        _responseCheck.postValue(user)
        /*if (user != null) {
            _responseCheck.postValue(user.userType)
        } else {
            _responseCheck.postValue("back")
        }*/
    }
}