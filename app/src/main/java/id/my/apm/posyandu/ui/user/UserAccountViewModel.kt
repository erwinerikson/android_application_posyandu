package id.my.apm.posyandu.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.User
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.user.ChangeAccountUseCase
import id.my.apm.posyandu.use_case.user.GetUsersUseCase

class UserAccountViewModel(
    private val getUsersUseCase: GetUsersUseCase,
    private val changeAccountUseCase: ChangeAccountUseCase
) : ViewModel() {

    private val _responseGetUsers = MutableLiveData<ArrayList<User>>()
    val responseGetUsers: LiveData<ArrayList<User>> = _responseGetUsers

    private val _responseChange = MutableLiveData<WebResponse>()
    val responseChange: LiveData<WebResponse> = _responseChange

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> = _errorResponse

    fun getUsers() {
        getUsersUseCase(
            onResult = { resp ->
                _responseGetUsers.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }

    fun changeAccount(id: String, type: String) {
        changeAccountUseCase(id, type,
            onResult = { resp ->
                _responseChange.postValue(resp)
            },
            onError = { err ->
                _errorResponse.postValue(err)
            }
        )
    }
}