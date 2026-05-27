package id.my.apm.posyandu.use_case.user

import id.my.apm.posyandu.domain.UserRepo
import id.my.apm.posyandu.model.WebResponse

class ChangePasswordUseCase(private val repository: UserRepo) {
    operator fun invoke(username: String, password: String, passwordNew: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        // Bisa tambahkan validasi bisnis di sini
        repository.changePassword(username, password, passwordNew, onResult, onError)
    }
}