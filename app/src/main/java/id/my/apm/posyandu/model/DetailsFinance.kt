package id.my.apm.posyandu.model

import com.google.gson.annotations.SerializedName

data class DetailsFinance(
    val medication: String,
    val immunization: String,
    val check: String,
    @SerializedName("process_in")
    val processIn: String,
    @SerializedName("process_out")
    val processOut: String,
    @SerializedName("total_in")
    val totalIn: String,
    @SerializedName("total_out")
    val totalOut: String,
    val message: String,
    val error: Boolean
)
