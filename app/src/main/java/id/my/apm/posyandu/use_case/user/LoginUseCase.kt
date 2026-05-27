package id.my.apm.posyandu.use_case.user

import id.my.apm.posyandu.domain.UserRepo
import id.my.apm.posyandu.model.AuthData

class LoginUseCase(private val repository: UserRepo) {
    operator fun invoke(username: String, password: String, onResult: (AuthData?) -> Unit, onError: (String) -> Unit) {
        // Bisa tambahkan validasi bisnis di sini
        repository.login(username, password, onResult, onError)
    }
}