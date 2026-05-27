package id.my.apm.posyandu.model

import com.google.gson.annotations.SerializedName

data class MedicationHistory(
    val id: String,
    val tgl: String,
    val nama: String,
    val qty: String,
    @SerializedName("id_pasien")
    val idPasien: String,
    val message: String,
    val error: Boolean,
)
