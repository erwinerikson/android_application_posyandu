package id.my.apm.posyandu.model

import com.google.gson.annotations.SerializedName

data class RegistrationPub(
    val id: String,
    @SerializedName("id_user")
    val idUser: String,
    val bayi: String,
    val ibu: String,
    val ibuhamil: String,
    val lansia: String,
    val nama: String,
    val ket: String,
    val status: String,
    val antri: String,
    val kode: String,
    val message: String,
    val error: Boolean,
)
