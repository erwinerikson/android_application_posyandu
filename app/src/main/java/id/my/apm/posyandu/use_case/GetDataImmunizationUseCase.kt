package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.DataImmunization

class GetDataImmunizationUseCase(private val repository: AllRepo) {
    operator fun invoke(kode: String, onResult: (ArrayList<DataImmunization>) -> Unit, onError: (String) -> Unit) {
        repository.getDataImmunization(kode, onResult, onError)
    }
}