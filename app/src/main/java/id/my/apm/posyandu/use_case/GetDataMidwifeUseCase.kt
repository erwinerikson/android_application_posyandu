package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.DataMidwife

class GetDataMidwifeUseCase(private val repository: AllRepo) {
    operator fun invoke(onResult: (ArrayList<DataMidwife>) -> Unit, onError: (String) -> Unit) {
        repository.getDataMidwife(onResult, onError)
    }
}