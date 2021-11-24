package co.edu.unipiloto.trucktrip3

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.cerrarButton
import kotlinx.android.synthetic.main.activity_truck_manager.*

class TruckManager : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_truck_manager)

        db.collection("Status").get().addOnSuccessListener { basedatos ->
            for (documento in basedatos ){
                val Status = findViewById<TextView>(R.id.StatusLoadTextView)
                Status.text = documento.get("Status").toString()
            }
        }


        tripsButton.setOnClickListener(){
            val intent = Intent(this, See_transports::class.java)
            startActivity(intent)
        }

        RegisterTruckButton.setOnClickListener(){
            val intent = Intent(this, Register_Truck::class.java)
            startActivity(intent)
        }

        cerrarButton.setOnClickListener{

            val prefs = getSharedPreferences(getString(R.string.pr), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}