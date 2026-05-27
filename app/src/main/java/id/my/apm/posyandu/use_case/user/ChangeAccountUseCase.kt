package id.my.apm.posyandu.use_case.user

import id.my.apm.posyandu.domain.UserRepo
import id.my.apm.posyandu.model.WebResponse

class ChangeAccountUseCase(private val repository: UserRepo) {
    operator fun invoke(id: String, type: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.changeAccount(id, type, onResult, onError)
    }
}