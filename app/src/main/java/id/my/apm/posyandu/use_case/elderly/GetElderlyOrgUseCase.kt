package id.my.apm.posyandu.use_case.elderly

import id.my.apm.posyandu.domain.ElderlyRepo
import id.my.apm.posyandu.model.RegistrationPub

class GetElderlyOrgUseCase(private val repository: ElderlyRepo) {
    operator fun invoke(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit) {
        repository.getElderlyOrg(onResult, onError)
    }
}