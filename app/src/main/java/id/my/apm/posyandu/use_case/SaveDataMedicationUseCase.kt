package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.WebResponse

class SaveDataMedicationUseCase(private val repository: AllRepo) {
    operator fun invoke(nama: String, satuan: String, harga: String, stok: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.saveDataMedication(nama, satuan, harga, stok, onResult, onError)
    }
}