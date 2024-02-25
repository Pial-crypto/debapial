package com.hassanpial.our_1st_project_of_book_exchange

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage


class Editing_Profile : AppCompatActivity() {
    private var selectedImageUri: Uri? =null
    // Declare variables at the class level
   private lateinit  var editprofileimg:ImageView
    var userok=true
private  final  var gallery_request_code=1
    var currentUID=FirebaseAuth.getInstance().uid.toString()
    private lateinit var auth: FirebaseAuth


    //private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap
    //private lateinit var placesClient: PlacesClient
    private lateinit var searchEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editing_profile)
       // val binding= ActivityMainBinding.inflate(layoutInflater)
      // setContentView(binding.root)
       // var currentLocationname="NO"
        val editText = findViewById<EditText>(R.id.editTextSearchLocation)


       var currentlocationbutton=findViewById<Button>(R.id.buttonSetCurrentLocation)
        if(intent.getStringExtra("CITY_NAME")!=null) {
            var currentLocationname = intent.getStringExtra("CITY_NAME").toString()

            editText.text = Editable.Factory.getInstance().newEditable(currentLocationname)
        }
        currentlocationbutton.setOnClickListener(){

startActivity(Intent(this,MapsActivity::class.java))

        }

        findViewById<Button>(R.id.buttonSearchLocation).setOnClickListener(){
             var map2=Intent(this,MapsActivity2::class.java)
            map2.putExtra("desiredcity",findViewById<EditText>(R.id.editTextSearchLocation).text.toString())
            startActivity(map2)
          //  Toast.makeText(this,"asdfas",Toast.LENGTH_SHORT).show()
        }
        searchEditText = findViewById(R.id.editTextSearchLocation)

        // Initialize Places API
      //  if (!Places.isInitialized()) {
        //    Places.initialize(applicationContext, "AIzaSyCMMn1FmVXpOh4ed27_xdYfneFXsdahaTY")
       // }
      //  placesClient = Places.createClient(this)




       // fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)










        // Declare and initialize views with var and findViewById
        var passedit = findViewById<TextInputEditText>(R.id.passedit)
        var confirm_passedit = findViewById<TextInputEditText>(R.id.confirmpassedit)
        var edit_phone = findViewById<TextInputEditText>(R.id.phoneEditText)
        var editname = findViewById<TextInputEditText>(R.id.nameedit)
          editprofileimg = findViewById<ImageView>(R.id.profileImageView)
        var editprofilebtn = findViewById<Button>(R.id.editPictureButton)
        var save = findViewById<Button>(R.id.saveButton)
        var editbuttonpass = findViewById<ImageButton>(R.id.editButtonpassword)
        var editButtonconfirmpass = findViewById<ImageButton>(R.id.editButtonconfirmpass)
        var editButtonphone = findViewById<ImageButton>(R.id.editButtonphone)
        var editButtonname = findViewById<ImageButton>(R.id.editButtonname)


SetupExistingDatas()

        editprofilebtn.setOnClickListener(){
            val gallery= Intent(Intent.ACTION_PICK)
            gallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery,gallery_request_code)

            //startActivityForResult(gallery_request_code,gallery)
        }


if(!userok)
        Toast.makeText(this, "User data empty", Toast.LENGTH_SHORT).show()


        editButtonname.setOnClickListener(){
            editname.isClickable=true
            editname.isFocusable=true
            editname.isFocusableInTouchMode=true;
            editname.requestFocus()



            passedit.isClickable=false
            passedit.isFocusable=false
            passedit.isFocusableInTouchMode=false;



            edit_phone.isClickable=false
            edit_phone.isFocusable=false
            edit_phone.isFocusableInTouchMode=false;



            confirm_passedit.isClickable=false
            confirm_passedit.isFocusable=false
            confirm_passedit.isFocusableInTouchMode=false;
        }




        editbuttonpass.setOnClickListener(){
   passedit.isClickable=true
    passedit.isFocusable=true
    passedit.isFocusableInTouchMode=true;
   passedit.requestFocus()


            editname.isClickable=false
            editname.isFocusable=false
            editname.isFocusableInTouchMode=false;



    edit_phone.isClickable=false
    edit_phone.isFocusable=false
    edit_phone.isFocusableInTouchMode=false;



    confirm_passedit.isClickable=false
    confirm_passedit.isFocusable=false
    confirm_passedit.isFocusableInTouchMode=false;
}



        editButtonconfirmpass.setOnClickListener(){
            confirm_passedit.isClickable=true
            confirm_passedit.isFocusable=true
            confirm_passedit.isFocusableInTouchMode=true;
            confirm_passedit.requestFocus()

            editname.isClickable=false
            editname.isFocusable=false
            editname.isFocusableInTouchMode=false;




            edit_phone.isClickable=false
            edit_phone.isFocusable=false
            edit_phone.isFocusableInTouchMode=false;

            passedit.isClickable=false
            passedit.isFocusable=false
            passedit.isFocusableInTouchMode=false;


        }




        editButtonphone.setOnClickListener(){
            edit_phone.isClickable=true
            edit_phone.isFocusable=true
            edit_phone.isFocusableInTouchMode=true;
            edit_phone.requestFocus()

            editname.isClickable=false
            editname.isFocusable=false
            editname.isFocusableInTouchMode=false;



            passedit.isClickable=false
            passedit.isFocusable=false
            passedit.isFocusableInTouchMode=false


            confirm_passedit.isClickable=false
            confirm_passedit.isFocusable=false
            confirm_passedit.isFocusableInTouchMode=false



        }

findViewById<Button>(R.id.removePictureButton).setOnClickListener(){
    editprofileimg.setImageResource(R.drawable.blankpp)
}




        save.setOnClickListener(){

findViewById<ProgressBar>(R.id.savechangepb).visibility= View.VISIBLE
            val storageRef = FirebaseStorage.getInstance().reference.child("profile_images")
            val imageRef = storageRef.child("$currentUID.jpg")



var name=editname.text.toString()
            var password=passedit.text.toString()
            var phone=edit_phone.text.toString()
var confirm_password=confirm_passedit.text.toString()




            var canwego = true


            ///checking the validity of name
            if (name.isEmpty()) {
                canwego = false
                findViewById<ProgressBar>(R.id.savechangepb).visibility= View.INVISIBLE
               editname.setError("Enter your name");
                editname.requestFocus();
                return@setOnClickListener;
            }

            if (name.length < 4)
            { findViewById<ProgressBar>(R.id.savechangepb).visibility= View.INVISIBLE
                canwego = false
                editname.setError("Name is too short.Enter at least 6 characters");
                editname.requestFocus();
                return@setOnClickListener;
            }




            ///checking the validity of phone
            if (phone.isEmpty()) {
                canwego = false
                findViewById<ProgressBar>(R.id.savechangepb).visibility= View.INVISIBLE
                edit_phone.setError("Enter your phone number");
                edit_phone.requestFocus();
                return@setOnClickListener;
            }

            if (phone.length != 11 || phone[0] != '0' || phone[1] != '1') { findViewById<ProgressBar>(R.id.savechangepb).visibility= View.INVISIBLE
                canwego = false
                edit_phone.setError("Enter a valid number");
                edit_phone.requestFocus();
                return@setOnClickListener;
            }




            //checking the validity of the password
            if (password.isEmpty()) { findViewById<ProgressBar>(R.id.savechangepb).visibility= View.INVISIBLE
                canwego = false
                passedit.setError("Enter a password");
                passedit.requestFocus();
                return@setOnClickListener;
            }

            if (password.length < 5) {
                findViewById<ProgressBar>(R.id.savechangepb).visibility= View.INVISIBLE
                canwego = false
                passedit.setError("Password must have at least 6 characters");
                passedit.requestFocus();
                return@setOnClickListener;

            }






            ///checking the validity of confirm password


            if (confirm_password.isEmpty()) {  findViewById<ProgressBar>(R.id.savechangepb).visibility= View.INVISIBLE
                canwego = false
                confirm_passedit.setError("Enter a password");
                confirm_passedit.requestFocus();
                return@setOnClickListener;
            }

            if (confirm_password.length < 5) {
                findViewById<ProgressBar>(R.id.savechangepb).visibility= View.INVISIBLE
                canwego = false
                confirm_passedit.setError("Password must have at least 6 characters");
                confirm_passedit.requestFocus();
                return@setOnClickListener;
            }


            if (password != confirm_password) {
                findViewById<ProgressBar>(R.id.savechangepb).visibility= View.INVISIBLE
                Toast.makeText(
                this,
                "Password didnt match",
                Toast.LENGTH_SHORT
            ).show();}

   else {
// Check if an image is selected
                if (selectedImageUri != null) {
                    // Upload the image to Firebase Storage
                    imageRef.putFile(selectedImageUri!!)
                        .addOnSuccessListener { taskSnapshot ->
                            // Get the download URL
                            imageRef.downloadUrl
                                .addOnSuccessListener { uri ->
                                    val imageUrl = uri.toString()

                                    // Update the userdata map with the image URL
                                    val userdata = mapOf(
                                        "Profile Picture" to imageUrl,
                                        "Name" to name,
                                        "Password" to password,
                                        "Phone" to phone,
                                        "Location" to editText.text
                                    )

                                    // Update the user data in the Realtime Database
                                    FirebaseDatabase.getInstance().getReference("Registered users")
                                        .child(currentUID)
                                        .updateChildren(userdata)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                this@Editing_Profile,
                                                "Saved Successfully",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            // Continue with any other logic you need after saving the data
                                            startActivity(Intent(this@Editing_Profile, loggedin_home_page::class.java))
                                        }
                                        .addOnFailureListener {
                                            findViewById<ProgressBar>(R.id.savechangepb).visibility= View.INVISIBLE
                                            Toast.makeText(
                                                this@Editing_Profile,
                                                "Failed to save ",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                }
                                .addOnFailureListener { exception ->
                                    findViewById<ProgressBar>(R.id.savechangepb).visibility= View.INVISIBLE
                                    // Handle the failure to get download URL
                                    Toast.makeText(
                                        this@Editing_Profile,
                                        "Failed to get image URL",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }

                }



              else {

                    val userdata = mapOf(
                        "Profile Picture" to null,
                        "Name" to name,
                        //"adfasd" to "bafasd",
                        //  "Email" to email,
                        "Password" to password,
                        "Phone" to phone,
                        "Location" to editText.text

                        //"UID" to uid
                    )

                    FirebaseDatabase.getInstance().getReference("Registered users")
                        .child(currentUID)
                        .updateChildren(userdata).addOnSuccessListener {
                            Toast.makeText(this, "Saved Successfully", Toast.LENGTH_LONG).show()
                        }.addOnFailureListener() {
                            findViewById<ProgressBar>(R.id.savechangepb).visibility= View.INVISIBLE
                            Toast.makeText(this, "Failed to save ", Toast.LENGTH_LONG).show()
                        }


                    startActivity(Intent(this, loggedin_home_page::class.java))
               }
            }
        }



    }





   fun SetupExistingDatas(){
       FirebaseDatabase.getInstance().getReference("Registered users").child(currentUID)
           .addValueEventListener(object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                   // Handle the data when it changes
                   if (snapshot.exists()) {
                       // The snapshot contains the data for the specified user
                       val name = snapshot.child("Name").value.toString()
                       val email = snapshot.child("Email").getValue(String::class.java)
var dp=snapshot.child("Profile Picture").getValue(String::class.java)
                       val password=snapshot.child("Password").getValue(String::class.java)
                       val phone=snapshot.child("Phone").getValue(String::class.java)
                    //   var locationname=snapshot.child("Location").getValue(String::class.java)
                       findViewById<TextInputEditText>(R.id.passedit).setText(password)
                       findViewById<TextInputEditText>(R.id.confirmpassedit).setText(password)
                       findViewById<TextInputEditText>(R.id.phoneEditText).setText(phone)
                      findViewById<TextInputEditText>(R.id.nameedit).setText(name)
                       val editText = findViewById<EditText>(R.id.editTextSearchLocation)
                      // editText.text = Editable.Factory.getInstance().newEditable(locationname)
                       //println("Name: $name, Email: $email")

                       if (dp != null) {
                           // Load the image into an ImageView using Glide (or any other method you prefer)
                           Glide.with(applicationContext)
                               .load(dp)
                               .into(editprofileimg)

                       } else {
                           editprofileimg.setImageResource(R.drawable.blankpp)

                           // Handle the case where the "Profile Picture" field is null or doesn't exist
                           // You might want to use a default image or show an error placeholder
                           // imageView.setImageResource(R.drawable.default_profile_image)
                           // or
                           // handle the error appropriately8/
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


}





    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == gallery_request_code && resultCode == RESULT_OK && data != null) {
           // selectedImageUri = data.data
            // You can set the selected image to your ImageView
           editprofileimg.setImageURI(data.data)
            selectedImageUri=data.data

        }
    }

















}






