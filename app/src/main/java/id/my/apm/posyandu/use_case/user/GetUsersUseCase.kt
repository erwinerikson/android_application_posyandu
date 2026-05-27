package id.my.apm.posyandu.use_case.user

import id.my.apm.posyandu.domain.UserRepo
import id.my.apm.posyandu.model.User

class GetUsersUseCase(private val repository: UserRepo) {
    operator fun invoke(onResult: (ArrayList<User>) -> Unit, onError: (String) -> Unit) {
        repository.getUsers(onResult, onError)
    }
}