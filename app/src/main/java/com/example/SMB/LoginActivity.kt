package com.example.SMB

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        var btRegister = findViewById<Button>(R.id.btRegister)
        var btLogin = findViewById<Button>(R.id.btLogin)
        var etEmail = findViewById<EditText>(R.id.etEmail)
        var etPass = findViewById<EditText>(R.id.etPass)

        btRegister.setOnClickListener {
            auth.createUserWithEmailAndPassword(etEmail.text.toString(),
                etPass.text.toString()
                ).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(this, "User registered", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else{
                        Toast.makeText(this, "Registration fail", Toast.LENGTH_SHORT).show()


                    }
            }
        }

        btLogin.setOnClickListener {
            auth.signInWithEmailAndPassword(etEmail.text.toString(),
                etPass.text.toString()
            ).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "User Log in", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else{
                    Toast.makeText(this, "Log in fail", Toast.LENGTH_SHORT).show()


                }
            }
        }



    }
}