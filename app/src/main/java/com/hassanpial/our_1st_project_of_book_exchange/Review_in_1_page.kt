package com.hassanpial.our_1st_project_of_book_exchange

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Review_in_1_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_in1_page)

        findViewById<TextView>(R.id.fullreview).text = intent.getStringExtra("FullReview")
    }
}