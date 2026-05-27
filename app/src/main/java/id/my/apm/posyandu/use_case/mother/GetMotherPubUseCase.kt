package id.my.apm.posyandu.use_case.mother

import id.my.apm.posyandu.domain.MotherRepo
import id.my.apm.posyandu.model.MotherPub

class GetMotherPubUseCase(private val repository: MotherRepo) {
    operator fun invoke(user: String, onResult: (MotherPub) -> Unit, onError: (String) -> Unit) {
        repository.getMotherPub(user, onResult, onError)
    }
}