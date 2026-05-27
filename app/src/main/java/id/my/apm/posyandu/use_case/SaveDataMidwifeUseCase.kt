package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.WebResponse

class SaveDataMidwifeUseCase(private val repository: AllRepo) {
    operator fun invoke(nama: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.saveDataMidwife(nama, onResult, onError)
    }
}