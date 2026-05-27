package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.WebResponse

class SaveCheckUseCase(private val repository: AllRepo) {
    operator fun invoke(idUser: String, idPasien: String, idPeriksa: String, berat: String, tinggi: String, tensi: String, hasil: String, idBidan: String, harga: String, kode: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.saveCheck(idUser, idPasien, idPeriksa, berat, tinggi, tensi, hasil, idBidan, harga, kode, onResult, onError)
    }
}