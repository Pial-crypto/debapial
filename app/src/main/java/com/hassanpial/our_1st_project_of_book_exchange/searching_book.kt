package com.hassanpial.our_1st_project_of_book_exchange


import BookAdapter
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
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


class searching_book : AppCompatActivity(),BookAdapter.OnItemClickListener {

    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookAdapter
    private lateinit var  go_to_send_message:Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searching_book)

        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recyclerView)

        // Initialize RecyclerView with an empty book list
        // Assuming you are using Firebase Realtime Database

// Example for Firebase Realtime Database
      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true)
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
                        // Create a Book object or do something with the retrieved data
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





                // Update the adapter with the new list of books for all users
               // adapter.updateBooks(allBooks)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }
        })


        // Set the click listener
       Handler(Looper.getMainLooper()).postDelayed({




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


       go_to_send_message= Intent(this,sending_message::class.java)





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
                        owner_name = snapshot.child("Name").value.toString()
                        owner_uid=snapshot.child("UID").value.toString()
                       var owner_location=snapshot.child("Location").value.toString()
                       // val email = snapshot.child("Email").getValue(String::class.java)
                        var dp=snapshot.child("Profile Picture").getValue(String::class.java)
                        go_to_send_message.putExtra("Owner",owner_name)
                        go_to_send_message.putExtra("dp",dp)
                        go_to_send_message.putExtra("UID",owner_uid)
                        go_to_send_message.putExtra("Location",owner_location)

                        //println("Name: $name, Email: $email")


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



        if(book.status=="Available to Exchange") {
showExchangeDialog("exchange",book.picture.toString(),book.name,book.genre,book.owner,book.bookid,book.author,book)


        }

      else  if(book.status=="Available to Sell") {
showExchangeDialog("buy",book.picture.toString(),book.name,book.genre,book.owner,book.bookid,book.author,book)

        }
      else if(book.status=="Available to Sell & Exchange"){

          showExchangeDialog("buy or exchange",book.picture.toString(),book.name,book.genre,book.owner,book.bookid,book.author,book)

      }
        else{

            Toast.makeText(this,"This book was sold",Toast.LENGTH_SHORT).show()
showAlertDialog(book)
        }



    }





    private fun showExchangeDialog(status:String,pic:String,name:String,genre:String,owner: String,bookid:String,author:String,book: Book) {
        val builder = AlertDialog.Builder(this)

        // Set the message for the Alert
        builder.setMessage("Do you agree to $status ?" )

        // Set Alert Title
        builder.setTitle("Alert!")

        // Set Cancelable false for when the user clicks outside the Dialog Box, then it will remain shown
        builder.setCancelable(false)

        // Set the positive button with "Yes" name
        builder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {

            // Assuming you have a map of key-value pairs representing the booked book information
            val bookedBookMap: Map<String, Any> = mapOf(
                "picture" to pic,
             "name" to name,

             "author" to author,
           "genre" to genre,
             "status" to status,
            "owner" to owner,
           "bookid" to bookid
                // Add more key-value pairs as needed
            )


            override fun onClick(dialog: DialogInterface?, which: Int) {
                // When the user clicks "Yes", you can add your logic here
                // For example, you can handle the exchange process
                var uid= FirebaseAuth.getInstance().uid.toString()
                FirebaseDatabase.getInstance().getReference("Registered users").child(uid).child("Booked books").child(bookid).setValue(bookedBookMap)







                // Get a reference to the root node "Registered users"
                val usersReference = FirebaseDatabase.getInstance().getReference("Registered users")

// Retrieve the list of uids
                usersReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(usersSnapshot: DataSnapshot) {
                        for (userSnapshot in usersSnapshot.children) {
                            val uid = userSnapshot.key

                            // Get a reference to the "Booked Books" node for the current uid
                            val bookedBooksReference = usersReference.child(uid!!).child("Booked books")

                            // Retrieve the list of book ids for the current uid
                            bookedBooksReference.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(booksSnapshot: DataSnapshot) {
                                    for (bookSnapshot in booksSnapshot.children) {
                                        val bookId = bookSnapshot.key

                                        // Check if the current bookId meets your condition
                                        if (bookId == bookId) {
                                            // Update the status or perform any other actions
                                            val status = bookSnapshot.child("status").getValue(String::class.java)
                                            if (status != null) {
                                                // Update the status or perform any other actions
                                                // For example, setting status to "Updated"
                                                bookedBooksReference.child(bookId.toString()).child("status").setValue("Already exchanged")
                                            }
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Handle error
                                }
                            })
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error
                    }
                })


///another dialogue




                startActivity(go_to_send_message)
//showAlertDialog(book)



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

                            val reviewView = Intent(this@searching_book, Viewing_reviews::class.java)
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
















