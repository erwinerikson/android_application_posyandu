package id.my.apm.posyandu.model

data class SchedulePub(
    val id: String,
    val tgl: String,
    val ket: String,
    val status: String,
    val message: String,
    val error: Boolean,
)
