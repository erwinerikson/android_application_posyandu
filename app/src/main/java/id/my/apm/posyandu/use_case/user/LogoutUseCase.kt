package id.my.apm.posyandu.use_case.user

import id.my.apm.posyandu.domain.UserRepo
import id.my.apm.posyandu.model.WebResponse

class LogoutUseCase(private val repository: UserRepo) {
    operator fun invoke(onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.logout(onResult, onError)
    }
}