package co.edu.unipiloto.trucktrip3

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.login_activity_main.*

class LoginActivity : AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_main)

        val analytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message", "Integracion de Firebase completa")
        analytics.logEvent("InitScreen", bundle)

        setup()
        session()
    }

    override fun onStart() {
        super.onStart()
        firstLayout.visibility = View.VISIBLE
    }

    private fun session(){
        val prefs = getSharedPreferences(getString(R.string.pr), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if (email != null && provider != null){
            firstLayout.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }
    }

    private fun setup(){

        title = "Autenticación"

        registrarButton.setOnClickListener{
            if (emailText.text.isNotEmpty() && passwordText.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailText.text.toString(), passwordText.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    }else{
                        showAlert()
                    }
                }
            }
        }
        entrarButton.setOnClickListener {
            if (emailText.text.isNotEmpty() && passwordText.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailText.text.toString(), passwordText.text.toString()).addOnCompleteListener{
                    if (it.isSuccessful){
                        user(it.result?.user?.email ?: "")
                    }else{
                        showAlert()
                    }
                }
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

    private fun showHome(email: String, provider: ProviderType){

        val registrationIntent = Intent(this, RegistrationActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(registrationIntent)
    }

    private fun user(email: String){

        db.collection("users").document(email).get().addOnSuccessListener { basedatos ->
            if (basedatos.exists()){
                val cargo: String? = basedatos.getString("Primary_Key")

                if (cargo.equals("Conductor")){

                    val intent = Intent(this, Driver::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();

                }
                if (cargo.equals("Propietario De Camion")){

                    val intent = Intent(this, TruckManager::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();

                }
                if (cargo.equals("Propietario De Carga")){

                    val intent = Intent(this, Loadout::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Login exitoso", Toast.LENGTH_SHORT).show();
                }

            }else{
                showAlert()
            }

        }

    }
}