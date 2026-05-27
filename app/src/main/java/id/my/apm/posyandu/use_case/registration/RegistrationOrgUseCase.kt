package id.my.apm.posyandu.use_case.registration

import id.my.apm.posyandu.domain.RegistrationRepo
import id.my.apm.posyandu.model.RegistrationOrg

class RegistrationOrgUseCase(private val repository: RegistrationRepo) {
    operator fun invoke(onResult: (RegistrationOrg) -> Unit, onError: (String) -> Unit) {
        repository.registrationOrg(onResult, onError)
    }
}