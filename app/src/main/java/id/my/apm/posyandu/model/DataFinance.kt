package id.my.apm.posyandu.model

import com.google.gson.annotations.SerializedName

data class DataFinance(
    val id: String,
    val tgl: String,
    @SerializedName("kode_trans")
    val kodeTrans: String,
    @SerializedName("kode_sumber")
    val kodeSumber: String,
    val desc: String,
    val sumber: String,
    val pemroses: String,
    val nominal: String,
    val status: String,
    val message: String,
    val error: Boolean,
)
