package id.my.apm.posyandu.use_case.registration

import id.my.apm.posyandu.domain.RegistrationRepo
import id.my.apm.posyandu.model.WebResponse

class ElderlyRegistrationUseCase(private val repository: RegistrationRepo) {
    operator fun invoke(elderlyName: String, age: String, gender: String, populationIdentificationNumber: String, familyCard: String, healthInsuranceCardNumber: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        repository.elderlyRegistration(elderlyName, age, gender, populationIdentificationNumber, familyCard, healthInsuranceCardNumber, onResult, onError)
    }
}