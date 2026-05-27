package id.my.apm.posyandu.use_case

import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.WebResponse

class SaveFinanceUseCase(private val repository: AllRepo) {
    operator fun invoke(kdTrans: String, kdSumber: String, desc: String, sumber: String, nominal: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.saveFinance(kdTrans, kdSumber, desc, sumber, nominal, onResult, onError)
    }
}