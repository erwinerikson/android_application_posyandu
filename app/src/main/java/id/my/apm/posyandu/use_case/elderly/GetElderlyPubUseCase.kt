package id.my.apm.posyandu.use_case.elderly

import id.my.apm.posyandu.domain.ElderlyRepo
import id.my.apm.posyandu.model.ElderlyPub

class GetElderlyPubUseCase(private val repository: ElderlyRepo) {
    operator fun invoke(id: String, onResult: (ElderlyPub) -> Unit, onError: (String) -> Unit) {
        repository.getElderlyPub(id, onResult, onError)
    }
}