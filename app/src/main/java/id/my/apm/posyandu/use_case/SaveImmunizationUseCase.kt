package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.WebResponse

class SaveImmunizationUseCase(private val repository: AllRepo) {
    operator fun invoke(idUser: String, idBayi: String, idImun: String, berat: String, tinggi: String, idBidan: String, harga: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.saveImmunization(idUser, idBayi, idImun, berat, tinggi, idBidan, harga, onResult, onError)
    }
}