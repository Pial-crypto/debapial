package com.hassanpial.our_1st_project_of_book_exchange

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

class sending_message : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sending_message)
        var ownername=(intent.getStringExtra("Owner"))
        findViewById<TextView>(R.id.textViewOwnerName).setText(intent.getStringExtra("Owner"))
        findViewById<TextView>(R.id.textViewOwnerlocation).setText(intent.getStringExtra("Location"))
        var ownerUID=intent.getStringExtra("UID")
        var dp=intent.getStringExtra("dp")
        if (dp != null) {
            // Load the image into an ImageView using Glide (or any other method you prefer)
            Glide.with(applicationContext)
                .load(dp)
                .into(findViewById(R.id.imageViewOwner))

        } else {
           findViewById<ImageView>(R.id.imageViewOwner).setImageResource(R.drawable.blankpp)

            // Handle the case where the "Profile Picture" field is null or doesn't exist
            // You might want to use a default image or show an error placeholder
            // imageView.setImageResource(R.drawable.default_profile_image)
            // or
            // handle the error appropriately8/
        }




        findViewById<Button>(R.id.buttonSendMessage).setOnClickListener(){
            findViewById<ProgressBar>(R.id.sendmsgpb).visibility= View.VISIBLE
            if(ownerUID==FirebaseAuth.getInstance().uid.toString()) {
                findViewById<ProgressBar>(R.id.sendmsgpb).visibility= View.INVISIBLE
                Toast.makeText(this, "Its your book", Toast.LENGTH_SHORT).show()
            }

       else {
                var go_to_chat = Intent(this, chatting_page::class.java)
                go_to_chat.putExtra("ownerUID", ownerUID)
                go_to_chat.putExtra("ownerName", ownername)
                startActivity(go_to_chat)
            }
        }
    }
}