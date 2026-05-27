package id.my.apm.posyandu.use_case.finance

import id.my.apm.posyandu.domain.FinanceRepo
import id.my.apm.posyandu.model.DataFinance

class GetFinanceUseCase(private val repository: FinanceRepo) {
    operator fun invoke(kodeTrans: String, onResult: (ArrayList<DataFinance>) -> Unit, onError: (String) -> Unit) {
        repository.getFinance(kodeTrans, onResult, onError)
    }
}