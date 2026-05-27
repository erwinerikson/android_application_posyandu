package id.my.apm.posyandu.repository

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import id.my.apm.posyandu.domain.MotherRepo
import id.my.apm.posyandu.model.MotherCheck
import id.my.apm.posyandu.model.MotherPub
import id.my.apm.posyandu.model.PregnantImmunization
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.utils.SessionManager
import javax.inject.Inject

class MotherRepository @Inject constructor(@ApplicationContext context: Context) : MotherRepo {

    private val requestQueue = Volley.newRequestQueue(context)
    val auth = SessionManager(context).getAuthData()

    override fun getMotherOrg(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/motherorg-get-all.php"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<RegistrationPub>>() {}.type
                        val motherList: ArrayList<RegistrationPub> = Gson().fromJson(response, listType)
                        onResult(motherList)
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

    override fun getPregnantOrg(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/pregnantorg-get-all.php"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<RegistrationPub>>() {}.type
                        val pregnantList: ArrayList<RegistrationPub> = Gson().fromJson(response, listType)
                        onResult(pregnantList)
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

    override fun getMotherPub(user: String, onResult: (MotherPub) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/motherpub-get-mother.php?user=$user"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val resp = Gson().fromJson(response, MotherPub::class.java)
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

    override fun getPregnantPub(user: String, onResult: (MotherPub) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/pregnantpub-get-pregnant.php?user=$user"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val resp = Gson().fromJson(response, MotherPub::class.java)
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

    override fun getMotherCheck(kode: String, user: String, onResult: (ArrayList<MotherCheck>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/mother-get-check.php?kode=${kode}&user=$user"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<MotherCheck>>() {}.type
                        val checkList: ArrayList<MotherCheck> = Gson().fromJson(response, listType)
                        onResult(checkList)
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

    override fun getPregnantImmunization(id: String, onResult: (ArrayList<PregnantImmunization>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/mother-get-immunization.php?id=$id"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<PregnantImmunization>>() {}.type
                        val immunizationList: ArrayList<PregnantImmunization> = Gson().fromJson(response, listType)
                        onResult(immunizationList)
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
}