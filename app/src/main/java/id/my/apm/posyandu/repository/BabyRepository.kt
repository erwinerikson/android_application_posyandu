package id.my.apm.posyandu.repository

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import id.my.apm.posyandu.domain.BabyRepo
import id.my.apm.posyandu.model.BabyPub
import id.my.apm.posyandu.model.ChildImmunization
import id.my.apm.posyandu.model.RegistrationPub
import id.my.apm.posyandu.utils.SessionManager
import java.lang.reflect.Type
import javax.inject.Inject

class BabyRepository @Inject constructor(@ApplicationContext context: Context) : BabyRepo {

    private val requestQueue = Volley.newRequestQueue(context)
    val auth = SessionManager(context).getAuthData()

    override fun getBabyOrg(onResult: (ArrayList<RegistrationPub>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/babyorg-get-all.php"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<RegistrationPub>>() {}.type
                        val babiesList: ArrayList<RegistrationPub> = Gson().fromJson(response, listType)
                        onResult(babiesList)
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

    override fun checkBabyPub(onResult: (BabyPub) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/babypub-check-data.php?user=" + auth?.id.toString()

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val resp = Gson().fromJson(response, BabyPub::class.java)
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
                //headers["Authorization"] = "salah"
                return headers
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun getBabyPub(id: String, onResult: (BabyPub) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/babypub-get-baby.php?id=$id"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val resp = Gson().fromJson(response, BabyPub::class.java)
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

    /*override fun getBabyImmunization(idBaby: String, onResult: (ArrayList<ChildImmunization>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/baby-get-immunization.php?id=$idBaby"

        requestQueue.fetchDataApi<ArrayList<ChildImmunization>>(
            url,
            auth?.token.toString(),
            onResult,
            onError
        )
    }*/

    override fun getBabiesPub(idUser: String, onResult: (ArrayList<BabyPub>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/babypub-get-babies.php?user=$idUser"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<BabyPub>>() {}.type
                        val babyList: ArrayList<BabyPub> = Gson().fromJson(response, listType)
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

    override fun getBabyImmunization(idBaby: String, onResult: (ArrayList<ChildImmunization>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/baby-get-immunization.php?id=$idBaby"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<ChildImmunization>>() {}.type
                        val babyList: ArrayList<ChildImmunization> = Gson().fromJson(response, listType)
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




    // ini percobaan pertama
    /*override fun getBabiesPub(onResult: (ArrayList<BabyPub>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/babypub-get-babies.php?user=" + auth?.id.toString()

        requestQueue.fetchDataApi<ArrayList<BabyPub>>(
            url = url,
            authToken = auth?.token,
            onResult,
            onError
        )
    }*/

    inline fun <reified T> RequestQueue.fetchDataApi(
        url: String,
        authToken: String?,
        crossinline onResult: (T) -> Unit,
        crossinline onError: (String) -> Unit
    ) {
        val mainHandler = Handler(Looper.getMainLooper())
        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        // listType otomatis mendeteksi tipe data T berkat keyword 'reified'
                        val typeToken: Type = object : TypeToken<T>() {}.type
                        val parsedData: T = Gson().fromJson(response, typeToken)
                        //onResult(parsedData)
                        mainHandler.post { onResult(parsedData) }
                    } else {
                        //onError("Empty or invalid response")
                        mainHandler.post { onError("Empty or invalid response") }
                    }
                } catch (e: Exception) {
                    //onError(e.message ?: "Unknown parsing error")
                    mainHandler.post { onError(e.message ?: "Unknown parsing error") }
                }
            },
            { error ->
                //onError(error.message ?: "Unknown Error")
                mainHandler.post { onError(error.message ?: "Unknown Error") }
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = authToken.toString()
                return headers
            }
        }
        this.add(stringRequest)
    }
}