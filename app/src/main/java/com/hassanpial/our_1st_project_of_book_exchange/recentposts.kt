package com.hassanpial.our_1st_project_of_book_exchange

import BookAdapter
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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

class recentposts :AppCompatActivity() , BookAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BookAdapter
    private lateinit var searchView: SearchView
    val allBooks= mutableListOf<Book>()
    var currentUID=FirebaseAuth.getInstance().uid.toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recentposts)




        recyclerView = findViewById(R.id.recyclerView)
         searchView = findViewById(R.id.searchView)
        val databaseReference =    FirebaseDatabase.getInstance().getReference("Registered users")
var i=0;

        FirebaseDatabase.getInstance().getReference("Registered users").child(  FirebaseAuth.getInstance().uid.toString()).child("Book")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Handle the data when it changes
                    if (snapshot.exists()) {

                        for (bookSnapshot in snapshot.children) {
                            if(i==5) break
i++
                           // if (bookSnapshot.child("Status").value.toString() == "Available to Exchange" ||bookSnapshot.child("Status").value.toString() == "Available to Sell & Exchange" || bookSnapshot.child("Status").value.toString()=="Already exchanged"){
                                val author = bookSnapshot.child("Author").value.toString()
                                val genre = bookSnapshot.child("Genre").value.toString()
                                val name = bookSnapshot.child("Name").value.toString()
                                val picture = bookSnapshot.child("Picture of book").value.toString()
                                val status = bookSnapshot.child("Status").value.toString()
                                val owner = bookSnapshot.child("Owner").value.toString()
                                var bookid=bookSnapshot.child("Book id").value.toString()
                                val book = Book(
                                    picture,
                                    name,
                                    author,
                                    genre,
                                    status,
                                    owner,
                                    bookid

                                )

                                allBooks.add(book)
                            //}
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





        Handler().postDelayed({
            recyclerView.layoutManager = LinearLayoutManager(this)
            adapter = BookAdapter(allBooks)

            // Set the click listener
            //  adapter.setOnItemClickListener(this)

            recyclerView.adapter = adapter
            adapter.setOnItemClickListener(this)

        } , 2000)

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
        //  Log.d("BookAdapter", "Item clicked at position $position with book name ${book.bookid}")
        // var bookstatus="eredr"
        var status= FirebaseDatabase.getInstance().getReference("Registered users").child("Book").child(book.bookid).child("Status")
       if(status.toString()=="Available to Exchange" ){
           Toast.makeText(this,"This book is not exchanged yet",Toast.LENGTH_SHORT).show()
       }
        else if(status.toString()=="Available to Sell"){
           Toast.makeText(this,"This book is not sold yet",Toast.LENGTH_SHORT).show()
       }

        else if(status.toString()=="Available to Sell & Exchange"){
           Toast.makeText(this,"This book is not sold or exchanged yet",Toast.LENGTH_SHORT).show()
       }
        else{
           Toast.makeText(this,"This book is sent to the buyer",Toast.LENGTH_SHORT).show()
       }



      //  showAlertDialog(book)


        // Assuming you have the position of the item you want to update


        //allBooks.removeAt(position)



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

                            val reviewView = Intent(this@recentposts, Viewing_reviews::class.java)
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