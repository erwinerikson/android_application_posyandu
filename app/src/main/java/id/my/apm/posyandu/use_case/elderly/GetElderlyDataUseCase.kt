package id.my.apm.posyandu.use_case.elderly

import id.my.apm.posyandu.domain.ElderlyRepo
import id.my.apm.posyandu.model.ElderlyPub

class GetElderlyDataUseCase(private val repository: ElderlyRepo) {
    operator fun invoke(user: String, onResult: (ArrayList<ElderlyPub>) -> Unit, onError: (String) -> Unit) {
        repository.getElderlyDataPub(user, onResult, onError)
    }
}