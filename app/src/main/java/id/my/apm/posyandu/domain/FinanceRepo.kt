package id.my.apm.posyandu.domain

import id.my.apm.posyandu.model.DataFinance
import id.my.apm.posyandu.model.DetailsFinance

interface FinanceRepo {

    fun getFinance(kodeTrans: String, onResult: (ArrayList<DataFinance>) -> Unit, onError: (String) -> Unit)
    fun getDetailsFinance(onResult: (DetailsFinance) -> Unit, onError: (String) -> Unit)
}