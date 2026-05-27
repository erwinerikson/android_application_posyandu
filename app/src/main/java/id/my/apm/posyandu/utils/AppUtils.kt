package id.my.apm.posyandu.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.my.apm.posyandu.R
import java.lang.reflect.Type
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

object AppUtils {

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Mendapatkan jaringan aktif saat ini
        val network = connectivityManager.activeNetwork ?: return false

        // Mendapatkan kapabilitas jaringan aktif
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Cek Wi-Fi
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            // Cek Data Seluler
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            // Cek Ethernet
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun progressDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        //dialog.setCancelable(false)
        dialog.window?.attributes?.windowAnimations = R.style.AnimationDialog
        dialog.window!!.setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
    }

    inline fun <reified T> RequestQueue.fetchDataApi(
        url: String,
        authToken: String?,
        crossinline onResult: (T) -> Unit,
        crossinline onError: (String) -> Unit
    ) {
        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        // listType otomatis mendeteksi tipe data T berkat keyword 'reified'
                        val typeToken: Type = object : TypeToken<T>() {}.type
                        val parsedData: T = Gson().fromJson(response, typeToken)
                        onResult(parsedData)
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
                headers["Authorization"] = authToken.toString()
                return headers
            }
        }
        this.add(stringRequest)
    }

    fun checkEt(et: EditText, view: View, message: String) {
        et.error = message
        et.requestFocus()
        view.isClickable =true
    }

    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun datePicker(context: Context, tv: TextView) {
        //val indonesianLocale = Locale.of("id", "ID")
        //val indonesianLocale = Locale.forLanguageTag("id-ID").toLanguageTag()
        val indonesianLocale = Locale.forLanguageTag("id-ID")
        // "EEEE, dd MMMM yyyy" = Senin, 01 Januari 2024
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", indonesianLocale)
        //val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", indonesianLocale)
        //val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        //    .withLocale(Locale("id", "ID"))
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context, { _, year, month, dayOfMonth ->
                // Bulan dimulai dari 0 (Januari = 0), jadi ditambah 1
                //val tanggalDipilih = "$dayOfMonth/${month + 1}/$year"
                calendar.set(year, month, dayOfMonth)
                //tv.text = tanggalDipilih
                tv.text = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    fun formatterDateNumber(date: String): String {
        val dateInput = changeStringToDate(date)
        val indonesianLocale = Locale.forLanguageTag("id-ID")
        val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy", indonesianLocale)
        return dateInput.format(dateFormat)
    }

    fun formatterDateLetter(date: String): String {
        val dateInput = changeStringToDate(date)
        val indonesianLocale = Locale.forLanguageTag("id-ID")
        val dateFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy", indonesianLocale)
        return dateInput.format(dateFormat)
    }

    fun formatterDateDay(date: String): String {
        val dateInput = changeStringToDate(date)
        val indonesianLocale = Locale.forLanguageTag("id-ID")
        val dateFormat = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", indonesianLocale)
        return dateInput.format(dateFormat)
    }

    fun calculateAge(date: String): String {
        val dateOfBirth = changeStringToDate(date)
        val dateNow = LocalDate.now()
        val difference = Period.between(dateOfBirth, dateNow)
        val years = difference.years
        val months = difference.months
        val days = difference.days
        val age = when (years) {
            0 if months == 0 -> {
                "$days hari"
            }
            0 -> {
                "$months bulan $days hari"
            }
            else -> {
                "$years tahun $months bulan $days hari"
            }
        }

        return age
    }

    fun changeStringToDate(date: String): LocalDate {
        val intYear = Integer.parseInt(date.substring(0, 4))
        val intMonth = Integer.parseInt(date.substring(5, 7))
        val intDay = Integer.parseInt(date.substring(8))
        return LocalDate.of(intYear, intMonth, intDay)
    }

    fun formatCurrency(amount: String): String {
        var amountCurrency = ""
        val kurs: DecimalFormat = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val format = DecimalFormatSymbols()
        format.currencySymbol = ""
        format.monetaryDecimalSeparator = ','
        format.groupingSeparator = '.'
        kurs.decimalFormatSymbols = format
        try {
            amountCurrency = kurs.format(amount.toDouble())
        } catch (_: Exception) {
            amountCurrency = "0,00"
        }
        return amountCurrency
    }

    fun formatCurrencySymbol(amount: String): String {
        var amountCurrency = ""
        val kursInd: DecimalFormat = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val formatRp = DecimalFormatSymbols()
        formatRp.currencySymbol = "Rp. "
        formatRp.monetaryDecimalSeparator = ','
        formatRp.groupingSeparator = '.'
        kursInd.decimalFormatSymbols = formatRp
        try {
            amountCurrency = kursInd.format(amount.toDouble())
        } catch (_: Exception) {
            amountCurrency = "Rp. 0,00"
        }
        return amountCurrency
    }
}