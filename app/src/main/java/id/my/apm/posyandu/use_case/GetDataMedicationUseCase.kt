package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.DataMedication

class GetDataMedicationUseCase(private val repository: AllRepo) {
    operator fun invoke(onResult: (ArrayList<DataMedication>) -> Unit, onError: (String) -> Unit) {
        repository.getDataMedication(onResult, onError)
    }
}