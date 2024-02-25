package com.hassanpial.our_1st_project_of_book_exchange
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("515003874919-pcpt9dk5vc6tduk4cvfokc3mhg3k2asj.apps.googleusercontent.com")
            .requestEmail()
            .build()

        var sign_in_with_google = findViewById<TextView>(R.id.sign_in_with_google)
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        sign_in_with_google.setOnClickListener() {
            signInGoogle()
        }

        var input_email = findViewById<EditText>(R.id.signinemail)
        var input_pass = findViewById<EditText>(R.id.signinpassword)
var loginpb=findViewById<ProgressBar>(R.id.loginpb)

        var login_button = findViewById<Button>(R.id.logintoprofile)
        login_button .setOnClickListener {
            val email = input_email.text.toString().trim()
            val pass = input_pass.text.toString().trim()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                loginpb.visibility=View.VISIBLE
                login_button.visibility=View.INVISIBLE
                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Authentication successful, navigate to the next activity
                        val intent = Intent(this, loggedin_home_page::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Authentication failed
                        loginpb.visibility=View.INVISIBLE
                        login_button.visibility=View.VISIBLE
                        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                            input_email.setError("Enter a valid email address");
                            input_email.requestFocus();

                        } else {
                            val errorCode = (task.exception as? FirebaseAuthException)?.errorCode
                            when (errorCode) {
                                "ERROR_INVALID_EMAIL" -> {
                                    Toast.makeText(
                                        this,
                                        "Invalid email address",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                  "ERROR_WRONG_PASSWORD" -> {
                                   Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show()
                                }
                                "ERROR_USER_NOT_FOUND" -> {
                                 Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
                                 }
                                else -> {
                                    Toast.makeText(this, "User not registered or wrong password", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            } else {
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                Googlelogin(account)
            }
        } else {
            Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun Googlelogin(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        try {
            auth.signInWithCredential(credential).addOnCompleteListener {

                if (it.isSuccessful) {
                    startActivity(Intent(this, loggedin_home_page::class.java))
                } else {
                    Toast.makeText(this,account.email , Toast.LENGTH_SHORT).show()
                }

            }

        }catch (e:Exception){

                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
    }
    }

    private fun login(email: String, pass: String) {
        // calling signInWithEmailAndPassword(email, pass)
        // function using Firebase auth object
        // On successful response Display a Toast
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
       Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_SHORT).show()
            } else {
                // If there is an error, you can get the error message using task.exception?.message
                Toast.makeText(this, "Log In failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}
