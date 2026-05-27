package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.SchedulePub

class GetAllSchedulePubUseCase(private val repository: AllRepo) {
    operator fun invoke(onResult: (ArrayList<SchedulePub>) -> Unit, onError: (String) -> Unit) {
        repository.getSchedulePub(onResult, onError)
    }
}