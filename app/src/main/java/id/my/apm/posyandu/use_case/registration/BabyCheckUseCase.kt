package id.my.apm.posyandu.use_case.registration

import id.my.apm.posyandu.domain.RegistrationRepo
import id.my.apm.posyandu.model.WebResponse

class BabyCheckUseCase(private val repository: RegistrationRepo) {
    operator fun invoke(onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.babyCheck(onResult, onError)
    }
}