package id.my.apm.posyandu.model

import com.google.gson.annotations.SerializedName

data class MotherPub(
    val id: String,
    val nama: String,
    @SerializedName("tahun_lahir")
    val tahunLahir: String,
    val suami: String,
    val nik: String,
    val kk: String,
    val bpjs: String,
    @SerializedName("tgl_hamil")
    val tglHamil: String?,
    val message: String,
    val error: Boolean,
)
