package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.WebResponse

class EditScheduleUseCase(private val repository: AllRepo) {
    operator fun invoke(tgl: String, ket: String, status: String, id: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.editSchedule(tgl, ket, status, id, onResult, onError)
    }
}