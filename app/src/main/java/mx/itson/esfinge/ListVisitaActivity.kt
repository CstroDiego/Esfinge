package mx.itson.esfinge

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import mx.itson.esfinge.entidades.Visita
import mx.itson.esfinge.utilerias.RetrofitUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Muestra los detalles de una visita
 *
 * @constructor Crea una nueva instancia de la actividad
 *
 * @author Diego Castro Arce
 */
class ListVisitaActivity : AppCompatActivity(), OnMapReadyCallback {
    /**
     * Mapa de google
     */
    private var mapa: GoogleMap? = null

    /**
     * Id de la visita en la base de datos
     */
    private var visitaId: Int? = null

    /**
     * Latitud de la visita
     */
    private var visitaLat: Double? = null

    /**
     * Longitud de la visita
     */
    private var visitaLon: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_visita)
        visitaId = intent.getIntExtra("visitaId", 0)

        val mapaFragment =
            supportFragmentManager.findFragmentById(R.id.ubicacion) as SupportMapFragment?
        mapaFragment!!.getMapAsync(this)

        obtenerVisita(visitaId!!)

        val btnCerrar = findViewById<Button>(R.id.btnCerrar)
        btnCerrar.setOnClickListener {
            finish()
        }
    }

    /**
     * Obtiene una visita de la base de datos por su id
     *
     * @param visitaId Id de la visita
     */
    private fun obtenerVisita(visitaId: Int) {
        val call: Call<Visita> = RetrofitUtil.getApi()!!.getVisita(visitaId)
        call.enqueue(object : Callback<Visita> {
            override fun onResponse(call: Call<Visita>, response: Response<Visita>) {
                if (response.isSuccessful) {
                    val visita: Visita? = response.body()
                    if (visita != null) {
                        val txtLugar = findViewById<TextView>(R.id.txtLugar)
                        val txtMotivo = findViewById<TextView>(R.id.txtMotivo)
                        val txtResponsable = findViewById<TextView>(R.id.txtResponsable)

                        txtLugar.text = visita.lugar.toString()
                        txtMotivo.text = visita.motivo.toString()
                        txtResponsable.text = visita.responsable.toString()

                        visitaLat = visita.latitud.toString().toDouble()
                        visitaLon = visita.longitud.toString().toDouble()

                        actualizarMapa()
                    }
                } else {
                    Log.e("Error al respuesta visita", response.message())
                }
            }

            override fun onFailure(call: Call<Visita>, t: Throwable) {
                Log.e("Error al obtener visita", t.toString())
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            mapa = googleMap
            mapa!!.mapType = GoogleMap.MAP_TYPE_HYBRID
        } catch (ex: Exception) {
            Log.e("Error al cargar el mapa", ex.toString())
        }
    }

    /**
     * Actualiza el mapa con la ubicación de la visita
     *
     */
    private fun actualizarMapa() {
        if (visitaLat != null && visitaLon != null) {
            val visitaLatLng = LatLng(visitaLat!!, visitaLon!!)

            mapa?.clear()
            mapa?.addMarker(
                MarkerOptions().position(visitaLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cheems))
            )
            val zoomLevel = 17f
            mapa?.moveCamera(CameraUpdateFactory.newLatLngZoom(visitaLatLng, zoomLevel))
            mapa?.uiSettings?.setAllGesturesEnabled(false)
        } else {
            Log.e("Error al actualizar el mapa", "No se encontró la ubicación de la visita")
        }
    }
}