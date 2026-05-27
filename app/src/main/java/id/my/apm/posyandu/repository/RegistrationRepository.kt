package id.my.apm.posyandu.repository

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import id.my.apm.posyandu.domain.RegistrationRepo
import id.my.apm.posyandu.model.RegistrationOrg
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.utils.SessionManager
import javax.inject.Inject

class RegistrationRepository @Inject constructor(@ApplicationContext context: Context) : RegistrationRepo {
    private val requestQueue = Volley.newRequestQueue(context)
    val auth = SessionManager(context).getAuthData()

    override fun getRegistrationPub(onResult: (RegistrationPub) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/reg-pub.php?user=" + auth?.id.toString()

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, RegistrationPub::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }

            /*@Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["user"] = auth?.id.toString()
                return params
            }*/
        }
        requestQueue.add(stringRequest)
    }

    override fun registrationOrg(onResult: (RegistrationOrg) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/reg-check.php"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, RegistrationOrg::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun changeStatusSchedule(id: String, status: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/change-status-schedule.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, WebResponse::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = id
                params["status"] = status
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun changeStatusRegistration(id: String, status: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/change-status-registration.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, WebResponse::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = id
                params["status"] = status
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun getRegistrationOrg(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/reg-get-all.php"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<RegistrationPub>>() {}.type
                        val registrationList: ArrayList<RegistrationPub> = Gson().fromJson(response, listType)
                        onResult(registrationList)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun babyCheck(onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/cek-bayi.php?user=" + auth?.id.toString()

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, WebResponse::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun checkMom(onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/cek-ibu.php?user=" + auth?.id.toString()

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, WebResponse::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun elderlyCheck(onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/cek-lansia.php?user=" + auth?.id.toString()

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, WebResponse::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun babyRegistration(babyNames: String, fathersName: String, mothersName: String, dateOfBirth: String, gender: String, familyCard: String, populationIdentificationNumber: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/pendaftaran-bayi.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, WebResponse::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["user"] = auth?.id.toString()
                params["babyNames"] = babyNames
                params["fathersName"] = fathersName
                params["mothersName"] = mothersName
                params["dateOfBirth"] = dateOfBirth
                params["gender"] = gender
                params["kk"] = familyCard
                params["nik"] = populationIdentificationNumber
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun motherRegistration(mothersName: String, husbandsName: String, age: String, populationIdentificationNumber: String, familyCard: String, healthInsuranceCardNumber: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/pendaftaran-ibu.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, WebResponse::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["user"] = auth?.id.toString()
                params["mothersName"] = mothersName
                params["husbandsName"] = husbandsName
                params["age"] = age
                params["nik"] = populationIdentificationNumber
                params["kk"] = familyCard
                params["bpjs"] = healthInsuranceCardNumber
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun elderlyRegistration(elderlyName: String, age: String, gender: String, populationIdentificationNumber: String, familyCard: String, healthInsuranceCardNumber: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/pendaftaran-lansia.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, WebResponse::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["user"] = auth?.id.toString()
                params["elderlyName"] = elderlyName
                params["gender"] = gender
                params["age"] = age
                params["nik"] = populationIdentificationNumber
                params["kk"] = familyCard
                params["bpjs"] = healthInsuranceCardNumber
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun registrationPub(baby: String, mother: String, pregnantMother: String, elderly: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/pendaftaran-pub.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, WebResponse::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["user"] = auth?.id.toString()
                params["baby"] = baby
                params["mother"] = mother
                params["pregnantMother"] = pregnantMother
                params["elderly"] = elderly
                return params
            }
        }
        requestQueue.add(stringRequest)
    }
}