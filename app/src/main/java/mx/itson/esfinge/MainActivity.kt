package mx.itson.esfinge

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import mx.itson.esfinge.entidades.Visita
import mx.itson.esfinge.utilerias.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad principal de la aplicación
 *
 * @constructor Crear la actividad principal
 *
 * @author Diego Castro Arce
 */
class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    /**
     * Mapa de google
     */
    var mapa: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapaFragment = supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment?
        mapaFragment!!.getMapAsync(this)

        val btnAgregarMarcador = findViewById<FloatingActionButton>(R.id.btnAgregarMarcador)
        btnAgregarMarcador.setOnClickListener {
            val intent = Intent(this, FormVisitaActivity::class.java)
            startActivity(intent)
        }
        obtenerMarcadores()
    }

    /**
     * Obtiene todos los registros de visitas de la base de datos y los muestra en el mapa
     *
     */
    private fun obtenerMarcadores() {
        // Obtener todas las visitas
        val call: Call<List<Visita>>? = RetrofitUtil.getApi()?.getVisitas()
        call?.enqueue(object : Callback<List<Visita>> {
            override fun onResponse(call: Call<List<Visita>>, response: Response<List<Visita>>) {
                if (response.isSuccessful) {
                    val visitas: List<Visita>? = response.body()
                    visitas?.let {
                        mapa?.clear()
                        // Agregar marcadores al mapa
                        for (v in it) {
                            val latLng = LatLng(v.latitud ?: 0.0, v.longitud ?: 0.0)
                            val marker = mapa?.addMarker(
                                MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cheems))
                            )
                            // Asignar el id de la visita al marcador
                            marker?.tag = v.id
                        }
                    }
                } else {
                    Log.e("Error al obtener visitas", response.message())
                }
            }

            override fun onFailure(call: Call<List<Visita>>, t: Throwable) {
                Log.e("Error al obtener visitas", t.toString())
            }
        })
    }


    override fun onMapReady(googleMap: GoogleMap) {
        // Trata de asignar el mapa de google
        try {
            mapa = googleMap
            mapa!!.mapType = GoogleMap.MAP_TYPE_HYBRID
            mapa!!.setOnMarkerClickListener(this)
        } catch (ex: Exception) {
            Log.e("Error al cargar el mapa", ex.toString())
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        // Obtener el id de la visita del marcador
        val visitaId = marker.tag as? Int
        if (visitaId != null) {
            Toast.makeText(this, "Cargando datos...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, ListVisitaActivity::class.java)
            intent.putExtra("visitaId", visitaId)
            startActivity(intent)
            return true
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        obtenerMarcadores()
    }
}
