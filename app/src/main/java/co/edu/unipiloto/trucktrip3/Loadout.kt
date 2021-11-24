package co.edu.unipiloto.trucktrip3

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_loadout.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.cerrarButton

class Loadout : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loadout)

        val user = Firebase.auth.currentUser
        user?.let {
            val email = user.email
            loadoutText.text = email
        }

        cerrarButton.setOnClickListener{

            val prefs = getSharedPreferences(getString(R.string.pr), Context.MODE_PRIVATE).edit()
            prefs.clear()
            prefs.apply()

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        loadoutButton.setOnClickListener{
            val intent = Intent(this, View_Loadout::class.java)
            startActivity(intent)
        }

        createLoadButton.setOnClickListener{
            val intent = Intent(this, CreateLoad::class.java)
            startActivity(intent)
        }

        locationButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)

        }
    }
}