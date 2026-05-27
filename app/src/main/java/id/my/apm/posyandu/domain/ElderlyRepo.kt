package id.my.apm.posyandu.domain

import id.my.apm.posyandu.model.ElderlyPub
import id.my.apm.posyandu.model.RegistrationPub

interface ElderlyRepo {

    fun getElderlyPub(id: String, onResult: (ElderlyPub) -> Unit, onError: (String) -> Unit)
    fun getElderlyDataPub(user: String, onResult: (ArrayList<ElderlyPub>) -> Unit, onError: (String) -> Unit)
    fun getElderlyOrg(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit)
}