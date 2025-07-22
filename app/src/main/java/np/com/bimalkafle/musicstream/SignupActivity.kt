package np.com.bimalkafle.musicstream

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import np.com.bimalkafle.musicstream.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.createAccountBtn.setOnClickListener {
            val email = binding.emailEdittext.text.toString().trim()
            val password = binding.passwordEdittext.text.toString()
            val confirmPassword = binding.confirmPasswordEdittext.text.toString()

            if (!isValidEmail(email)) {
                binding.emailEdittext.error = "Invalid email"
                return@setOnClickListener
            }

            if (!isValidPassword(password)) {
                binding.passwordEdittext.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                binding.confirmPasswordEdittext.error = "Passwords do not match"
                return@setOnClickListener
            }

            createAccount(email, password)
        }

        binding.gotoLoginBtn.setOnClickListener {
            finish()
        }
    }

    private fun createAccount(email: String, password: String) {
        setInProgress(true)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                setInProgress(false)
                showToast("Account created successfully")
                finish()
            }
            .addOnFailureListener {
                setInProgress(false)
                showToast("Account creation failed: ${it.message}")
            }
    }

    private fun setInProgress(inProgress: Boolean) {
        binding.progressBar.visibility = if (inProgress) View.VISIBLE else View.GONE
        binding.createAccountBtn.visibility = if (inProgress) View.GONE else View.VISIBLE
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
