package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.WebResponse

class SaveMedicationUseCase(private val repository: AllRepo) {
    operator fun invoke(idUser: String, idPasien: String, idObat: String, qty: String, kode: String, idBidan: String, harga: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.saveMedication(idUser, idPasien, idObat, qty, kode, idBidan, harga, onResult, onError)
    }
}