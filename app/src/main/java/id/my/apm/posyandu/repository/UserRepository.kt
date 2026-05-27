package id.my.apm.posyandu.repository

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import id.my.apm.posyandu.domain.UserRepo
import id.my.apm.posyandu.model.AuthData
import id.my.apm.posyandu.model.SchedulePub
import id.my.apm.posyandu.model.User
import id.my.apm.posyandu.model.WebResponse
import id.my.apm.posyandu.utils.SessionManager
import javax.inject.Inject

class UserRepository @Inject constructor(@ApplicationContext context: Context) : UserRepo {
    private val requestQueue = Volley.newRequestQueue(context)
    val auth = SessionManager(context).getAuthData()

    override fun register(name: String, address: String, phone: String, username: String, password: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        //val url = "http://localhost/api/register"
        //val url = "http://192.168.1.2/api/register"
        val url = "http://10.0.2.2/api/register.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
             { response ->
                 try {
                     // Parsing JSON (gunakan Gson)
                     val resp = Gson().fromJson(response, WebResponse::class.java)
                     if (resp != null) {
                         onResult(resp)
                     } else {
                         onError("Empty or invalid response")
                     }
                 } catch (e: Exception) {
                     onError(e.message ?: "Unknown parsing error")
                 }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["name"] = name
                params["address"] = address
                params["phone"] = phone
                params["username"] = username
                params["password"] = password
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun login(username: String, password: String, onResult: (AuthData?) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/login.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, AuthData::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun logout(onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/logout.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, WebResponse::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun changePassword(username: String, password: String, passwordNew: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/changePassword.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, WebResponse::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                //headers["Content-Type"] = "application/json"
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = username
                params["password"] = password
                params["passwordNew"] = passwordNew
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun changeAccount(id: String, type: String, onResult: (WebResponse) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/changeAccount.php"

        val stringRequest = object : StringRequest(
            Method.POST, url,
            { response ->
                try {
                    val resp = Gson().fromJson(response, WebResponse::class.java)
                    if (resp != null) {
                        onResult(resp)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["id"] = id
                params["type"] = type
                return params
            }
        }
        requestQueue.add(stringRequest)
    }

    override fun getUserById(id: String, onResult: (User?) -> Unit, onError: (String) -> Unit) {
        val url = "https://example.com"

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                try {
                    // Parsing JSON (gunakan Gson)
                    val user = Gson().fromJson(response, User::class.java)
                    onResult(user)
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        )
        requestQueue.add(stringRequest)
    }

    override fun getUsers(onResult: (ArrayList<User>) -> Unit, onError: (String) -> Unit) {
        val url = "http://10.0.2.2/api/get-users.php"

        val stringRequest = object : StringRequest(
            Method.GET, url,
            { response ->
                try {
                    if (!response.isNullOrEmpty()) {
                        val listType = object : TypeToken<ArrayList<User>>() {}.type
                        val userList: ArrayList<User> = Gson().fromJson(response, listType)
                        onResult(userList)
                    } else {
                        onError("Empty or invalid response")
                    }
                } catch (e: Exception) {
                    onError(e.message ?: "Unknown parsing error")
                }
            },
            { error ->
                onError(error.message ?: "Unknown Error")
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/x-www-form-urlencoded"
                headers["Authorization"] = auth?.token.toString()
                return headers
            }
        }
        requestQueue.add(stringRequest)
    }

    /*
        Menggunakan Gson di Android Studio dengan Kotlin untuk parsing JSON

        1. Tambahkan Dependensi
        dependencies {
            implementation("com.google.code.gson:gson:2.10.1")
        }

        2. Buat Data Class (Model)
        data class User(
            val name: String,
            val email: String,
            val age: Int
        )

        3. Contoh Penggunaan Gson
        A. Konversi JSON String ke Objek Kotlin (Deserialization)
        import com.google.gson.Gson

        val jsonString = "{\"name\":\"Budi\",\"email\":\"budi@example.com\",\"age\":25}"
        val gson = Gson()
        val user = gson.fromJson(jsonString, User::class.java)

        println(user.name) // Output: Budi

        B. Konversi Objek Kotlin ke JSON String (Serialization)
        val newUser = User("Andi", "andi@example.com", 30)
        val jsonOutput = Gson().toJson(newUser)

        println(jsonOutput)
        // Output: {"name":"Andi","email":"andi@example.com","age":30}

        4. Menggunakan Gson dengan List
        Jika JSON berupa array [...], gunakan TypeToken
        import com.google.gson.reflect.TypeToken

        val jsonArray = "[{\"name\":\"Budi\"}, {\"name\":\"Andi\"}]"
        val listType = object : TypeToken<List<User>>() {}.type
        val userList: List<User> = Gson().fromJson(jsonArray, listType)
     */

    /*
        Mengirim JSON Body
        Jika API Anda membutuhkan JSON body (Content-Type: application/json), gunakan JsonObjectRequest daripada StringRequest

        import org.json.JSONObject

        // ... dalam fungsi
        val url = "https://your-api-url.com"
        val jsonBody = JSONObject()
        jsonBody.put("key1", "value1")
        jsonBody.put("key2", "value2")

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response -> Log.d("Volley", "Response: $response") },
            { error -> Log.e("Volley", "Error: $error") }
        )

        Volley.newRequestQueue(this).add(jsonObjectRequest)
     */
}