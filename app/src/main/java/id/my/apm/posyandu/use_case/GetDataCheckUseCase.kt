package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.DataCheck

class GetDataCheckUseCase(private val repository: AllRepo) {
    operator fun invoke(onResult: (ArrayList<DataCheck>) -> Unit, onError: (String) -> Unit) {
        repository.getDataCheck(onResult, onError)
    }
}