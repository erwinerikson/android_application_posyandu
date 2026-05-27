package id.my.apm.posyandu.model

import com.google.gson.annotations.SerializedName

data class HistoryPub(
    @SerializedName("created_at")
    val createdAt: String,
    val bayi: String,
    val ibu: String,
    @SerializedName("ibu_hamil")
    val ibuHamil: String,
    val lansia: String,
    val status: String,
    val message: String,
    val error: Boolean,
)
