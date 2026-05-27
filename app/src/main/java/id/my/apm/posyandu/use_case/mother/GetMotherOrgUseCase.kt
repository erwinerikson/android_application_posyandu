package id.my.apm.posyandu.use_case.mother

import id.my.apm.posyandu.domain.MotherRepo
import id.my.apm.posyandu.model.RegistrationPub

class GetMotherOrgUseCase(private val repository: MotherRepo) {
    operator fun invoke(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit) {
        repository.getMotherOrg(onResult, onError)
    }
}