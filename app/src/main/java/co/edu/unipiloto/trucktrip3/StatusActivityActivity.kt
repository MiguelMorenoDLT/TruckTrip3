package co.edu.unipiloto.trucktrip3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.status_activity.*

class StatusActivityActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        title = "Status of lodoaut"

        super.onCreate(savedInstanceState)
        setContentView(R.layout.status_activity)

        confirmarButton.setOnClickListener {

        }
    }
}