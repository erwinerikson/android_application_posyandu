package id.my.apm.posyandu.model

data class AuthData(
    val id: String,
    val nama: String,
    val alamat: String,
    val telp: String,
    val username: String,
    val userType: String,
    val token: String,
    val message: String,
    val error: Boolean,
)
