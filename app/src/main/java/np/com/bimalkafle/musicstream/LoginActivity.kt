package np.com.bimalkafle.musicstream

import android.content.Intent
import android.util.Log
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import np.com.bimalkafle.musicstream.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        auth.currentUser?.let {
            navigateToMain()
        }
    }

    private fun setupListeners() {
        binding.loginBtn.setOnClickListener {
            val email = binding.emailEdittext.text.toString().trim()
            val password = binding.passwordEdittext.text.toString()

            if (!isValidEmail(email)) {
                binding.emailEdittext.error = "Invalid email"
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                binding.passwordEdittext.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            loginWithFirebase(email, password)
        }

        binding.gotoSignupBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    private fun loginWithFirebase(email: String, password: String) {
        setInProgress(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                setInProgress(false)
                navigateToMain()
            }
            .addOnFailureListener {
                setInProgress(false)
                Log.d("FIREBASE", "${it.message}")
                showToast("Login failed: ${it.message}")
            }
    }

    private fun setInProgress(inProgress: Boolean) {
        binding.progressBar.visibility = if (inProgress) View.VISIBLE else View.GONE
        binding.loginBtn.visibility = if (inProgress) View.GONE else View.VISIBLE
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}
