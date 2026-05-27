package id.my.apm.posyandu.domain

import id.my.apm.posyandu.model.MotherCheck
import id.my.apm.posyandu.model.MotherPub
import id.my.apm.posyandu.model.PregnantImmunization
import id.my.apm.posyandu.model.RegistrationPub

interface MotherRepo {

    fun getMotherPub(user: String, onResult: (MotherPub) -> Unit, onError: (String) -> Unit)
    fun getPregnantPub(user: String, onResult: (MotherPub) -> Unit, onError: (String) -> Unit)
    fun getMotherCheck(kode: String, user: String, onResult: (ArrayList<MotherCheck>) -> Unit, onError: (String) -> Unit)
    fun getPregnantImmunization(id: String, onResult: (ArrayList<PregnantImmunization>) -> Unit, onError: (String) -> Unit)
    fun getMotherOrg(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit)
    fun getPregnantOrg(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit)
}