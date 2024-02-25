package com.hassanpial.our_1st_project_of_book_exchange

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.google.firebase.database.FirebaseDatabase
import com.hassanpial.our_1st_project_of_book_exchange.databinding.ActivityMainBinding

class client_review : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_client_review)
        //setContentView(binding.root)
        var edittextreview=findViewById<EditText>(R.id.editTextReview)
var clientReview=findViewById<TextView>(R.id.client_review)
edittextreview.text.clear()
        findViewById<Button>(R.id.submit_review).setOnClickListener() {

findViewById<Button>(R.id.submit_review).visibility=View.VISIBLE
            clientReview.text = edittextreview.text
              var bookid=intent.getStringExtra("BookID")
             FirebaseDatabase.getInstance().getReference("ALLBOOKREVIEWS").child(bookid.toString()).setValue(clientReview)
            // Assuming your font file is in the assets folder with the name "your_font.ttf"
            try {
                val typeface = Typeface.createFromAsset(assets, "Simply.ttf")
                clientReview.typeface = typeface
            } catch (e: Exception) {

            }
        }


        clientReview.setOnClickListener {
            // Extract text from the TextView
            val reviewText = clientReview.text.toString()

            // Create an intent
            val reviewInOnePage = Intent(this, Review_in_1_page::class.java)

            // Pass the text as a String
            reviewInOnePage.putExtra("FullReview", reviewText)

            // Start the new activity
            startActivity(reviewInOnePage)
        }



    }
}