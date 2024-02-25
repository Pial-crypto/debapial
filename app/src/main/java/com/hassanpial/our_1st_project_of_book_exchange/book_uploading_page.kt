package com.hassanpial.our_1st_project_of_book_exchange

import BookAdapter
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
// Declare the adapter variable at the class level

class book_uploading_page : AppCompatActivity() {
    var gallery_request_code=1
    private var selectedImageUri: Uri? =null
    var currentUID= FirebaseAuth.getInstance().uid.toString()
    private lateinit var adapter: BookAdapter
    val storageRef = FirebaseStorage.getInstance().reference.child("profile_images")
    val imageRef = storageRef.child("$currentUID.jpg")
    lateinit var book_list:ArrayList<Book>

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_uploading_page)
        //val binding= ActivityMainBinding.inflate(layoutInflater)
       // setContentView(binding.root)


var selectimagebutton= findViewById<Button>(R.id.buttonSelectImage)

      selectimagebutton .setOnClickListener(){
            val gallery= Intent(Intent.ACTION_PICK)
            gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery,gallery_request_code)

            //Toast.makeText(this,"sdafd",Toast.LENGTH_LONG).show()
        }






      // {
            //findViewById<Button>(R.id.buttonUploadBook).isClickable=true

            findViewById<Button>(R.id.buttonUploadBook).setOnClickListener() {
                var book_name = findViewById<EditText>(R.id.editTextBookTitle).text.toString()
                var writers_name = findViewById<EditText>(R.id.editTextAuthorName).text.toString()
                var genere = findViewById<EditText>(R.id.editTextGenre).text.toString()

                var check = false
                if (findViewById<CheckBox>(R.id.checkBoxExchange).isChecked) {
                    check = true
                }

                if (findViewById<CheckBox>(R.id.checkBoxSell).isChecked) {
                    check = true
                }


                if (check && !book_name.isEmpty() && !writers_name.isEmpty() && !genere.isEmpty() ) {

                    // get the user's UID

                    val databaseReference =
                        FirebaseDatabase.getInstance().getReference("Registered users")
                            .child(currentUID)
                    var status:String="Not told yet"
                    if(findViewById<CheckBox>(R.id.checkBoxExchange).isChecked  && findViewById<CheckBox>(R.id.checkBoxSell).isChecked )status="Available to Sell & Exchange"
                    else    if(findViewById<CheckBox>(R.id.checkBoxExchange).isChecked )  status="Available to Exchange"
                    else    if(findViewById<CheckBox>(R.id.checkBoxSell).isChecked )  status="Available to Sell"

                    if (selectedImageUri != null) {
                        // Upload the image to Firebase Storage
                        imageRef.putFile(selectedImageUri!!)
                            .addOnSuccessListener { taskSnapshot ->
                                // Get the download URL
                                imageRef.downloadUrl
                                    .addOnSuccessListener { uri ->
                                        val imageUrl = uri.toString()

                                        // Generate a unique key for the new book
                                        val newBookKey = databaseReference.child("Book").push().key

                                        // Create a map for the new book

                                        val newBookData = mapOf(
                                            "Picture of book" to imageUrl,
                                            "Name" to book_name,
                                            "Author" to writers_name,
                                            "Genre" to genere,
                                            "Status" to status,
                                            "Owner" to currentUID,
                                            "Book id" to newBookKey
                                        )



                                      //  adapter.updateData(book_list)

                                        // Update the user data with the new book
                                        val bookUpdates = mapOf<String, Any>(
                                            "/Book/$newBookKey" to newBookData
                                        )

                                        // Update the user data in the Realtime Database
                                        databaseReference.updateChildren(bookUpdates)
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    this,
                                                    "Successfully uploaded",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(
                                                    this,
                                                    "Failed to save ",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                    }
                                    .addOnFailureListener { exception ->
                                        // Handle the failure to get download URL
                                        Toast.makeText(
                                            this,
                                            "Failed to get image URL",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            }
                    } else {
                        // Generate a unique key for the new book
                        val newBookKey = databaseReference.child("Book").push().key

                        // Create a map for the new book
                        val newBookData = mapOf(
                            "Picture of book" to null,
                            "Name" to book_name,
                            "Author" to writers_name,
                            "Genre" to genere,
                            "Status" to status,
                            "Owner" to  currentUID,
                            "Book id" to newBookKey
                        )

                      //  book_list.add(Book(null,book_name,writers_name,genere))
                        // Notify the listeners (searching_book activity) about the changes
                       // adapter.updateData(book_list)
                        // Assuming book_list is already populated with Book objects
                      //  val intent = Intent(this,searching_book::class.java)
                    //    intent.putExtra("bookList", book_list)

                        // Update the user data with the new book
                        val bookUpdates = mapOf<String, Any>(
                            "/Book/$newBookKey" to newBookData
                        )

                        // Update the user data in the Realtime Database
                        databaseReference.updateChildren(bookUpdates)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Successfully uploaded", Toast.LENGTH_LONG)
                                    .show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Failed to save ", Toast.LENGTH_LONG).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Please check all of this", Toast.LENGTH_LONG).show()
                }


                //}


            }





    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == gallery_request_code && resultCode == RESULT_OK && data != null) {
            // selectedImageUri = data.data
            // You can set the selected image to your ImageView
            findViewById<ImageView>(R.id.imageViewBookCover).setImageURI(data.data)
            selectedImageUri= data.data

        }
    }
}