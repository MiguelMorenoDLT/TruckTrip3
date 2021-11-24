package co.edu.unipiloto.trucktrip3

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_driver.*
import kotlinx.android.synthetic.main.activity_driver.cerrarButton
import kotlinx.android.synthetic.main.activity_registration.*

class Driver : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver)

        val DriverText = findViewById<TextView>(R.id.driverText)
        val DriverTruck = findViewById<TextView>(R.id.driverTruckText)
        val DriverLoadout = findViewById<TextView>(R.id.driverloadoutText)

        db.collection("TripDriver").get().addOnSuccessListener { basedatos ->
            for (documento in basedatos ){

                DriverText.text = "Driver: " + documento.get("Name_Driver").toString() + " " + documento.get("Last_Name_Driver").toString()
                DriverTruck.text = "Truck: " + documento.get("Truck").toString() + " " + documento.get("Loading_Capacity").toString()
                DriverLoadout.text = "Load: " + documento.get("Name_Load").toString()
            }
        }

        TripButton.setOnClickListener() {
            val intent = Intent(this, TripsDriver::class.java)
            startActivity(intent)
        }

        ReportLocationButton.setOnClickListener{
            val intent = Intent(this, StatusActivityActivity::class.java)
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