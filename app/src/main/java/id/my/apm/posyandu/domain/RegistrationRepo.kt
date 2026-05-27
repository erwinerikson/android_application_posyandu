package id.my.apm.posyandu.domain

import id.my.apm.posyandu.model.RegistrationOrg
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.model.WebResponse

interface RegistrationRepo {

    fun getRegistrationPub(onResult: (RegistrationPub) -> Unit, onError: (String) -> Unit)
    fun getRegistrationOrg(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit)
    fun babyCheck(onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun checkMom(onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun elderlyCheck(onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun babyRegistration(babyNames: String, fathersName: String, mothersName: String, dateOfBirth: String, gender: String, familyCard: String, populationIdentificationNumber: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun motherRegistration(mothersName: String, husbandsName: String, age: String, populationIdentificationNumber: String, familyCard: String, healthInsuranceCardNumber: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun elderlyRegistration(elderlyName: String, age: String, gender: String, populationIdentificationNumber: String, familyCard: String, healthInsuranceCardNumber: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun registrationPub(baby: String, mother: String, pregnantMother: String, elderly: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun registrationOrg(onResult: (RegistrationOrg) -> Unit, onError: (String) -> Unit)
    fun changeStatusSchedule(id: String, status: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
    fun changeStatusRegistration(id: String, status: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit)
}