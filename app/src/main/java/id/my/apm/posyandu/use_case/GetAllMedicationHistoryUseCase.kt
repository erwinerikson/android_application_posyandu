package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.MedicationHistory

class GetAllMedicationHistoryUseCase(private val repository: AllRepo) {
    operator fun invoke(kode: String, id: String, onResult: (ArrayList<MedicationHistory>) -> Unit, onError: (String) -> Unit) {
        repository.getMedicationHistory(kode, id, onResult, onError)
    }
}