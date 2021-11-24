package co.edu.unipiloto.trucktrip3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_view_loadout.*

class View_Loadout : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_loadout)

        val user = Firebase.auth.currentUser
        user?.let {
            val email = user.email
            getAllDocuments(email.toString())
        }
    }

    private fun getAllDocuments (email: String){
        val spinner = findViewById<Spinner>(R.id.spinnerCarga)
        val arr = ArrayList<String>()

        db.collection("Cargas").get().addOnSuccessListener { basedatos ->
            for (documento in basedatos ){
                arr.add(documento.get("Name_Load").toString())
            }

            val adaptor = ArrayAdapter(this, android.R.layout.simple_spinner_item, arr)
            spinner.adapter = adaptor
        }

        RequeastTransportationButton.setOnClickListener() {

            val vr: String = spinnerCarga.selectedItem.toString()

            db.collection("Viajes").document().set(
                hashMapOf(
                    "Id" to email,
                    "Name_Load" to vr,
                    "From" to FromEditText.text.toString(),
                    "Until" to UntilEditText.text.toString()
                )
            )
            Toast.makeText(this, "Solicitud Realizada", Toast.LENGTH_SHORT).show();
            val intent = Intent(this, Loadout::class.java)
            startActivity(intent)
        }

    }
}