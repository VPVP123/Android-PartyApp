package com.github.skosvall.nextlvl

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.lang.Exception

const val RC_SIGN_IN = 1

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var listOfAdminAccounts: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        listOfAdminAccounts = mutableListOf()

        val accounts = db.collection("adminAccounts").document("accounts")
        val buttonLogin = this.findViewById<Button>(R.id.login)
        val googleLogin = this.findViewById<SignInButton>(R.id.google_sign_in_button)

        accounts.get()
            .addOnSuccessListener { fields ->
                if (fields != null) {
                    val myArray = fields.get("accounts") as List<String>?
                    if (myArray != null) {
                        for (item in myArray) {
                            listOfAdminAccounts.add(item)
                        }
                    }
                } else {
                    Log.d("noExist", "No document found")
                }
                googleLogin.setOnClickListener{
                    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .build()

                    // Build a GoogleSignInClient with the options specified by gso.
                    val mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

                    val signInIntent = mGoogleSignInClient.signInIntent
                    startActivityForResult(signInIntent, RC_SIGN_IN)
                }
            }.addOnFailureListener { exception ->
                Log.d("errorDB", "get failed with ", exception)
            }

        buttonLogin.setOnClickListener{
            val email = findViewById<EditText>(R.id.email) as EditText
            val password = findViewById<EditText>(R.id.password) as EditText

            if(!email.text.toString().isEmpty() || !password.text.toString().isEmpty()){
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            startActivity(
                                Intent(this, AdminPanelActivity::class.java)
                            )
                        } else {
                            val popUpError1 = AlertDialog.Builder(this)
                            popUpError1.setTitle("Login failed")
                            popUpError1.setMessage("The email and/or password you entered is incorrect")
                            popUpError1.setPositiveButton( "Ok") { dialog, which ->
                                dialog.dismiss()
                            }
                            popUpError1.show()
                        }
                    }
            }else{
                val popUpError1 = AlertDialog.Builder(this)
                popUpError1.setTitle("Enter all fields")
                popUpError1.setMessage("The email and password fields cannot be empty")
                popUpError1.setPositiveButton( "Ok") { dialog, which ->
                    dialog.dismiss()
                }
                popUpError1.show()
            }
        }

    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(
                Intent(this, AdminPanelActivity::class.java)
            )
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)

        try{
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            if (account != null){
                val token = account.idToken
                auth.signInWithCredential(GoogleAuthProvider.getCredential(token, null))
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val currentUser = auth.currentUser
                            val currentUserEmail = auth.currentUser.email

                            if(currentUser != null && listOfAdminAccounts.contains(currentUserEmail))
                            startActivity(
                                Intent(this, AdminPanelActivity::class.java)
                            )else{
                                val popUpError1 = AlertDialog.Builder(this)
                                popUpError1.setTitle("Login failed")
                                popUpError1.setMessage("The email and/or password you entered is incorrect")
                                popUpError1.setPositiveButton("Ok") { dialog, which ->
                                    dialog.dismiss()
                                }
                                popUpError1.show()
                                FirebaseAuth.getInstance().signOut()
                            }
                        } else {
                            val popUpError1 = AlertDialog.Builder(this)
                            popUpError1.setTitle("Login failed")
                            popUpError1.setMessage("The email and/or password you entered is incorrect")
                            popUpError1.setPositiveButton("Ok") { dialog, which ->
                                dialog.dismiss()
                            }
                            popUpError1.show()
                        }
                    }
            }
        } catch (exception: Exception){
            Log.d("LOGIN", exception.toString())
        }
    }
}