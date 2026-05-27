package id.my.apm.posyandu.use_case.baby

import id.my.apm.posyandu.domain.BabyRepo
import id.my.apm.posyandu.model.ChildImmunization

class GetBabyImmunizationUseCase(private val repository: BabyRepo) {
    operator fun invoke(idBaby: String, onResult: (ArrayList<ChildImmunization>) -> Unit, onError: (String) -> Unit) {
        repository.getBabyImmunization(idBaby, onResult, onError)
    }
}