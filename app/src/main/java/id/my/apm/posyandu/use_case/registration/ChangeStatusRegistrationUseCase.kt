package id.my.apm.posyandu.use_case.registration

import id.my.apm.posyandu.domain.RegistrationRepo
import id.my.apm.posyandu.model.WebResponse

class ChangeStatusRegistrationUseCase(private val repository: RegistrationRepo) {
    operator fun invoke(id: String, status: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.changeStatusRegistration(id, status, onResult, onError)
    }
}