package id.my.apm.posyandu.model

import com.google.gson.annotations.SerializedName

data class CheckHistory(
    val id: String,
    val tgl: String,
    val nama: String,
    val tinggi: String,
    val berat: String,
    val tensi: String,
    val hasil: String,
    @SerializedName("nama_bidan")
    val namaBidan: String,
    @SerializedName("id_pasien")
    val idPasien: String,
    val message: String,
    val error: Boolean,
)