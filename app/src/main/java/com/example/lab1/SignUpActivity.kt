package com.example.lab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.lab1.databinding.ActivitySignUpBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("students")
        binding.signUpButton.setOnClickListener{
            val signupname= binding.signupname.text.toString()
            val signupage = binding.signupage.text.toString()
            val signupaddress = binding.signupaddress.text.toString()
            val signupemail = binding.signupemail.text.toString()
            if(signupname.isNotEmpty() && signupage.isNotEmpty() && signupaddress.isNotEmpty() && signupemail.isNotEmpty())
            {
                signupUser(signupname,signupage,signupaddress,signupemail)
            }
            else
            {
                Toast.makeText(this@SignUpActivity, "All Fields Are Mandatory",Toast.LENGTH_SHORT).show()
            }
        }
        // setContentView(R.layout.activity_sign_up)
    }

    private fun signupUser(studentname: String, studentage: String, studentaddress: String, studentemail: String)
    {
        databaseReference.orderByChild("studentname").equalTo(studentname).addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                if(!dataSnapshot.exists())
                {
                    val id = databaseReference.push().key
                    val userData = UserData(id,studentname,studentage,studentaddress,studentemail)
                    databaseReference.child(id!!).setValue(userData)
                    Toast.makeText(this@SignUpActivity, "Signup Successful",Toast.LENGTH_SHORT).show()
                    //                    startActivity(Intent(this@SignUpActivity,LoginActivity:: class.Java))
                    finish()
                }
                else{
                    Toast.makeText(this@SignUpActivity, "Student Already Exists",Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(databaseError: DatabaseError){
                Toast.makeText(this@SignUpActivity, "Datbase Error: ${databaseError.message}",Toast.LENGTH_SHORT).show()
            }
        })
    }

}