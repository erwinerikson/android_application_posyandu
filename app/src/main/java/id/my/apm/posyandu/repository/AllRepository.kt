package id.my.apm.posyandu.repository

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import id.my.apm.posyandu.domain.AllRepo
import id.my.apm.posyandu.model.CheckHistory
import id.my.apm.posyandu.model.DataCheck
import id.my.apm.posyandu.model.DataImmunization
import id.my.apm.posyandu.model.DataMedication
import id.my.apm.posyandu.model.DataMidwife
import id.my.apm.posyandu.model.HistoryPub
import id.my.apm.posyandu.model.MedicationHistory
import id.my.apm.posyandu.model.SchedulePub
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.utils.SessionManager
import javax.inject.Inject

class AllRepository @Inject constructor(@ApplicationContext context: Context) : AllRepo {

    private val requestQueue = Volley.newRequestQueue(context)
    val auth = SessionManager(context).getAuthData()

    override fun getCheckHistory(kode: String, id: String, onResult: (ArrayList<CheckHistory>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/allpub-get-check.php?kode=${kode}&id=$id"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<CheckHistory>>() {}.type
                        val checkList: ArrayList<CheckHistory> = Gson().fromJson(response, listType)
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

    override fun getMedicationHistory(kode: String, id: String, onResult: (ArrayList<MedicationHistory>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/allpub-get-medication.php?kode=${kode}&id=$id"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<MedicationHistory>>() {}.type
                        val medicationList: ArrayList<MedicationHistory> = Gson().fromJson(response, listType)
                        onResult(medicationList)
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

    override fun getHistoryPub(user: String, onResult: (ArrayList<HistoryPub>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/allpub-get-history.php?user=${user}"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<HistoryPub>>() {}.type
                        val historyList: ArrayList<HistoryPub> = Gson().fromJson(response, listType)
                        onResult(historyList)
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

    override fun getSchedulePub(onResult: (ArrayList<SchedulePub>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/allpub-get-schedule.php"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<SchedulePub>>() {}.type
                        val scheduleList: ArrayList<SchedulePub> = Gson().fromJson(response, listType)
                        onResult(scheduleList)
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

    override fun getDataImmunization(kode: String, onResult: (ArrayList<DataImmunization>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/get-immunization-data.php?kode=${kode}"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<DataImmunization>>() {}.type
                        val immunizationList: ArrayList<DataImmunization> = Gson().fromJson(response, listType)
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

    override fun getAllDataImmunization(onResult: (ArrayList<DataImmunization>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/get-immunization-alldata.php"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<DataImmunization>>() {}.type
                        val immunizationList: ArrayList<DataImmunization> = Gson().fromJson(response, listType)
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

    override fun getDataCheck(onResult: (ArrayList<DataCheck>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/get-check-data.php"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<DataCheck>>() {}.type
                        val checkList: ArrayList<DataCheck> = Gson().fromJson(response, listType)
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

    override fun getDataMedication(onResult: (ArrayList<DataMedication>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/get-medication-data.php"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<DataMedication>>() {}.type
                        val medicationList: ArrayList<DataMedication> = Gson().fromJson(response, listType)
                        onResult(medicationList)
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

    override fun getDataMidwife(onResult: (ArrayList<DataMidwife>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/get-midwife-data.php"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<DataMidwife>>() {}.type
                        val midwifeList: ArrayList<DataMidwife> = Gson().fromJson(response, listType)
                        onResult(midwifeList)
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

    override fun saveImmunization(idUser: String, idBayi: String, idImun: String, berat: String, tinggi: String, idBidan: String, harga: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/save-immunization.php"

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
                params["id_user"] = idUser
                params["id_bayi"] = idBayi
                params["id_imun"] = idImun
                params["berat"] = berat
                params["tinggi"] = tinggi
                params["id_bidan"] = idBidan
                params["harga"] = harga
                params["pemroses"] = auth?.id.toString()
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun saveImmunPregnant(idUser: String, idIbu: String, idImun: String, idBidan: String, harga: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/save-immunization-pregnant.php"

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
                params["id_user"] = idUser
                params["id_ibu"] = idIbu
                params["id_imun"] = idImun
                params["id_bidan"] = idBidan
                params["harga"] = harga
                params["pemroses"] = auth?.id.toString()
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun saveCheck(idUser: String, idPasien: String, idPeriksa: String, berat: String, tinggi: String, tensi: String, hasil: String, idBidan: String, harga: String, kode: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/save-check.php"

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
                params["id_user"] = idUser
                params["id_pasien"] = idPasien
                params["id_periksa"] = idPeriksa
                params["berat"] = berat
                params["tinggi"] = tinggi
                params["tensi"] = tensi
                params["hasil"] = hasil
                params["id_bidan"] = idBidan
                params["harga"] = harga
                params["kode"] = kode
                params["pemroses"] = auth?.id.toString()
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun saveMedication(idUser: String, idPasien: String, idObat: String, qty: String, kode: String, idBidan: String, harga: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/save-medication.php"

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
                params["id_user"] = idUser
                params["id_pasien"] = idPasien
                params["id_obat"] = idObat
                params["qty"] = qty
                params["kode"] = kode
                params["id_bidan"] = idBidan
                params["harga"] = harga
                params["pemroses"] = auth?.id.toString()
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun saveFinance(kdTrans: String, kdSumber: String, desc: String, sumber: String, nominal: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/save-finance.php"

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
                params["kd_trans"] = kdTrans
                params["kd_sumber"] = kdSumber
                params["desc"] = desc
                params["sumber"] = sumber
                params["nominal"] = nominal
                params["pemroses"] = auth?.id.toString()
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun saveDataCheck(nama: String, harga: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/save-data-check.php"

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
                params["nama"] = nama
                params["harga"] = harga
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun saveDataMidwife(nama: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/save-data-midwife.php"

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
                params["nama"] = nama
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun saveDataImmunization(nama: String, harga: String, kode: String, ket: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/save-data-immunization.php"

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
                params["nama"] = nama
                params["harga"] = harga
                params["kode"] = kode
                params["ket"] = ket
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun saveDataMedication(nama: String, satuan: String, harga: String, stok: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/save-data-medication.php"

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
                params["nama"] = nama
                params["satuan"] = satuan
                params["harga"] = harga
                params["stok"] = stok
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun editSchedule(tgl: String, ket: String, status: String, id: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/edit_schedule.php"

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
                params["tgl"] = tgl
                params["ket"] = ket
                params["status"] = status
                params["id"] = id
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun saveSchedule(tgl: String, ket: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/save-schedule.php"

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
                params["tgl"] = tgl
                params["ket"] = ket
                return params
            }
        }
        requestQueue.add(stringRequest)
    }
}