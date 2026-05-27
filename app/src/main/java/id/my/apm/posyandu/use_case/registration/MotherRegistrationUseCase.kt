package id.my.apm.posyandu.use_case.registration

import id.my.apm.posyandu.domain.RegistrationRepo
import id.my.apm.posyandu.model.WebResponse

class MotherRegistrationUseCase(private val repository: RegistrationRepo) {
    operator fun invoke(mothersName: String, husbandsName: String, age: String, populationIdentificationNumber: String, familyCard: String, healthInsuranceCardNumber: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.motherRegistration(mothersName, husbandsName, age, populationIdentificationNumber, familyCard, healthInsuranceCardNumber, onResult, onError)
    }
}