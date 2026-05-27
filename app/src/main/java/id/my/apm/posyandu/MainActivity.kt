package id.my.apm.posyandu

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import id.my.apm.posyandu.ui.user.LoginActivity
import id.my.apm.posyandu.ui.user.OrganizerActivity
import id.my.apm.posyandu.ui.user.PublicActivity
import id.my.apm.posyandu.utils.SessionManager

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sessionManager = SessionManager(this)
        viewModel = MainViewModel(sessionManager)

        viewModel.responseCheck.observe(this) { resp ->
            when (resp?.userType) {
                "1" -> {
                    startActivity(Intent(this, PublicActivity::class.java))
                    finish()
                }
                "2" -> {
                    startActivity(Intent(this, OrganizerActivity::class.java))
                    finish()
                }
                else -> {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.checkSession()
        }, 3000)
    }
}