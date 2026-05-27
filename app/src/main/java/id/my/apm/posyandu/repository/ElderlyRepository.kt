package id.my.apm.posyandu.repository

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import id.my.apm.posyandu.domain.ElderlyRepo
import id.my.apm.posyandu.model.ElderlyPub
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.utils.SessionManager
import javax.inject.Inject

class ElderlyRepository @Inject constructor(@ApplicationContext context: Context) : ElderlyRepo {

    private val requestQueue = Volley.newRequestQueue(context)
    val auth = SessionManager(context).getAuthData()

    override fun getElderlyOrg(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/elderlyorg-get-all.php"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<RegistrationPub>>() {}.type
                        val elderlyList: ArrayList<RegistrationPub> = Gson().fromJson(response, listType)
                        onResult(elderlyList)
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

    override fun getElderlyPub(id: String, onResult: (ElderlyPub) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/elderlypub-get-elderly.php?id=$id"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val resp = Gson().fromJson(response, ElderlyPub::class.java)
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

    override fun getElderlyDataPub(user: String, onResult: (ArrayList<ElderlyPub>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/elderlypub-get-data.php?user=$user"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<ElderlyPub>>() {}.type
                        val babyList: ArrayList<ElderlyPub> = Gson().fromJson(response, listType)
                        onResult(babyList)
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