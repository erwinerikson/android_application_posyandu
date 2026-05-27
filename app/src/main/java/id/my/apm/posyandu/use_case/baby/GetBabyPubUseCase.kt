package id.my.apm.posyandu.use_case.baby

import id.my.apm.posyandu.domain.BabyRepo
import id.my.apm.posyandu.model.BabyPub

class GetBabyPubUseCase(private val repository: BabyRepo) {
    operator fun invoke(id: String, onResult: (BabyPub) -> Unit, onError: (String) -> Unit) {
        repository.getBabyPub(id, onResult, onError)
    }
}