package id.my.apm.posyandu.use_case.finance

import id.my.apm.posyandu.domain.FinanceRepo
import id.my.apm.posyandu.model.DetailsFinance

class GetDetailsFinanceUseCase(private val repository: FinanceRepo) {
    operator fun invoke(onResult: (DetailsFinance) -> Unit, onError: (String) -> Unit) {
        repository.getDetailsFinance(onResult, onError)
    }
}