package id.my.apm.posyandu.use_case.registration

import id.my.apm.posyandu.domain.RegistrationRepo
import id.my.apm.posyandu.model.WebResponse

class RegistrationPubUseCase(private val repository: RegistrationRepo) {
    operator fun invoke(baby: String, mother: String, pregnantMother: String, elderly: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.registrationPub(baby, mother, pregnantMother, elderly, onResult, onError)
    }
}