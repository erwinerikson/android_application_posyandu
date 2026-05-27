package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.WebResponse

class SaveScheduleUseCase(private val repository: AllRepo) {
    operator fun invoke(tgl: String, ket: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.saveSchedule(tgl, ket, onResult, onError)
    }
}