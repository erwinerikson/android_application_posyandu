package id.my.apm.posyandu.use_case.registration

import id.my.apm.posyandu.domain.RegistrationRepo
import id.my.apm.posyandu.model.WebResponse

class BabyRegistrationUseCase(private val repository: RegistrationRepo) {
    operator fun invoke(babyNames: String, fathersName: String, mothersName: String, dateOfBirth: String, gender: String, familyCard: String, populationIdentificationNumber: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.babyRegistration(babyNames, fathersName, mothersName, dateOfBirth, gender, familyCard, populationIdentificationNumber, onResult, onError)
    }
}