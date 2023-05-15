package mx.itson.esfinge

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import mx.itson.esfinge.entidades.Visita
import mx.itson.esfinge.utilerias.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormVisitaActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener,
    View.OnClickListener {

    private var mapa: GoogleMap? = null
    private var lat: Double? = null
    private var lon: Double? = null
    private var textoLugar: String? = null
    private var textoMotivo: String? = null
    private var textoResponsable: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_visita)

        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapaMarker) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        btnGuardar.setOnClickListener(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            mapa = googleMap
            mapa!!.mapType = GoogleMap.MAP_TYPE_HYBRID

            val estaPermitido = ActivityCompat.checkSelfPermission(
                this, ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            if (estaPermitido) {
                mapa!!.isMyLocationEnabled = true
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(ACCESS_FINE_LOCATION), 200
                )
            }

            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                lat = location.latitude
                lon = location.longitude

                onLocationChanged(location)
            }
        } catch (ex: Exception) {
            Log.e("error al cargar el mapa", ex.toString())
        }
    }

    override fun onLocationChanged(location: Location) {

        val latitud = location.latitude
        val longitud = location.longitude

        val latLng = LatLng(latitud, longitud)
        mapa?.clear()
        mapa?.addMarker(MarkerOptions().position(latLng).draggable(true).title("Mi ubicación"))
        mapa?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mapa?.animateCamera(CameraUpdateFactory.zoomTo(15f))
        mapa?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {
                val a = 1 + 1
            }

            override fun onMarkerDrag(marker: Marker) {
                val a = 1 + 1
            }

            override fun onMarkerDragEnd(marker: Marker) {

                lat = marker.position.latitude
                lon = marker.position.longitude
            }
        })
    }

    override fun onClick(view: View?) {
        val btn = view as Button
        when (btn.id) {
            R.id.btnGuardar -> {
                try {
                    val lugar = findViewById<EditText>(R.id.etLugar)
                    val motivo = findViewById<EditText>(R.id.etMotivo)
                    val responsable = findViewById<EditText>(R.id.etResponsable)

                    textoLugar = lugar.text.toString()
                    textoMotivo = motivo.text.toString()
                    textoResponsable = responsable.text.toString()

                    val visita = Visita(textoLugar, textoMotivo, textoResponsable, lat, lon)

                    val call: Call<Visita> = RetrofitUtil.getApi()!!.createVisita(visita)
                    call.enqueue(object : Callback<Visita> {
                        override fun onResponse(
                            call: Call<Visita>,
                            response: Response<Visita>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@FormVisitaActivity,
                                    "Visita guardada",
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(
                                    this@FormVisitaActivity,
                                    "Error al guardar la visita",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<Visita>, t: Throwable) {
                            Log.e("Error call", t.message!!)
                        }
                    })
                } catch (ex: Exception) {
                    Log.e("Error", ex.message!!)
                }
            }
        }
    }
}