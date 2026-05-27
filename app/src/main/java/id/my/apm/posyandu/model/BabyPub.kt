package id.my.apm.posyandu.model

import com.google.gson.annotations.SerializedName

data class BabyPub(
    val id: String,
    @SerializedName("id_user")
    val idUser: String,
    val nama: String,
    @SerializedName("nama_ibu")
    val namaIbu: String,
    @SerializedName("nama_ayah")
    val namaAyah: String,
    @SerializedName("tgl_lahir")
    val tglLahir: String,
    val jk: String,
    val nik: String,
    val kk: String,
    val bpjs: String,
    val status: String,
    val kode: String,
    val message: String,
    val error: Boolean,
)
