package id.my.apm.posyandu.use_case.mother

import id.my.apm.posyandu.domain.MotherRepo
import id.my.apm.posyandu.model.MotherCheck

class GetMotherCheckHistoryUseCase(private val repository: MotherRepo) {
    operator fun invoke(kode: String, user: String, onResult: (ArrayList<MotherCheck>) -> Unit, onError: (String) -> Unit) {
        repository.getMotherCheck(kode, user, onResult, onError)
    }
}