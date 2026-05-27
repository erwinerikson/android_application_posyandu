package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.domain.MotherRepo
import id.my.apm.posyandu.model.CheckHistory
import id.my.apm.posyandu.model.MotherCheck

class GetAllCheckHistoryUseCase(private val repository: AllRepo) {
    operator fun invoke(kode: String, id: String, onResult: (ArrayList<CheckHistory>) -> Unit, onError: (String) -> Unit) {
        repository.getCheckHistory(kode, id, onResult, onError)
    }
}