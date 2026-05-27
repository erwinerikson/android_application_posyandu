package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.WebResponse

class SaveDataImmunizationUseCase(private val repository: AllRepo) {
    operator fun invoke(nama: String, harga: String, kode: String, ket: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.saveDataImmunization(nama, harga, kode, ket, onResult, onError)
    }
}