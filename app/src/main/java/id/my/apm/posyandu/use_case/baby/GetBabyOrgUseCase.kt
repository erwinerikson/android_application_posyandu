package id.my.apm.posyandu.use_case.baby

import id.my.apm.posyandu.domain.BabyRepo
import id.my.apm.posyandu.model.RegistrationPub

class GetBabyOrgUseCase(private val repository: BabyRepo) {
    operator fun invoke(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit) {
        repository.getBabyOrg(onResult, onError)
    }
}