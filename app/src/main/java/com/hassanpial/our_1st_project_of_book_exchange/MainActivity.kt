package com.hassanpial.our_1st_project_of_book_exchange

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.navigation.ui.AppBarConfiguration
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.hassanpial.our_1st_project_of_book_exchange.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private  lateinit  var auth: FirebaseAuth

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_main)
       // setContentView(binding.root)
        var binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)





        auth = Firebase.auth
        signIn()

        val agreedCheckBox = findViewById<CheckBox>(R.id.agreed_cb)

        val loginButton = findViewById<TextView>(R.id.login)
        val nextButton: Button = findViewById(R.id.next_button)

        binding.login.setOnClickListener {
            val signin = Intent(this, SignInActivity::class.java)
            startActivity(signin)
        }


///getting input ids
        val name = findViewById<EditText>(R.id.name)
        val Email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val confirm_password = findViewById<EditText>(R.id.confirm_password)
        var phone=findViewById<EditText>(R.id.phone)
var pb=findViewById<ProgressBar>(R.id.pb)


// Assuming this is inside your activity or fragment
























































    nextButton.setOnClickListener {

            ///taking all the values
            val gotphone= phone.text.toString()
            val gotemail = Email.text.toString()
            val gotname = name.text.toString()
            val gotpassword = password.text.toString()
            val gotconfirm_password = confirm_password.text.trim().toString()



            var canwego = true


            ///checking the validity of name
            if (gotname.isEmpty()) {
                canwego = false

                name.setError("Enter your name");
                name.requestFocus();
                return@setOnClickListener;
            }

            if (gotname.length < 4)
            {
                canwego = false
                name.setError("Name is too short.Enter at least 6 characters");
                name.requestFocus();
                return@setOnClickListener;
            }





            //checking the validity of the email
            if (gotemail.isEmpty()) {
                canwego = false
                Email.setError("Enter an email address")
                Email.requestFocus()

                return@setOnClickListener;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(gotemail).matches()) {
                canwego = false
                Email.setError("Enter a valid email address");
                Email.requestFocus();
                return@setOnClickListener;
            }


            ///checking the validity of name
            if (gotphone.isEmpty()) {
                canwego = false

                phone.setError("Enter your phone number");
                phone.requestFocus();
                return@setOnClickListener;
            }

            if (gotphone.length != 11 || gotphone[0] != '0' || gotphone[1] != '1') {
                canwego = false
                phone.setError("Enter a valid number");
                phone.requestFocus();
                return@setOnClickListener;
            }


            //checking the validity of the password
            if (gotpassword.isEmpty()) {
                canwego = false
                password.setError("Enter a password");
                password.requestFocus();
                return@setOnClickListener;
            }

            if (gotpassword.length < 5) {

                canwego = false
                password.setError("Password must have at least 6 characters");
                password.requestFocus();
                return@setOnClickListener;
            }


            ///checking the validity of confirm password


            if (gotconfirm_password.isEmpty()) {
                canwego = false
                confirm_password.setError("Enter a password");
                confirm_password.requestFocus();
                return@setOnClickListener;
            }

            if (gotconfirm_password.length < 5) {

                canwego = false
                confirm_password.setError("Password must have at least 6 characters");
                confirm_password.requestFocus();
                return@setOnClickListener;
            }


            ///checking the validity of name
            if (gotname.isEmpty()) {
                canwego = false

                name.setError("Enter your name");
                name.requestFocus();
                return@setOnClickListener;
            }

            if (gotname.length < 4) {
                canwego = false
                name.setError("Name is too short.Enter at least 6 characters");
                name.requestFocus();
                return@setOnClickListener;
            }




            if (canwego) {







                if (gotpassword != gotconfirm_password) Toast.makeText(
                    this,
                    "Password didnt match",
                    Toast.LENGTH_SHORT
                ).show();
                else {


                    if (agreedCheckBox.isChecked) {

                        var pb=findViewById<ProgressBar>(R.id.pb)
                        pb.visibility= View.VISIBLE
                        nextButton.visibility=View.INVISIBLE

                        //sendEmailVerification()

                        // val regDone = Intent(this, regestration_almost_done::class.java)
                        pb.visibility=View.VISIBLE
                        var rad=Intent(this,regestration_almost_done::class.java)
                        rad.putExtra("Phone",gotphone)
                        rad.putExtra("Email",gotemail)
                        rad.putExtra("Password",gotpassword)
                        rad.putExtra("Name",gotname)
                      //  rad.putExtra("Phone",gotphone)
                      //  rad.putExtra("Phone",gotphone)

                        ///email: String, password: String,name: String,phone:String
                        startActivity(rad)
                        //createAccount(gotemail,gotpassword,gotname,gotphone)
                    } else {
                        Toast.makeText(
                            this,
                            "Please check if you agree to the terms and conditions",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

    }




    // [START on_start_check_user]
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }
    // [END on_start_check_user]
    private fun createAccount(email: String, password: String,name: String,phone:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                android.os.Handler().postDelayed({
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val uid = user?.uid

                        if (uid != null) {
                            // Now you have the UID of the newly created user
                            // You can use it to create a reference in the Realtime Database

                            val userMap = mapOf(
                                "Name" to name,
                                "Email" to email,
                                "Password" to password,
                                "Phone" to phone,
                                "UID" to uid
                            )



                            FirebaseDatabase.getInstance().getReference("Registered users")
                                .child(uid).setValue(userMap).addOnSuccessListener {





                                    // Iterate through all registered users
                                    FirebaseDatabase.getInstance().getReference("Registered users")
                                        .addListenerForSingleValueEvent(object : ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                val numberOfUsers = snapshot.childrenCount.toInt()
                                                if(numberOfUsers>=2) {
                                                    for (usersuids in snapshot.children) {

                                                        if(uid!=usersuids.key){
                                                           var theirchatroom= generateChatRoomId(uid,usersuids.key.toString())
                                                            var couple= mapOf(
                                                       "user1" to uid,
                                                   "user2" to usersuids.key.toString(),
                                                       "messages" to null
                                                )
                                                            FirebaseDatabase.getInstance().getReference("Chatting").child(theirchatroom).setValue(couple)


                                                        }

                                                    }
                                                }





                                            }

                                            override fun onCancelled(error: DatabaseError) {

                                            }

                                        })











                                    findViewById<Button>(R.id.next_button).visibility=View.VISIBLE
                                    findViewById<ProgressBar>(R.id.pb).visibility=View.INVISIBLE


                                    // Inside the addOnSuccessListener block
                                    val toast = Toast.makeText(
                                        baseContext,
                                        "Successfully created account",
                                        Toast.LENGTH_SHORT
                                    )
                                    toast.show()






                                    toast.view?.setOnClickListener {

                                        startActivity(Intent(this, loggedin_home_page::class.java))
                                       // finish()
                                    }

                                }.addOnFailureListener {
                                    Toast.makeText(
                                        baseContext,
                                        "User Registration Failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
findViewById<ProgressBar>(R.id.pb).visibility=View.INVISIBLE
                                }


                        }
                    } else {
                        findViewById<ProgressBar>(R.id.pb).visibility=View.INVISIBLE
                        // If sign in fails, display a message to the user.
                        Log.e("createAccount", "createUserWithEmail:failure", task.exception)
                       val exception = task.exception
                        if (exception is FirebaseAuthUserCollisionException) {
                            // The email address is already in use
                            findViewById<EditText>(R.id.email).error = "Email was already registered"
                            findViewById<EditText>(R.id.email).requestFocus()
                            findViewById<Button>(R.id.next_button).visibility=View.VISIBLE
                            findViewById<Button>(R.id.pb).visibility=View.INVISIBLE
                          //  return@setOnClickListener;
                           // Toast.makeText(
                             //   baseContext,
                                //"Authentication sgfbgfailed"+task.exception,
                                //Toast.LENGTH_SHORT
                           // ).show()
                        } else {
                            // Other sign-up failures
                            Toast.makeText(
                                baseContext,
                                "Authentication failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }
                }, 10000)
            }
    }


    private fun signIn() {auth.addAuthStateListener { firebaseAuth ->
        val user = firebaseAuth.currentUser
        if (user != null) {
            startActivity(Intent(this,loggedin_home_page::class.java))
            // User is signed in
            // You can navigate to the main screen or perform other actions
            // based on the signed-in state
        } else {
            // User is signed out
        }
    }

    }


    private fun sendVerificationCode(phoneNumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            object : OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                    // Auto-retrieval or instant verification completed.
                    // This method will be called if the code is automatically detected by Google Play services.
                    // You can use phoneAuthCredential to sign in the user (optional).
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Failed to send verification code or verification failed.
                    // Handle the error here.
                }

                override fun onCodeSent(verificationId: String, token: ForceResendingToken) {
                    // Code has been sent to the provided phone number.
                    // Save the verification ID and use it to verify the code entered by the user later.
                    // You can also save the token to automatically trigger resend action if needed.
                }
            }
        )
    }


    private   fun sendEmailVerification(email: String) {



        //val user = auth.currentUser
        val verificationCode = generateSixDigitCode()

        // Here you can save the verificationCode to a database or a shared preference
        // for later comparison when the user enters the code.
        //  val email=findViewById<EditText>(R.id.email).text.trim().toString()
        val subject = "Verification Code for Book Exchanging Project"
        val message = "Your verification code is: $verificationCode. Please use this code to verify your email address."

        val connectivityManager = getSystemService( Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        if (networkInfo == null || !networkInfo.isConnected) {
            // Handle no network connection

        }

        else {
            //sendVerificationCode(email,subject,message)
        }
        // Create an intent to send an email

        // val intent = Intent(Intent.ACTION_SENDTO)
        ////intent.data = Uri.parse("mailto:$email")
        //intent.putExtra(Intent.EXTRA_EMAIL,email)
        //intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //intent.putExtra(Intent.EXTRA_TEXT, message)
//startActivity(intent)
        // val code_passing=Intent(this,regestration_almost_done::class.java)
        // code_passing.putExtra("verification_code",verificationCode)

        /*    user?.sendEmailVerification()
                  ?.addOnCompleteListener(this) { task ->
                      Handler().postDelayed({
                      if (task.isSuccessful) {
                          // Email Verification sent successfully
                          Toast.makeText(
                              this,
                              "Verification email sent. Please check your email.",
                              Toast.LENGTH_SHORT
                          ).show()
                      } else {
                          // If the verification email sending fails, handle the error
                          Toast.makeText(
                              this,
                              "Failed to send verification email. Please try again.",
                              Toast.LENGTH_SHORT
                          ).show()
                      }

                  }, 7000)
                  }*/

    }
    private fun updateUI(user: FirebaseUser?) {


        startActivity(Intent(this, regestration_almost_done::class.java))

    }

    private fun reload() {
    }

    companion object {
        private const val TAG = "EmailPassword"
    }

    ///generating verification code
    private fun generateSixDigitCode(): String {
        val random = java.util.Random()
        val code = random.nextInt(900000) + 100000 // Generate a random 6-digit number
        return code.toString()
    }




    fun generateChatRoomId(user1Id: String, user2Id: String): String {
        val sortedUserIds = listOf(user1Id, user2Id).sorted().joinToString("_")
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val randomChars = (1..4).map { ('A'..'Z').random() }.joinToString("")
        return "CHATROOM_$sortedUserIds$timestamp$randomChars"
    }



    fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(object : OnCompleteListener<String> {
            override fun onComplete(task: Task<String>) {
                if (!task.isSuccessful) {
                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                    return
                }
                // Get new FCM registration token
                val token = task.result
                // Log and toast
            //    val msg = getString(R.string.msg_token_fmt, token)
              //  Log.d(TAG, msg)
          //      Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    /*
    fun sendVerificationCode(email:String,subject:String,message:String) {
        val recipient = email
        //val subject = "Verification Code"
        // val code = "123456" // Replace with your verification code
        //val message = "Your verification code is $code"

        val properties = Properties()
        properties["mail.smtp.host"] = "smtp.gmail.com"
        properties["mail.smtp.port"] = "587"
        properties["mail.smtp.auth"] = "true"
        properties["mail.smtp.starttls.enable"] = "true"

        val session = BlobStoreManager.Session.getInstance(properties, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication("p03734027@gmail.com", "oniketprantor")
            }
        })
        try {
            val mimeMessage = MimeMessage(session)
            mimeMessage.setFrom(InternetAddress("p03734027@gmail.com"))
            mimeMessage.setRecipient(Message.RecipientType.TO, InternetAddress(recipient))
            mimeMessage.subject = subject
            mimeMessage.setText(message)

            Transport.send(mimeMessage)
        }catch (e:Exception){

        }

        Toast.makeText(this,"Sent",Toast.LENGTH_SHORT).show()
    }

     */



    private fun sendNotification(title: String?, body: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

}

