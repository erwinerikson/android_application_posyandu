package id.my.apm.posyandu.use_case.baby

import id.my.apm.posyandu.domain.BabyRepo
import id.my.apm.posyandu.model.BabyPub

class CheckBabyPubUseCase(private val repository: BabyRepo) {
    operator fun invoke(onResult: (BabyPub) -> Unit, onError: (String) -> Unit) {
        repository.checkBabyPub(onResult, onError)
    }
}