package id.my.apm.posyandu.model

data class DataMedication(
    val id: String,
    val nama: String,
    val satuan: String,
    val harga: String,
    val stok: String,
    val message: String,
    val error: Boolean,
)
