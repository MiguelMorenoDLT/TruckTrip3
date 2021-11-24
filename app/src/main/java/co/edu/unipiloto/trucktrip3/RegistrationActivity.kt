package co.edu.unipiloto.trucktrip3

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registration.*

enum class ProviderType{
    BASIC
}

class RegistrationActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")
        setup(email ?: "", provider ?: "")

        //Guardado de datos

        val prefs = getSharedPreferences(getString(R.string.pr), Context.MODE_PRIVATE).edit()
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()
    }
    private fun setup(email: String, provider: String) {

        title = "Inicio"

        val spinner = findViewById<Spinner>(R.id.idSpinner)
        val lista = resources.getStringArray(R.array.user_type)

        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item, lista)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long) {
                Toast.makeText(this@RegistrationActivity, lista[p2], Toast.LENGTH_LONG).show()

                val vr: String = lista.get(p2).toString()

                guardarButton.setOnClickListener{

                    db.collection("users").document(email).set(
                        hashMapOf(
                            "Primary_Key" to vr,
                            "Provider" to provider,
                            "First_name" to nameText.text.toString(),
                            "Last_name" to lastnameText.text.toString(),
                            "Id" to cedulaText.text.toString(),
                            "Address" to addressText.text.toString(),
                            "Phone" to phoneText.text.toString())
                    )
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        emailtextView.text = email
        provedortextView.text = provider

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