package id.my.apm.posyandu.use_case.registration

import id.my.apm.posyandu.domain.RegistrationRepo
import id.my.apm.posyandu.model.WebResponse

class ElderlyCheckUseCase(private val repository: RegistrationRepo) {
    operator fun invoke(onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.elderlyCheck(onResult, onError)
    }
}