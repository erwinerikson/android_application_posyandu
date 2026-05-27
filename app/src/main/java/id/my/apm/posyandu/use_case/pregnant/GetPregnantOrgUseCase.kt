package id.my.apm.posyandu.use_case.pregnant

import id.my.apm.posyandu.domain.MotherRepo
import id.my.apm.posyandu.model.RegistrationPub

class GetPregnantOrgUseCase(private val repository: MotherRepo) {
    operator fun invoke(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit) {
        repository.getPregnantOrg(onResult, onError)
    }
}