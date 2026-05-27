package id.my.apm.posyandu.use_case.registration

import id.my.apm.posyandu.domain.RegistrationRepo
import id.my.apm.posyandu.model.RegistrationPub

class GetRegistrationPubUseCase(private val repository: RegistrationRepo) {
    operator fun invoke(onResult: (RegistrationPub) -> Unit, onError: (String) -> Unit) {
        repository.getRegistrationPub(onResult, onError)
    }
}