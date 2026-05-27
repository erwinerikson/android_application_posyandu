package id.my.apm.posyandu.use_case.pregnant

import id.my.apm.posyandu.domain.MotherRepo
import id.my.apm.posyandu.model.PregnantImmunization

class GetPregnantImmunizationUseCase(private val repository: MotherRepo) {
    operator fun invoke(id: String, onResult: (ArrayList<PregnantImmunization>) -> Unit, onError: (String) -> Unit) {
        repository.getPregnantImmunization(id, onResult, onError)
    }
}