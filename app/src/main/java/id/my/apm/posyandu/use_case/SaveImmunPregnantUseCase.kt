package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.WebResponse

class SaveImmunPregnantUseCase(private val repository: AllRepo) {
    operator fun invoke(idUser: String, idIbu: String, idImun: String, idBidan: String, harga: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.saveImmunPregnant(idUser, idIbu, idImun, idBidan, harga, onResult, onError)
    }
}