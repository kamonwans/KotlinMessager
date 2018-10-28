package com.example.kamonwan_s.kotlinmessager.registerlogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.kamonwan_s.kotlinmessager.LatestMessagesActivity
import com.example.kamonwan_s.kotlinmessager.R
import com.example.kamonwan_s.kotlinmessager.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            performRegister()
        }
        tvAlreadyHaveAnAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnSelectPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)
        }
    }

    var selectedPhotoUri : Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 &&resultCode ==  Activity.RESULT_OK && data != null){
            // proceed and check what the select image was ...

           selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUri)
            imgSelectPhoto.setImageBitmap(bitmap)
            btnSelectPhoto.alpha = 0f
//            val bitmapDrawable = BitmapDrawable(bitmap)
//            btnSelectPhoto.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister() {
        val email = editEmail.text.toString()
        val password = editPassword.text.toString()
        val username = editUsername.text.toString()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please enter text in email or password",Toast.LENGTH_SHORT).show()
            return
        }
        Log.d("RegisterActivity","email : " + email)
        Log.d("RegisterActivity","password : $password")
        //Firebase Auth
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{
                    if  (!it.isSuccessful) return@addOnCompleteListener
                    //else if success
                    Log.d("RegisterActivity","Success create user with : ${it.result.user.uid}")

                    uploadImageToFirebaseStorage()
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed to create user : ${it.message}",Toast.LENGTH_SHORT).show()
                    Log.d("RegisterActivity","Failed to create user : ${it.message}")
                }
    }

    private fun uploadImageToFirebaseStorage() {

        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("Register","Successfully upload image : ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        it.toString()
                        Log.d("Register","File Location: $it")

                        saveToFirebaseDatabase(it.toString())
                    }
                }
                .addOnFailureListener{
                    // do some login here
                }
    }

    private fun saveToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid,editUsername.text.toString(),profileImageUrl)
        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("Register","Finally we save the user")

                    val intent = Intent(this, LatestMessagesActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }
    }
}
