package id.my.apm.posyandu.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.User
import id.my.apm.posyandu.use_case.user.GetUserUseCase

class GetUserViewModel(private val userUseCase: GetUserUseCase) : ViewModel() {

    // LiveData untuk memantau data di UI
    private val _userData = MutableLiveData<User?>()
    val userData: LiveData<User?> = _userData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun register(id: String) {
       userUseCase(id,
            onResult = { user ->
                _userData.postValue(user)
            },
            onError = { error ->
                _errorMessage.postValue(error)
            }
        )
    }

    fun fetchUser(id: String) {
        userUseCase(id,
            onResult = { user ->
                _userData.postValue(user)
            },
            onError = { error ->
                _errorMessage.postValue(error)
            }
        )
    }
}