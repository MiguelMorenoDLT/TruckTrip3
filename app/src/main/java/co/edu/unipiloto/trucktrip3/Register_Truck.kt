package co.edu.unipiloto.trucktrip3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register_truck.*

class Register_Truck : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_truck)


        val user = Firebase.auth.currentUser
        user?.let {
            val email = user.email
            save_truck(email.toString())
        }

    }

    private fun save_truck (email: String){

        SaveTruckButton.setOnClickListener {
            val IntentTruck = Intent(this, TruckManager::class.java)
            if (MarcaCamionEditText.text.isNotEmpty() && ModelTruckEditText.text.isNotEmpty() && ColorEditText.text.isNotEmpty() && LoadingCapacityEditText.text.isNotEmpty()){
                db.collection("Camiones").document().set(
                    hashMapOf(
                        "Id" to email,
                        "Brand_Truck" to MarcaCamionEditText.text.toString(),
                        "Model_Truck" to ModelTruckEditText.text.toString(),
                        "Color_Truck" to ColorEditText.text.toString(),
                        "Loading_Capacity" to LoadingCapacityEditText.text.toString()
                    )
                )
                startActivity(IntentTruck)
            }else{
                showAlert()
            }
        }
    }
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error de autenticación")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}