package com.example.tiendaproyecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tiendaproyecto.databinding.ActivityAccountRecoveryBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountRecoveryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountRecoveryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountRecoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.senEmailAppCompatButton.setOnClickListener {
            //obtener la dirección de correo
            val emailAddress = binding.emailEditText.text.toString()

            //enviar correo para restablecer la contraseña
            Firebase.auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                val intent = Intent(this, SignInActivity::class.java)
                this.startActivity(intent)
                } else {
                    Toast.makeText(
                        this, "Ingrese un correo electrónico de una cuenta válida.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}