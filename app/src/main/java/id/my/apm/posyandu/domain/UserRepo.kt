package id.my.apm.posyandu.domain

import id.my.apm.posyandu.model.AuthData
import id.my.apm.posyandu.model.User
import id.my.apm.posyandu.model.WebResponse

interface UserRepo {
    fun register(name: String, address: String, phone: String, username: String, password: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)

    fun login(username: String, password: String, onResult: (AuthData?) -> Unit, onError: (String) -> Unit)

    fun logout(onResult: (WebResponse) -> Unit, onError: (String) -> Unit)

    fun changePassword(username: String, password: String, passwordNew: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)

    fun changeAccount(id: String, type: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)

    fun getUserById(id: String, onResult: (User?) -> Unit, onError: (String) -> Unit)

    fun getUsers(onResult: (ArrayList<User>) -> Unit, onError: (String) -> Unit)
}