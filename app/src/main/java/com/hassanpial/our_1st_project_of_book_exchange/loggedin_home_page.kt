package com.hassanpial.our_1st_project_of_book_exchange

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.ui.AppBarConfiguration
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale


private lateinit var appBarConfiguration: AppBarConfiguration
//private lateinit var binding: ActivityMainBinding

class loggedin_home_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loggedin_home_page)

       // var binding= ActivityMainBinding.inflate(layoutInflater)
      //  setContentView(binding.root)


        val notificationTitle = "New Message"
        val notificationBody = "You've received a new message!"
        //sendNotification(notificationTitle, notificationBody,notificationBody,1)


var message=findViewById<ImageView>(R.id.message)

        val dl = findViewById<DrawerLayout>(R.id.dl)
        val nv = findViewById<NavigationView>(R.id.navigation_view)
        val mainll = findViewById<LinearLayout>(R.id.mainll)
        val navtoolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.nav_toolbar)
        val drawerToggle = ActionBarDrawerToggle(
            this,
            dl,
            navtoolbar,
            R.string.open,
            R.string.close
        )
var search=findViewById<ImageView>(R.id.search)

        search.setOnClickListener(){
            startActivity(Intent(this,searching_book::class.java))
        }


        mainll.setBackgroundColor(resources.getColor(R.color.teal_200))
        dl.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
/*
        try {
            // Code that may cause an exception
            // For example, operations that involve accessing external resources or performing I/O operations
            // Ensure that you identify the specific lines of code that could potentially throw exceptions
            // Place only the relevant portion of your code inside the try block
            // It's generally not recommended to wrap large sections of code in a single try block
            // Instead, focus on wrapping specific operations that are prone to exceptions

            // Example:
            val headerView = nv.getHeaderView(0)
            val darkModeSwitch: SwitchCompat = headerView.findViewById(R.id.darkModeSwitch)

            darkModeSwitch.isChecked =
                AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

            darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                recreate()
            }
        } catch (e: Exception) {
            // Handle the exception
            // This block will be executed if an exception occurs within the try block
            // You can log the exception, display an error message, or perform any other necessary actions
            Log.e("Exception", "Error: ${e.message}", e)
            // You can also provide a fallback behavior or recovery mechanism here
        }

 */

        val headerView = nv.getHeaderView(0)


        var sellfaster=findViewById<Button>(R.id.sell_faster)
var exchange=findViewById<Button>(R.id.exchange_book)
        val popular = headerView.findViewById<TextView>(R.id.Popular)
        val back = headerView.findViewById<LinearLayout>(R.id.back)
var dpimage=headerView.findViewById<ShapeableImageView>(R.id.profileImageView)
var received=findViewById<TextView>(R.id.received)
        var currentUID= FirebaseAuth.getInstance().uid.toString()


        FirebaseDatabase.getInstance().getReference("Registered users").child(currentUID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Handle the data when it changes
                    if (snapshot.exists()) {
                        // The snapshot contains the data for the specified user

                        var dp=snapshot.child("Profile Picture").getValue(String::class.java)


                        if (dp != null) {
                            // Load the image into an ImageView using Glide (or any other method you prefer)
                            Glide.with(applicationContext)
                                .load(dp)
                                .into(dpimage)

                        } else {
                            dpimage.setImageResource(R.drawable.profile_picture_not_available)
                            // Handle the case where the "Profile Picture" field is null or doesn't exist
                            // You might want to use a default image or show an error placeholder
                            // imageView.setImageResource(R.drawable.default_profile_image)
                            // or
                            // handle the error appropriately
                        }
                    } else {
                        //  userok=false
                        // Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
                        // Handle the case when the user data doesn't exist
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle errors, if any
                    println("Error: ${error.message}")
                }
            })











        var myuid=FirebaseAuth.getInstance().uid


        FirebaseDatabase.getInstance().getReference("Chatting")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val finalmap = ArrayList<Pair<String,Pair<String, Pair<String, String>>>>()
                    for (chatSnapshot in dataSnapshot.children) {
                        // This map will store time as key and message as value
                        val chatRoomId = chatSnapshot.key

                        var opponent=" "


                        if(chatSnapshot.child("user1").value.toString()==myuid.toString() ){

                            val messageMap = ArrayList<Pair<String, Pair<String, String>>>()
                            // addLinearLayoutToScrollView("adsf")
                            var opponentuid = chatSnapshot.child("user2").value.toString()
                            val opponentRef = FirebaseDatabase.getInstance().getReference("Registered users").child(opponentuid).child("Name")

                            opponentRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    opponent = dataSnapshot.value.toString()
                                    // Use opponentName as needed
                                    //  println("Opponent's Name: $opponentName")

                                    for(msgs in chatSnapshot.child("messages").children){
                                        var msgkey=msgs.key
                                        var sentmsg=chatSnapshot.child("messages").child(msgkey.toString()).child("sentmessage").value.toString()
                                        val thismsgtime = chatSnapshot.child("messages").child(msgkey.toString()).child("senttime").value.toString()
                                        var notificationstatus=chatSnapshot.child("messages").child(msgkey.toString()).child("Notification_status").value.toString()
                                        // Convert Timestamp to a string representation
                                        //  val timestampString = thismsgtime?.toString() ?: ""

// Add the message and its time to the list as a Pair
                                        if (notificationstatus=="unnotified" &&  chatSnapshot.child("messages").child(msgkey.toString()).child("senderuid").value.toString()==opponentuid) {
                                            messageMap.add(
                                                Pair(
                                                    thismsgtime,
                                                    Pair(sentmsg, opponentuid)
                                                )
                                            )
                                        }
                                    }


                                    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

                                    messageMap.sortByDescending { it.first } // Assuming it.first is the timestamp as Long



                                    val lastEntry = messageMap.firstOrNull()

// Check if lastEntry is not null before using it
                                    var lasttime=""
                                    var lastMessage=""
                                    if (lastEntry != null) {
                                        lasttime = lastEntry.first
                                        lastMessage = lastEntry.second.first
                                        var thisopponentuid=lastEntry.second.second
                                        finalmap.add(Pair(lasttime, Pair(lastMessage,Pair(opponent,thisopponentuid) )))
                                        // addLinearLayoutToScrollView(lastMessage)
                                        // Do something with the last time and message
                                        // println("Last Time: $lastTime, Last Message: $lastMessage")
                                    } else {
                                        // Handle the case where there are no messages
                                        println("No messages found.")
                                        // addLinearLayoutToScrollView("no message")
                                    }


//            addLinearLayoutToScrollView("adsffgdfa")






                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle errors
                                    println("Error getting opponent's name: ${databaseError.message}")
                                }
                            })
                            // addLinearLayoutToScrollView(chatSnapshot.child("user1").toString())
                        }

                        else if(chatSnapshot.child("user2").value.toString()==myuid.toString() ){
                            val messageMap = ArrayList<Pair<String, Pair<String, String>>>()
                            // addLinearLayoutToScrollView("adsf")
                            var opponentuid = chatSnapshot.child("user1").value.toString()
                            val opponentRef = FirebaseDatabase.getInstance().getReference("Registered users").child(opponentuid).child("Name")

                            opponentRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    opponent = dataSnapshot.value.toString()
                                    // Use opponentName as needed
                                    //  println("Opponent's Name: $opponentName")

                                    for(msgs in chatSnapshot.child("messages").children){
                                        var msgkey=msgs.key
                                        var sentmsg=chatSnapshot.child("messages").child(msgkey.toString()).child("sentmessage").value.toString()
                                        val thismsgtime = chatSnapshot.child("messages").child(msgkey.toString()).child("senttime").value.toString()
// Add the message and its time to the map
                                        var notificationstatus=chatSnapshot.child("messages").child(msgkey.toString()).child("Notification_status").value.toString()
                                        // Convert Timestamp to a string representation
                                        //  val timestampString = thismsgtime?.toString() ?: ""

// Add the message and its time to the list as a Pair
                                        if (notificationstatus=="unnotified" &&  chatSnapshot.child("messages").child(msgkey.toString()).child("senderuid").value.toString()==opponentuid) {
                                            messageMap.add(
                                                Pair(
                                                    thismsgtime,
                                                    Pair(sentmsg, opponentuid)
                                                )
                                            )
                                        }
                                    }


                                    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

                                    messageMap.sortByDescending { it.first } // Assuming it.first is the timestamp as Long



                                    val lastEntry = messageMap.firstOrNull()

// Check if lastEntry is not null before using it
                                    var lasttime=""
                                    var lastMessage=""
                                    if (lastEntry != null) {
                                        lasttime = lastEntry.first
                                        lastMessage = lastEntry.second.first
                                        var thisopponentuid=lastEntry.second.second
                                        finalmap.add(Pair(lasttime, Pair(lastMessage,Pair(opponent,thisopponentuid) )))
                                        // addLinearLayoutToScrollView(lastMessage)

                                        // Do something with the last time and message
                                        // println("Last Time: $lastTime, Last Message: $lastMessage")
                                    } else {
                                        // Handle the case where there are no messages
                                        println("No messages found.")
                                        // addLinearLayoutToScrollView("no message")
                                    }


//            addLinearLayoutToScrollView("adsffgdfa")






                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Handle errors
                                    println("Error getting opponent's name: ${databaseError.message}")
                                }
                            })
                            // addLinearLayoutToScrollView(chatSnapshot.child("user1").toString())
                        }


                    }


                    val handler = Handler(Looper.getMainLooper())

// Delay execution by 2000 milliseconds (2 seconds)
                    handler.postDelayed({
                        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

                        finalmap.sortByDescending { it.first } // Assuming it.first is the timestamp as Long
                        // addLinearLayoutToScrollView("adsffgdfa")
                        //addLinearLayoutToScrollView(finalmap.size.toString())
                        for ((time, msgpair) in finalmap) {
                            // Process each entry in reverse order
                            //println("Key: $key, Value: $value")

                            var latestmsg= msgpair.first
                            var latestopponent= msgpair.second.first
                            var latestopponentuid=msgpair.second.second
                              val formattedTime = convertMillisToDateFormat(time.toLong(), "dd-MM-yyyy HH:mm")
                            //  addLinearLayoutToScrollView(latestopponent + " at "+formattedTime,latestopponentuid)
                            sendNotification("$latestopponent sent you a message at $formattedTime",latestmsg.toString(),latestmsg,getRandomNumber())

                        }}, 10000)



// Traverse the list and call the method for each pair





                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors
                }
            })





















        message.setOnClickListener(){
            startActivity(Intent(this,received_messages::class.java))
        }

findViewById<TextView>(R.id.newrecruit).setOnClickListener(){
startActivity(Intent(this,book_uploading_page::class.java))
}

headerView.findViewById<TextView>(R.id.edit_profile).setOnClickListener{
 startActivity(Intent(this,Editing_Profile::class.java))
}



        received.setOnClickListener(){
            startActivity(Intent(this,received_book::class.java))
        }


        sellfaster.setOnClickListener(){
startActivity(Intent(this,Ready_to_Sell_Book_List_for_me::class.java))
        }

        exchange.setOnClickListener(){
            startActivity(Intent(this,Exchangable_Book_List_for_me::class.java))
        }


        headerView.findViewById<Button>(R.id.logout).setOnClickListener(){
            logout()
            redirectToLoginScreen()
        }

        back.setOnClickListener {
            if (dl.isDrawerOpen(GravityCompat.START)) {
                dl.closeDrawer(GravityCompat.START)
            }
        }

        popular.setOnClickListener {
            Log.i("Tag", "Balcal")
        }
    }

    override fun onBackPressed() {
        val dl = findViewById<DrawerLayout>(R.id.dl)
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        // You may also clear any saved user credentials or tokens if needed
        // e.g., clearSharedPreferences()
    }


    private fun redirectToLoginScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun clearSharedPreferences() {
        val sharedPreferences = getSharedPreferences("YourSharedPreferencesName", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }




    private fun sendNotification(title: String?, body: String?,channelId:String,id:Int) {
        val intent = Intent(this, received_messages::class.java).apply {
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




    fun convertMillisToDateFormat(millis: Long, dateFormat: String): String {
        val date = Date(millis)
        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        return sdf.format(date)
    }
    fun getRandomNumber(): Int {
        return (Int.MIN_VALUE..Int.MAX_VALUE).random()
    }






}