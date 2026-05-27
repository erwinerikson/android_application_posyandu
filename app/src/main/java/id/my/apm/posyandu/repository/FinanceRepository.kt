package id.my.apm.posyandu.repository

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import id.my.apm.posyandu.domain.FinanceRepo
import id.my.apm.posyandu.model.DataFinance
import id.my.apm.posyandu.model.DetailsFinance
import id.my.apm.posyandu.utils.SessionManager
import javax.inject.Inject

class FinanceRepository @Inject constructor(@ApplicationContext context: Context) : FinanceRepo {

    private val requestQueue = Volley.newRequestQueue(context)
    val auth = SessionManager(context).getAuthData()

    override fun getFinance(kodeTrans: String, onResult: (ArrayList<DataFinance>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/get-finance.php?kode=${kodeTrans}"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<DataFinance>>() {}.type
                        val financeList: ArrayList<DataFinance> = Gson().fromJson(response, listType)
                        onResult(financeList)
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

    override fun getDetailsFinance(onResult: (DetailsFinance) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/get-details-finance.php"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val resp = Gson().fromJson(response, DetailsFinance::class.java)
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
}