package com.hassanpial.our_1st_project_of_book_exchange

import BookAdapter
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class received_book : AppCompatActivity(),BookAdapter.OnItemClickListener  {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookAdapter
    private lateinit var searchView: SearchView
    private lateinit var review: Intent
    //val allBooks= mutableListOf<Book>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_book)

      review=Intent(this,received_messages::class.java)

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)

        // Initialize RecyclerView with an empty book list
        // Assuming you are using Firebase Realtime Database

// Example for Firebase Realtime Database
        //  FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        var uid = FirebaseAuth.getInstance().uid.toString()
        val databaseReference =
            FirebaseDatabase.getInstance().getReference("Registered users").child(uid)
                .child("Booked books")


        // ... (rest of your existing code)


        val allBooks = mutableListOf<Book>()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing list


                for (bookSnapshot in snapshot.children) {
                    // Accessing child nodes within the book ID
                    val author = bookSnapshot.child("Author").value.toString()
                    val genre = bookSnapshot.child("Genre").value.toString()
                    val name = bookSnapshot.child("Name").value.toString()
                    val picture = bookSnapshot.child("Picture of book").value.toString()
                    val status = bookSnapshot.child("Status").value.toString()
                    val owner = bookSnapshot.child("Owner").value.toString()
                    var bookid = bookSnapshot.child("Book id").value.toString()
                    // Create a Book object or do something with the retrieved data
                    if (status == "Received") {
                        val book = Book(
                            // Unique book ID
                            picture,
                            name,
                            author,
                            genre,
                            status,
                            owner,
                            bookid
                        )

                        // Add the book to the list
                        allBooks.add(book)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })



        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BookAdapter(allBooks)

        // Set the click listener
        adapter.setOnItemClickListener(this)

        recyclerView.adapter = adapter


        // Set a query listener for the SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission if needed
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter your RecyclerView data based on the search query
                adapter.filter(newText)
                return true
            }
        })

        //go_to_send_message= Intent(this,sending_message::class.java)


    }


    override fun onItemClick(position: Int, book: Book) {
        Log.d("BookAdapter", "Item clicked at position $position with book name ${book.name}")
        var owner_name="eredr"
        var owner_uid="fadsf"
        // Add other actions as needed
        FirebaseDatabase.getInstance().getReference("Registered users").child(book.owner)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Handle the data when it changes
                    if (snapshot.exists()) {
                        // The snapshot contains the data for the specified user

showExchangeDialog(book)

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





    }





    private fun showExchangeDialog(book: Book) {
        val builder = AlertDialog.Builder(this)

        // Set the message for the Alert
        builder.setMessage("Do you want to give a review?")

        // Set Alert Title
        builder.setTitle("Alert!")

        // Set Cancelable false for when the user clicks outside the Dialog Box, then it will remain shown
        builder.setCancelable(false)

        // Set the positive button with "Yes" name
        builder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                // When the user clicks "Yes", you can add your logic here
                // For example, you can handle the exchange process
                //startActivity(go_to_send_message)
review.putExtra("BookID",book.bookid)

                startActivity(review)




                dialog?.dismiss() // Dismiss the dialog if needed
            }
        })

        // Set the Negative button with "No" name
        builder.setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                // If the user clicks "No", you can add your logic here
                // For example, you can cancel the exchange process
                dialog?.dismiss() // Dismiss the dialog if needed
            }
        })

        // Create the Alert dialog
        val alertDialog: AlertDialog = builder.create()

        // Show the Alert Dialog box
        alertDialog.show()
    }







}