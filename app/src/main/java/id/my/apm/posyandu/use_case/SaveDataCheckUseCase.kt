package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.WebResponse

class SaveDataCheckUseCase(private val repository: AllRepo) {
    operator fun invoke(nama: String, harga: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.saveDataCheck(nama, harga, onResult, onError)
    }
}