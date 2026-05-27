package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.DataImmunization

class GetAllDataImmunizationUseCase(private val repository: AllRepo) {
    operator fun invoke(onResult: (ArrayList<DataImmunization>) -> Unit, onError: (String) -> Unit) {
        repository.getAllDataImmunization(onResult, onError)
    }
}