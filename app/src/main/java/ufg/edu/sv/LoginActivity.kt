package ufg.edu.sv

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ufg.edu.sv.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Validation logic
            if (username.isEmpty()) {
                binding.tilUsername.error = "Ingrese su usuario"
                return@setOnClickListener
            } else {
                binding.tilUsername.error = null
            }

            if (password.isEmpty()) {
                binding.tilPassword.error = "Ingrese su contraseña"
                return@setOnClickListener
            } else {
                binding.tilPassword.error = null
            }

            if (username == "admin" && password == "12345") {
                // Success - Redirect to Main screen
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Prevent returning to login screen on back press
            } else {
                // Failure
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                binding.tilUsername.error = "Credenciales incorrectas"
                binding.tilPassword.error = "Credenciales incorrectas"
            }
        }
    }
}
