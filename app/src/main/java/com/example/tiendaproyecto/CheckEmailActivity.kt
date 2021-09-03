package com.example.tiendaproyecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tiendaproyecto.databinding.ActivityCheckEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class CheckEmailActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityCheckEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //inicializa la variable binding
        binding = ActivityCheckEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth

        val user = auth.currentUser

        //después de que el usuario haya verificado su correo debe hacer clic en continuar
        binding.veficateEmailAppCompatButton.setOnClickListener {
            val profileUpdates = userProfileChangeRequest {  }

            user!!.updateProfile(profileUpdates).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    if(user.isEmailVerified){
                        reload()
                    } else {
                        Toast.makeText(this,"Por favor verifica tu correo de verificación",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.signOutImageView.setOnClickListener {
            signOut()
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            //si el usuario ha verificado su correo se redirreciona al MainActivity
            if (currentUser.isEmailVerified){
                reload()
            } else {
                sendEmailVerification()
            }
        }
    }

    private fun sendEmailVerification() {
        //obtener el usuario
        val user = auth.currentUser
        user!!.sendEmailVerification().addOnCompleteListener(this){ task ->
            //si el correo se envió correctamente
            if (task.isSuccessful) {
                Toast.makeText(this,"Revisa tu correo electrónico, se envió  un correo de verificación",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun reload(){
        val intent = Intent (this, MainActivity::class.java)
        this.startActivity(intent)
    }

    private fun signOut(){
        Firebase.auth.signOut()
        //redireccionar a la actividad inicio de sesión
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }
}