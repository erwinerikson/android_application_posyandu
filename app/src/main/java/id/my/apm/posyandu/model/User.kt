package id.my.apm.posyandu.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: String,
    val nama: String,
    val alamat: String,
    val telp: String,
    val username: String,
    @SerializedName("user_type")
    val userType: String,
)
