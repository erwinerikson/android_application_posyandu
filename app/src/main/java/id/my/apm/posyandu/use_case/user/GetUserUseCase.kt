package id.my.apm.posyandu.use_case.user

import id.my.apm.posyandu.domain.UserRepo
import id.my.apm.posyandu.model.User

class GetUserUseCase(private val repository: UserRepo) {
    operator fun invoke(id: String, onResult: (User?) -> Unit, onError: (String) -> Unit) {
        // Bisa tambahkan validasi bisnis di sini
        repository.getUserById(id, onResult, onError)
    }
}