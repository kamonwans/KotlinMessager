package com.example.kamonwan_s.kotlinmessager.registerlogin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.kamonwan_s.kotlinmessager.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin.setOnClickListener {
            val email = editEmailLogin.text.toString()
            val password = editPasswordLogin.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener{

                    }
        }

        tvBackToRegister.setOnClickListener {
            finish()
        }
    }
}
