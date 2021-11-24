package co.edu.unipiloto.trucktrip3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_see_transports.*

class See_transports : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_see_transports)

        val spinner = findViewById<Spinner>(R.id.SpinnerTrips)
        val spinnerCamion = findViewById<Spinner>(R.id.SpinnerCamion)
        val spinnerConductor = findViewById<Spinner>(R.id.SpinnerConductor)

        val arr = ArrayList<String>()
        val arrC = ArrayList<String>()
        val arrD = ArrayList<String>()

        //Viajes
        db.collection("Viajes").get().addOnSuccessListener { basedatos ->
            for (documento in basedatos ){
                arr.add("Carga: "+ documento.get("Name_Load").toString() + " Viaje: "+documento.get("From").toString() + "-" + documento.get("Until").toString())
            }

            val adaptor = ArrayAdapter(this, android.R.layout.simple_spinner_item, arr)
            spinner.adapter = adaptor
        }
        //Camiones
        db.collection("Camiones").get().addOnSuccessListener { basedatos ->
            for (documento in basedatos ){
                arrC.add("Camión: "+ documento.get("Brand_Truck").toString() + " Capacidad: "+documento.get("Loading_Capacity").toString())
            }

            val adaptor = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrC)
            spinnerCamion.adapter = adaptor
        }
        //Conductor
        db.collection("users").get().addOnSuccessListener { basedatos ->
            for (documento in basedatos ){
                if (documento.get("Primary_Key")?.equals("Conductor") == true){
                    arrD.add(documento.get("First_name").toString() +" "+documento.get("Last_name").toString())
                }
            }
            val adaptor = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrD)
            spinnerConductor.adapter = adaptor
        }

        AceptarButton.setOnClickListener(){

            val carga : String
            val vr: String = SpinnerTrips.selectedItem.toString()
            carga = (vr.split(" ")[1])
            var p : String = SpinnerCamion.selectedItem.toString()
            val cam = (p.split(" ")[1])
            val camPeso = (p.split(" ")[3]) +" "+ (p.split(" ")[4])

            //Camiones
            db.collection("Camiones").get().addOnSuccessListener { basedatos ->
                for (documento in basedatos) {
                    if (documento.get("Brand_Truck")?.equals(cam) == true && documento.get("Loading_Capacity")?.equals(camPeso) == true) {
                        //pasar a otro y borrarlo
                        db.collection("InfoCamionDriver").document().set(
                            hashMapOf(
                                "Id" to documento.get("Id"),
                                "Brand_Truck" to documento.get("Brand_Truck"),
                                "Model_Truck" to documento.get("Model_Truck"),
                                "Color_Truck" to documento.get("Color_Truck"),
                                "Loading_Capacity" to documento.get("Loading_Capacity"))
                        )
                        db.collection("Camiones").document(documento.id).delete()
                    }
                }
            }

            var conductor : String = SpinnerConductor.selectedItem.toString()

            var correo : String = ""
            var nombre = (conductor.split(" ")[0])
            var apellido = (conductor.split(" ")[2])


            //Usuario
            db.collection("users").get().addOnSuccessListener { basedatos ->
                for (documento in basedatos) {
                    println("Este es la primary key "+documento.get("Primary_Key"))
                    if (documento.get("Primary_Key")?.equals("Conductor") == true){
                        correo = documento.id
                    }
                }
            }


            //Viajes
            db.collection("Viajes").get().addOnSuccessListener { basedatos ->
                for (documento in basedatos ){

                    if (documento.get("Name_Load")?.equals(carga) == true) {
                        db.collection("TripDriver").document().set(
                            hashMapOf(
                                "From" to documento.get("From"),
                                "Until" to documento.get("Until"),
                                "Name_Load" to documento.get("Name_Load"),
                                "Id" to documento.get("Id"),
                                "Truck" to cam,
                                "Loading_Capacity" to camPeso,
                                "Name_Driver" to nombre,
                                "Last_Name_Driver" to apellido,
                                "Email" to correo)
                        )
                        db.collection("Viajes").document(documento.id).delete()
                    }
                }
            }
            val intent = Intent(this, TruckManager::class.java)
            startActivity(intent)
            Toast.makeText(this, "Carga reservada exitosamente", Toast.LENGTH_SHORT).show();
        }
    }
}