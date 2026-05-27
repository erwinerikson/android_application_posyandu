package id.my.apm.posyandu.utils

import android.content.Context
import com.google.gson.Gson
import id.my.apm.posyandu.model.AuthData
import androidx.core.content.edit

class SessionManager(context: Context) {
    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveAuthData(response: AuthData?) {
        val jsonString = gson.toJson(response) // Ubah objek ke String JSON
        prefs.edit { putString("user_data", jsonString) }
    }

    fun getAuthData(): AuthData? {
        val jsonString = prefs.getString("user_data", null)
        return if (jsonString != null) {
            gson.fromJson(jsonString, AuthData::class.java) // Ubah JSON ke Objek
        } else {
            null
        }
    }

    fun editAuthData(nama: String, alamat: String, telp: String, userType: String, token: String) {
        prefs.edit().apply {
            putString("nama", nama)
            putString("alamat", alamat)
            putString("telp", telp)
            putString("userType", userType)
            putString("token", token)
            apply()
        }
    }

    fun logout() {
        prefs.edit().apply {
            // Menghapus data spesifik LoginResponse
            remove("user_data")
            // ATAU jika ingin menghapus SEMUA data (token, setting, dll)
            clear()
            apply()
        }
    }
}