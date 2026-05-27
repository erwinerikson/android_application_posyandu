package id.my.apm.posyandu.use_case.registration

import id.my.apm.posyandu.domain.RegistrationRepo
import id.my.apm.posyandu.model.RegistrationPub

class GetRegistrationOrgUseCase(private val repository: RegistrationRepo) {
    operator fun invoke(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit) {
        repository.getRegistrationOrg(onResult, onError)
    }
}