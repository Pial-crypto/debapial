
package com.hassanpial.our_1st_project_of_book_exchange

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase

class Viewing_reviews : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewing_reviews)
var review=intent.getStringExtra("reviewKey")
        var clientReview=findViewById<TextView>(R.id.client_review)


        clientReview.text = review
        var bookid=intent.getStringExtra("BookID")
       // FirebaseDatabase.getInstance().getReference("ALLBOOKREVIEWS").child(bookid.toString()).setValue(clientReview)
        // Assuming your font file is in the assets folder with the name "your_font.ttf"
        try {
            val typeface = Typeface.createFromAsset(assets, "Simply.ttf")
            clientReview.typeface = typeface
        } catch (e: Exception) {

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