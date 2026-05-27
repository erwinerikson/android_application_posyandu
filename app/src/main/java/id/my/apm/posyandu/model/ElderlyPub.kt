package id.my.apm.posyandu.model

import com.google.gson.annotations.SerializedName

data class ElderlyPub(
    val id: String,
    val nama: String,
    @SerializedName("tahun_lahir")
    val tahunLahir: String,
    val jk: String,
    val nik: String,
    val kk: String,
    val bpjs: String,
    val message: String,
    val error: Boolean,
)
