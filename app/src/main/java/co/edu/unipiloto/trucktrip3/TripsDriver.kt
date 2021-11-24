package co.edu.unipiloto.trucktrip3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_driver.*
import kotlinx.android.synthetic.main.activity_trips_driver.*


class TripsDriver : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips_driver)

        val user = Firebase.auth.currentUser
        user?.let {
            val email = user.email
            Info(email.toString())
        }

        Cargabutton.setOnClickListener {

            val user = Firebase.auth.currentUser
            user?.let {
                val email = user.email

                db.collection("Status").document().set(
                    hashMapOf(
                        "Id" to email,
                        "Status" to "Status Load: On the way",
                    )
                )
            }
            val intent = Intent(this, Driver::class.java)
            startActivity(intent)
        }


    }
    private fun Info (email: String){

        val From = findViewById<TextView>(R.id.FromTextEdit)
        val Until = findViewById<TextView>(R.id.UntilTextEdit)
        val Carga = findViewById<TextView>(R.id.CargaTextEdit)

        db.collection("TripDriver").get().addOnSuccessListener { basedatos ->
            for (documento in basedatos) {
                if (documento.get("Email")?.equals(email) == true) {
                    From.text = documento.get("From").toString()
                    Until.text = documento.get("Until").toString()
                    Carga.text = documento.get("Name_Load").toString()
                }
            }
        }
    }

}