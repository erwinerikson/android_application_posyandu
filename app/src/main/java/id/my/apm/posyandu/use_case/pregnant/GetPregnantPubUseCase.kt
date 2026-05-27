package id.my.apm.posyandu.use_case.pregnant

import id.my.apm.posyandu.domain.MotherRepo
import id.my.apm.posyandu.model.MotherPub

class GetPregnantPubUseCase(private val repository: MotherRepo) {
    operator fun invoke(user: String, onResult: (MotherPub) -> Unit, onError: (String) -> Unit) {
        repository.getPregnantPub(user, onResult, onError)
    }
}