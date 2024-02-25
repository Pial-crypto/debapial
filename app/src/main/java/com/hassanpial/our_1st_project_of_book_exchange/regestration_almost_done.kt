package com.hassanpial.our_1st_project_of_book_exchange

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.random.Random

class regestration_almost_done : AppCompatActivity() {
    private  lateinit  var auth: FirebaseAuth
    var SMS_REQUEST_CODE=1
    var channelId="NOTIFICATION_CHANNEL"
    var id=101

    private lateinit var appBarConfiguration: AppBarConfiguration
  //  private lateinit var binding: ActivityRegestrationAlmostDoneBinding
  lateinit  var vcode: String
    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regestration_almost_done)
        //var binding= ActivityMainBinding.inflate(layoutInflater)
            //setContentView(binding.root)
        var code=generateRandomSixDigitNumber().toString();
        auth = Firebase.auth
        var phone="+88"+intent.getStringExtra("Phone").toString()
        Toast.makeText(this,"Your verification code is $code",Toast.LENGTH_SHORT).show()
        var email=intent.getStringExtra("Email").toString()
        var password=intent.getStringExtra("Password").toString()

        var name=intent.getStringExtra("Name").toString()
       // sendEmailVerification()

//requestsmsPermission()

        sendNotificationsms("Verification code sent",code,channelId,id)
        val back=findViewById<Button>(R.id.back)

setEditTextValues(code)
        ///implementing back button
        back.setOnClickListener(){
            finish()
        }


        findViewById<Button>(R.id.gotoyouraccount).setOnClickListener(){
            findViewById<Button>(R.id.gotoyouraccount).visibility=View.INVISIBLE
            findViewById<ProgressBar>(R.id.pgbarofcreateaccount).visibility=View.VISIBLE

            if (findViewById<EditText>(R.id.code1).text.toString() == code[0].toString() &&
                findViewById<EditText>(R.id.code2).text.toString() == code[1].toString() &&
                findViewById<EditText>(R.id.code3).text.toString() == code[2].toString() &&
                findViewById<EditText>(R.id.code4).text.toString() == code[3].toString() &&
                findViewById<EditText>(R.id.code5).text.toString() == code[4].toString() &&
                findViewById<EditText>(R.id.code6).text.toString() == code[5].toString() ) {
               // startActivity(Intent(this,loggedin_home_page::class.java))
                findViewById<Button>(R.id.gotoyouraccount).visibility=View.INVISIBLE
                findViewById<ProgressBar>(R.id.pgbarofcreateaccount).visibility=View.VISIBLE
                createAccount(email,password,name.toString(),phone.toString())


            }
            else{
                Toast.makeText(this,"Wrong verification code",Toast.LENGTH_SHORT).show()
                findViewById<Button>(R.id.gotoyouraccount).visibility=View.VISIBLE
                findViewById<ProgressBar>(R.id.pgbarofcreateaccount).visibility=View.INVISIBLE

            }



        }

        ///implementing resend button
        val resend=findViewById<TextView>(R.id.resend)
        resend.setOnClickListener(){
            // Toast.makeText(this,"sent again",Toast.LENGTH_SHORT).show()
            //sendEmailVerification()
           // sendVerificationCode(phone)

            //sendMessage2(phone,code.toString())
            sendNotificationsms("Verification code sent",code,channelId,id)

        }

        findViewById<Button>(R.id.gotoyourprofile).setOnClickListener(){
            checkEmailVerificationStatus(name, password, email, phone)
        }





    }





    private fun autofillVerificationCode(email: String) {
        if (email != null && email.length == 11) { // Assuming the verification code is in the email
            val codeDigits = email.substring(1)
            setEditTextValues(codeDigits)
        }
    }

    private fun setEditTextValues(codeDigits: String) {
        // Set the values of the EditText fields based on the codeDigits
      //  var codeDigits= intent.getStringExtra("verification_code")
        val code1 = findViewById<EditText>(R.id.code1)
        code1.setText(codeDigits?.get(0).toString())

        val code2 = findViewById<EditText>(R.id.code2)
        code2.setText(codeDigits?.get(1).toString())

        val code3 = findViewById<EditText>(R.id.code3)
        code3.setText(codeDigits?.get(2).toString())

        val code4 = findViewById<EditText>(R.id.code4)
        code4.setText(codeDigits?.get(3).toString())

        val code5 = findViewById<EditText>(R.id.code5)
        code5.setText(codeDigits?.get(4).toString())

        val code6 = findViewById<EditText>(R.id.code6)
        code6.setText(codeDigits?.get(5).toString())


    }





    fun sendEmailVerification() {
        val user = auth.currentUser

        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                Handler().postDelayed({
                    if (task.isSuccessful) {
                        // Email Verification sent successfully
                        Toast.makeText(
                            this,
                            "Verification email resent.",
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
            }

    }



   // import android.telephony.SmsManager

    fun sendMessage2(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNumber, null, message, null, null)
            Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    fun sendMessage(phoneNumber: String, message: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:$phoneNumber") // Set the destination phone number
        intent.putExtra("sms_body", message) // Set the message body

        val smsIntent = Intent.createChooser(intent, "Send SMS")
        if (smsIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(smsIntent, SMS_REQUEST_CODE)
        } else {
            // Handle the case where no activity can handle the intent
            Toast.makeText(this, "Couldn't send the verification code", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SMS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // SMS sent successfully
                Toast.makeText(this, "SMS sent successfully", Toast.LENGTH_SHORT).show()
                //setEditTextValues(code)
            } else {
                // SMS sending failed
                //Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show()
            }
        }
    }









    /*

    private fun sendVerificationCode(phoneNumber: String) {
        Log.d(TAG, "Attempting to send verification code to $phoneNumber")

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                    Log.d(TAG, "Verification completed with auto-retrieval: $phoneAuthCredential")
                    Toast.makeText(baseContext,"Verification completed with auto-retrieval: $phoneAuthCredential",Toast.LENGTH_SHORT).show()

                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e(TAG, "Verification failed: ${e.message}", e)
                    // Handle verification failure
                    if (e is FirebaseNetworkException) {
                        Log.d(TAG,"$e is networkproblem")
                        // Handle network-related errors
                        // For example, notify the user about the network issue
                    } else {
                        Log.d(TAG,"$e is otherproblem")
                        // Handle other types of verification failures
                    }
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    Log.d(TAG, "Verification code sent successfully to $phoneNumber")
                  //  handleVerification(phoneAuthCredential)
                    // Handle code sent successfully
                }
            }
        )
    }

     */









    private fun issmsPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Request external storage permission if not granted
    private fun requestsmsPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.SEND_SMS),
            SMS_REQUEST_CODE
        )
    }

    // Handle permission request response
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SMS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with accessing external storage
           //sendMessage2("+88"+intent.getStringExtra("Phone").toString(),code)
                } else {
                    // Permission denied, show a message or handle accordingly
                    Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Check storage permission and request if not granted
    private fun checkStoragePermission() {
        if (issmsPermissionGranted()) {
            // Permission is already granted
         //   sendMessage2("+88"+intent.getStringExtra("Phone").toString(),code)

        } else {
            // Permission is not granted, request it
            requestsmsPermission()
        }
    }












    fun generateRandomSixDigitNumber(): Int {
        // Generate a random number between 100000 and 999999 (inclusive)
        return Random.nextInt(100000, 1000000)
    }


    private fun handleVerification(phoneAuthCredential: PhoneAuthCredential) {
        val code = phoneAuthCredential.smsCode
        if (code != null) {
            Log.d(TAG, "Verification code received: $code")
            Toast.makeText(this,"Verification code received",Toast.LENGTH_SHORT).show()
            setEditTextValues(code.toString())

            // Proceed with verification
        } else {
            Log.w(TAG, "Received null verification code")
            // Prompt the user to manually input the code
            // Display UI to allow manual input of the verification code
            // This could include showing EditText fields for each digit of the code
            // and a button to trigger verification with the input code
        }
    }

    private fun createAccount(email: String, password: String,name: String,phone:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                android.os.Handler().postDelayed({
                    if (task.isSuccessful) {

                        sendEmailVerification()
                    } else {
                        findViewById<ProgressBar>(R.id.pb).visibility= View.INVISIBLE
                        // If sign in fails, display a message to the user.
                        Log.e("createAccount", "createUserWithEmail:failure", task.exception)
                        val exception = task.exception
                        if (exception is FirebaseAuthUserCollisionException) {
                            // The email address is already in use
                            findViewById<EditText>(R.id.email).error = "Email was already registered"
                            findViewById<EditText>(R.id.email).requestFocus()
                            findViewById<Button>(R.id.next_button).visibility= View.VISIBLE
                            findViewById<Button>(R.id.pb).visibility= View.INVISIBLE
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




    private fun sendVerificationEmail() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Email verification link sent successfully
                    Log.d(TAG, "Email verification link sent.")
                    Toast.makeText(this, "Verification email sent.", Toast.LENGTH_SHORT).show()
                } else {
                    // Error sending email verification link
                    Log.e(TAG, "Error sending verification email.", task.exception)
                    Toast.makeText(this, "Failed to send verification email.", Toast.LENGTH_SHORT).show()


                }
            }
    }


    private fun checkEmailVerificationStatus(name:String,password: String,email: String,phone: String) {
        val user = auth.currentUser
        user?.reload()?.addOnSuccessListener {
            if (user.isEmailVerified) {
                // Email is verified
                Log.d(TAG, "Email is verified.")
                Toast.makeText(this, "Email is verified.", Toast.LENGTH_SHORT).show()
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
                                .addListenerForSingleValueEvent(object :
                                    ValueEventListener {
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











                               findViewById<Button>(R.id.next_button).visibility= View.VISIBLE
                               findViewById<ProgressBar>(R.id.pb).visibility= View.INVISIBLE


                              //Inside the addOnSuccessListener block
                             val toast = Toast.makeText(
                              baseContext,
                               "Successfully created account",
                               Toast.LENGTH_SHORT
                             )
                            toast.show()






                                 toast.view?.setOnClickListener {

                             startActivity(Intent(this, loggedin_home_page::class.java))

                            finish()
                             }

                        }.addOnFailureListener {
                            Toast.makeText(
                                baseContext,
                                "User Registration Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                            findViewById<Button>(R.id.pgbarofcreateaccount).visibility=View.INVISIBLE
                            findViewById<ProgressBar>(R.id.gotoyouraccount).visibility=View.VISIBLE
                        }


                }
                // Proceed with your app logic after email verification
            } else {
                // Email is not verified
                Log.d(TAG, "Email is not verified.")
                Toast.makeText(this, "Email is not verified.", Toast.LENGTH_SHORT).show()
                // Email is not verified, delete the user
                user.delete()
                    .addOnSuccessListener {
                        Log.d(TAG, "User deleted successfully.")
                        Toast.makeText(this, "User deleted successfully.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error deleting user: ${e.message}")
                        Toast.makeText(this, "Error deleting user: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }




    private fun sendNotificationsms(title: String?, body: String?,channelId:String,id:Int) {
        val intent = Intent(this, regestration_almost_done::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )


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

        notificationManager.notify(id /* ID of notification */, notificationBuilder.build())
    }


    fun generateChatRoomId(user1Id: String, user2Id: String): String {
        val sortedUserIds = listOf(user1Id, user2Id).sorted().joinToString("_")
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val randomChars = (1..4).map { ('A'..'Z').random() }.joinToString("")
        return "CHATROOM_$sortedUserIds$timestamp$randomChars"
    }



    fun createwithverifying(email:String,password:String){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verificationTask ->
                            if (verificationTask.isSuccessful) {




                               // startActivity(Intent(this,loggedin_home_page::class.java))












                                // Email sent successfully, handle verification
                                // You may not delete the user immediately here. Instead,
                                // you can schedule a job or task to check verification status
                                // after a certain period and delete if necessary.
                            } else {
                                // Failed to send verification email
                                // Handle error, maybe delete the user in this case
                            }
                        }
                } else {
                    // Registration failed
                    // Handle error, if needed
                }
            }

    }



}