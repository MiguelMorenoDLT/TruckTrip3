package co.edu.unipiloto.trucktrip3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_create_load.*

class CreateLoad : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_load)

        val user = Firebase.auth.currentUser
        user?.let {
            val email = user.email
            save_load(email.toString())
        }
    }

    private fun save_load (email: String){

        SaveLoadButton.setOnClickListener {
            val cargaIntent = Intent(this, Loadout::class.java)
            if (NameLoadButton.text.isNotEmpty() && weightLoadEditText.text.isNotEmpty()){
                db.collection("Cargas").document().set(
                    hashMapOf(
                        "Id" to email,
                        "Name_Load" to NameLoadButton.text.toString(),
                        "Weight_Load" to weightLoadEditText.text.toString()
                    )
                )
                startActivity(cargaIntent)
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