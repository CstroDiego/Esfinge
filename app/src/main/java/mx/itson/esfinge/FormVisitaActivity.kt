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

/**
 * Actividad para el formulario de visita
 *
 * @constructor Crear un formulario de visita
 *
 * @author Diego Castro Arce
 */
class FormVisitaActivity : AppCompatActivity(), OnMapReadyCallback, LocationListener,
    View.OnClickListener {

    /**
     * Mapa de google
     */
    private var mapa: GoogleMap? = null

    /**
     * Latitud de la visita
     */
    private var lat: Double? = null

    /**
     * Longitud de la visita
     */
    private var lon: Double? = null

    /**
     * Lugar de la visita
     */
    private var textoLugar: String? = null

    /**
     * Motivo de la visita
     */
    private var textoMotivo: String? = null

    /**
     * Responsable de la visita
     */
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
            // Inicializar mapa
            mapa = googleMap
            mapa!!.mapType = GoogleMap.MAP_TYPE_HYBRID

            // Verificar permisos
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

            // Obtener ubicacion
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

        // Obtener ubicacion
        val latitud = location.latitude
        val longitud = location.longitude

        // Agregar marcador
        val latLng = LatLng(latitud, longitud)
        mapa?.clear()
        mapa?.addMarker(MarkerOptions().position(latLng).draggable(true).title("Mi ubicación"))
        mapa?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mapa?.animateCamera(CameraUpdateFactory.zoomTo(15f))
        mapa?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {
                1 + 1
            }

            override fun onMarkerDrag(marker: Marker) {
               1 + 1
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
                    // Obtener datos
                    val lugar = findViewById<EditText>(R.id.etLugar)
                    val motivo = findViewById<EditText>(R.id.etMotivo)
                    val responsable = findViewById<EditText>(R.id.etResponsable)

                    // Guardar datos de visita
                    textoLugar = lugar.text.toString()
                    textoMotivo = motivo.text.toString()
                    textoResponsable = responsable.text.toString()

                    // Validar datos
                    val visita = Visita(textoLugar, textoMotivo, textoResponsable, lat, lon)

                    // Guardar visita
                    val call: Call<Visita> = RetrofitUtil.getApi()!!.createVisita(visita)
                    call.enqueue(object : Callback<Visita> {
                        override fun onResponse(
                            call: Call<Visita>,
                            response: Response<Visita>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@FormVisitaActivity,
                                    getString(R.string.texto_visita_guardada),
                                    Toast.LENGTH_LONG
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(
                                    this@FormVisitaActivity,
                                    getString(R.string.texto_error_al_guardar_la_visita),
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