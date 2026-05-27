package id.my.apm.posyandu.domain

import id.my.apm.posyandu.model.BabyPub
import id.my.apm.posyandu.model.ChildImmunization
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.model.WebResponse

interface BabyRepo {

    fun checkBabyPub(onResult: (BabyPub) -> Unit, onError: (String) -> Unit)
    fun getBabyPub(id: String, onResult: (BabyPub) -> Unit, onError: (String) -> Unit)
    fun getBabiesPub(idUser: String, onResult: (ArrayList<BabyPub>) -> Unit, onError: (String) -> Unit)
    fun getBabyImmunization(idBaby: String, onResult: (ArrayList<ChildImmunization>) -> Unit, onError: (String) -> Unit)
    fun getBabyOrg(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit)
}