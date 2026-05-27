package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.HistoryPub

class GetAllHistoryPubUseCase(private val repository: AllRepo) {
    operator fun invoke(user: String, onResult: (ArrayList<HistoryPub>) -> Unit, onError: (String) -> Unit) {
        repository.getHistoryPub(user, onResult, onError)
    }
}