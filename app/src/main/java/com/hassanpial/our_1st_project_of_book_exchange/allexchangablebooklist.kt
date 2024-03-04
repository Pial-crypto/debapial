package com.hassanpial.our_1st_project_of_book_exchange

import BookAdapter
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class allexchangablebooklist : AppCompatActivity(),BookAdapter.OnItemClickListener{
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allexchangablebooklist)


        val databaseReference =    FirebaseDatabase.getInstance().getReference("Registered users")


        // ... (rest of your existing code)



        val allBooks= mutableListOf<Book>()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing list


                for (userSnapshot in snapshot.children) {
                    val booksSnapshot = userSnapshot.child("Book")

                    for (bookSnapshot in booksSnapshot.children) {
                        // Accessing child nodes within the book ID
                        val author = bookSnapshot.child("Author").value.toString()
                        val genre = bookSnapshot.child("Genre").value.toString()
                        val name = bookSnapshot.child("Name").value.toString()
                        val picture = bookSnapshot.child("Picture of book").value.toString()
                        val status=bookSnapshot.child("Status").value.toString()
                        val owner=bookSnapshot.child("Owner").value.toString()
                        var bookid=bookSnapshot.child("Book id").value.toString()
                            //declaring book

                        if (status=="Available to Exchange" && userSnapshot.toString()!=FirebaseAuth.getInstance().uid.toString()) {
                            // Create a Book object or do something with the retrieved data
                            var book = Book(
                                // Unique book ID
                                picture,
                                name,
                                author,
                                genre,
                                status,
                                owner,
                                bookid
                            )
                            allBooks.add(book)
                        }
                        // Add the book to the list


                    }
                }





                // Update the adapter with the new list of books for all users
                // adapter.updateBooks(allBooks)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }
        })


        // Set the click listener
       Handler().postDelayed({




            adapter = BookAdapter(allBooks)

            recyclerView.adapter = adapter
            adapter.setOnItemClickListener(this)
            recyclerView.layoutManager = LinearLayoutManager(this)
        } , 2000) // Delay in milliseconds (e.g., 2000 milliseconds or 2 seconds)






        // Set a query listener for the SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override   fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission if needed
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter your RecyclerView data based on the search query
                adapter.filter(newText)
                return true
            }
        })











    }

    override fun onItemClick(position: Int, book: Book) {

    }













    private fun showAlertDialog(book: Book) {
        val builder = AlertDialog.Builder(this)

        // Set the message for the Alert
        builder.setMessage("Do you want to see the review?")

        // Set Alert Title
        builder.setTitle("Alert!")

        // Set Cancelable false for when the user clicks outside the Dialog Box, then it will remain shown
        builder.setCancelable(false)

        // Set the positive button with "OK" name
        builder.setPositiveButton("OK", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                // Handle the positive button action if needed
                dialog?.dismiss() // Dismiss the dialog if needed


                FirebaseDatabase.getInstance()
                    .getReference("ALLBOOKREVIEWS")
                    .child(book.bookid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange( dataSnapshot: DataSnapshot) {
                            // Handle the data retrieved from the database
                            // dataSnapshot contains the data at the specified database reference
                            var itsreview=dataSnapshot.value.toString()

                            val reviewView = Intent(this@allexchangablebooklist, Viewing_reviews::class.java)
                            reviewView.putExtra("reviewKey", itsreview) // Pass the review data to the next activity
                            startActivity(reviewView)

                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Handle any errors that occur during the read operation
                        }
                    })


            }
        })

        // Set the Negative button with "Cancel" name
        builder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                // Handle the negative button action if needed
                dialog?.dismiss() // Dismiss the dialog if needed
            }
        })

        // Create the Alert dialog
        val alertDialog: AlertDialog = builder.create()

        // Show the Alert Dialog box
        alertDialog.show()
    }













}