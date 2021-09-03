package com.example.tiendaproyecto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.tiendaproyecto.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inicializa la variable binding
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth

        //instanciar el componente de inicio de sesión y que al hacer clic en ese componente ejecute la función de inicio de sesión
        binding.signInAppCompatButton.setOnClickListener{
            //obtener los valores del correo y contraseña
            val mEmail = binding.emailEditText.text.toString()
            val mPassword = binding.passwordEditText.text.toString()

            //firebase no acepta valores nulos
            when {
                mEmail.isEmpty() || mPassword.isEmpty()-> {
                    Toast.makeText(baseContext, "Correo electrónico o contraseña incorrecta.",
                        Toast.LENGTH_SHORT).show()
                } else ->{
                SignIn(mEmail, mPassword)
                }
            }
        }

        //redireccionar a la actividad de registro de usuario
        binding.signUpTextView.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        //clic en el texto de olvidaste la contraseña, redirecciona a la actividad account recovery
        binding.recoveryAccountTextView.setOnClickListener {
            val intent = Intent(this, AccountRecoveryActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            if (currentUser.isEmailVerified) {
                reload()
            } else {
                val intent = Intent(this, CheckEmailActivity::class.java)
                startActivity(intent)
            }
        }
    }

    //función para iniciar sesión
    private fun SignIn (email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success")
                    reload()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Correo electrónico o contraseña incorrecta.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun reload(){
        val intent = Intent (this, MainActivity::class.java)
        this.startActivity(intent)
    }
}