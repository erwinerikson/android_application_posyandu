package id.my.apm.posyandu.use_case.user

import id.my.apm.posyandu.domain.UserRepo
import id.my.apm.posyandu.model.WebResponse

class RegisterUseCase(private val repository: UserRepo) {
    operator fun invoke(name: String, address: String, phone: String, username: String, password: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        // Bisa tambahkan validasi bisnis di sini
        repository.register(name, address, phone, username, password, onResult, onError)
    }
}