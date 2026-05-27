package id.my.apm.posyandu.use_case.baby

import id.my.apm.posyandu.domain.BabyRepo
import id.my.apm.posyandu.model.BabyPub

class GetBabiesPubUseCase(private val repository: BabyRepo) {
    operator fun invoke(idUser: String, onResult: (ArrayList<BabyPub>) -> Unit, onError: (String) -> Unit) {
        repository.getBabiesPub(idUser, onResult, onError)
    }
}