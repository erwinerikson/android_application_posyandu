package id.my.apm.posyandu.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.my.apm.posyandu.model.AuthData
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.use_case.registration.BabyCheckUseCase
import id.my.apm.posyandu.use_case.registration.BabyRegistrationUseCase
import id.my.apm.posyandu.use_case.registration.CheckMomUseCase
import id.my.apm.posyandu.use_case.registration.ElderlyCheckUseCase
import id.my.apm.posyandu.use_case.registration.ElderlyRegistrationUseCase
import id.my.apm.posyandu.use_case.registration.GetRegistrationPubUseCase
import id.my.apm.posyandu.use_case.registration.MotherRegistrationUseCase
import id.my.apm.posyandu.use_case.registration.RegistrationPubUseCase
import id.my.apm.posyandu.utils.SessionManager

class RegistrationPubViewModel(
    private val getRegistrationPubUseCase: GetRegistrationPubUseCase,
    private val babyCheckUseCase: BabyCheckUseCase,
    private val checkMomUseCase: CheckMomUseCase,
    private val elderlyCheckUseCase: ElderlyCheckUseCase,
    private val babyRegistrationUseCase: BabyRegistrationUseCase,
    private val motherRegistrationUseCase: MotherRegistrationUseCase,
    private val elderlyRegistrationUseCase: ElderlyRegistrationUseCase,
    private val registrationPubUseCase: RegistrationPubUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _responseGet = MutableLiveData<AuthData>()
    val responseGet: LiveData<AuthData> = _responseGet

    private val _errorGet = MutableLiveData<Boolean>()
    val errorGet: LiveData<Boolean> = _errorGet

    private val _responseRegistrationPub = MutableLiveData<RegistrationPub>()
    val responseRegistrationPub: LiveData<RegistrationPub> = _responseRegistrationPub

    private val _responseBabyCheck = MutableLiveData<WebResponse>()
    val responseBabyCheck: LiveData<WebResponse> = _responseBabyCheck

    private val _responseCheckMom = MutableLiveData<WebResponse>()
    val responseCheckMom: LiveData<WebResponse> = _responseCheckMom

    private val _responseCheckPregnant = MutableLiveData<WebResponse>()
    val responseCheckPregnant: LiveData<WebResponse> = _responseCheckPregnant

    private val _responseElderlyCheck = MutableLiveData<WebResponse>()
    val responseElderlyCheck: LiveData<WebResponse> = _responseElderlyCheck

    private val _responseBabyReg = MutableLiveData<WebResponse>()
    val responseBabyReg: LiveData<WebResponse> = _responseBabyReg

    private val _responseMotherReg = MutableLiveData<WebResponse>()
    val responseMotherReg: LiveData<WebResponse> = _responseMotherReg

    private val _responseElderlyReg = MutableLiveData<WebResponse>()
    val responseElderlyReg: LiveData<WebResponse> = _responseElderlyReg

    private val _responseRegistration = MutableLiveData<WebResponse>()
    val responseRegistration: LiveData<WebResponse> = _responseRegistration

    private val _errorRegistrationPub = MutableLiveData<Boolean>()
    val errorRegistrationPub: LiveData<Boolean> = _errorRegistrationPub

    private val _errorRegistrationPubReg = MutableLiveData<Boolean>()
    val errorRegistrationPubReg: LiveData<Boolean> = _errorRegistrationPubReg

    private val _errorRegistration = MutableLiveData<Boolean>()
    val errorRegistration: LiveData<Boolean> = _errorRegistration

    fun getSession() {
        val auth = sessionManager.getAuthData()
        if (auth != null) {
            _responseGet.postValue(auth)
            _errorGet.postValue(false)
        } else {
            _errorGet.postValue(true)
        }
    }

    fun getData() {
        getRegistrationPubUseCase(
            onResult = { resp ->
                _responseRegistrationPub.postValue(resp)
                _errorRegistrationPub.postValue(false)
            },
            onError = {
                _errorRegistrationPub.postValue(true)
            }
        )
    }

    fun cekBayi() {
        babyCheckUseCase(
            onResult = { resp ->
                _responseBabyCheck.postValue(resp)
                _errorRegistrationPub.postValue(false)
            },
            onError = {
                _errorRegistrationPub.postValue(true)
            }
        )
    }

    fun cekIbu() {
        checkMomUseCase(
            onResult = { resp ->
                _responseCheckMom.postValue(resp)
                _errorRegistrationPub.postValue(false)
            },
            onError = {
                _errorRegistrationPub.postValue(true)
            }
        )
    }

    fun cekIbuHamil() {
        checkMomUseCase(
            onResult = { resp ->
                _responseCheckPregnant.postValue(resp)
                _errorRegistrationPub.postValue(false)
            },
            onError = {
                _errorRegistrationPub.postValue(true)
            }
        )
    }

    fun cekLansia() {
        elderlyCheckUseCase(
            onResult = { resp ->
                _responseElderlyCheck.postValue(resp)
                _errorRegistrationPub.postValue(false)
            },
            onError = {
                _errorRegistrationPub.postValue(true)
            }
        )
    }

    fun pendaftaranBayi(babyNames: String, fathersName: String, mothersName: String, dateOfBirth: String, gender: String, familyCard: String, populationIdentificationNumber: String) {
        babyRegistrationUseCase(babyNames, fathersName, mothersName, dateOfBirth, gender, familyCard, populationIdentificationNumber,
            onResult = { resp ->
                _responseBabyReg.postValue(resp)
                _errorRegistrationPubReg.postValue(false)
            },
            onError = {
                _errorRegistrationPubReg.postValue(true)
            }
        )
    }

    fun pendaftaranIbu(mothersName: String, husbandsName: String, age: String, populationIdentificationNumber: String, familyCard: String, healthInsuranceCardNumber: String) {
        motherRegistrationUseCase(mothersName, husbandsName, age, populationIdentificationNumber, familyCard, healthInsuranceCardNumber,
            onResult = { resp ->
                _responseMotherReg.postValue(resp)
                _errorRegistrationPubReg.postValue(false)
            },
            onError = {
                _errorRegistrationPubReg.postValue(true)
            }
        )
    }

    fun pendaftaranLansia(elderlyName: String, age: String, gender: String, populationIdentificationNumber: String, familyCard: String, healthInsuranceCardNumber: String) {
        elderlyRegistrationUseCase(elderlyName, age, gender, populationIdentificationNumber, familyCard, healthInsuranceCardNumber,
            onResult = { resp ->
                _responseElderlyReg.postValue(resp)
                _errorRegistrationPubReg.postValue(false)
            },
            onError = {
                _errorRegistrationPubReg.postValue(true)
            }
        )
    }

    fun pendaftaran(baby: String, mother: String, pregnantMother: String, elderly: String) {
        registrationPubUseCase(baby, mother, pregnantMother, elderly,
            onResult = { resp ->
                _responseRegistration.postValue(resp)
                _errorRegistration.postValue(false)
            },
            onError = {
                _errorRegistration.postValue(true)
            }
        )
    }
}