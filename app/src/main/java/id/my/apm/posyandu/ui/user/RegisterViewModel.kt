package id.my.apm.posyandu.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.user.RegisterUseCase

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {

    private val _response = MutableLiveData<WebResponse>()
    val response: LiveData<WebResponse> = _response

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun register(name: String, address: String, phone: String, username: String, password: String) {
        registerUseCase(name, address, phone, username, password,
            onResult = { resp ->
                _response.postValue(resp)
            },
            onError = { error ->
                _errorMessage.postValue(error)
            }
        )
    }
}