package mx.itson.esfinge

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ListVisitaActivity : AppCompatActivity(), OnMapReadyCallback {
    var mapa: GoogleMap? = null
    private var visitaId: Int? = null
    private var visitaLugar: String? = null
    private var visitaMotivo: String? = null
    private var visitaResponsable: String? = null
    private var visitaLat: Double? = null
    private var visitaLon: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_visita)
        visitaId = intent.getIntExtra("visitaId", 0)
        visitaLugar = intent.getStringExtra("visitaLugar")
        visitaMotivo = intent.getStringExtra("visitaMotivo")
        visitaResponsable = intent.getStringExtra("visitaResponsable")
        visitaLat = intent.getDoubleExtra("visitaLatitud", 0.0)
        visitaLon = intent.getDoubleExtra("visitaLongitud", 0.0)

        val mapaFragment =
            supportFragmentManager.findFragmentById(R.id.ubicacion) as SupportMapFragment?
        mapaFragment!!.getMapAsync(this)

        cargarDatos(visitaLugar!!, visitaMotivo!!, visitaResponsable!!)

    }

    private fun cargarDatos(lugar: String, motivo: String, responsable: String) {
        val txtLugar = findViewById<TextView>(R.id.txtLugar)
        val txtMotivo = findViewById<TextView>(R.id.txtMotivo)
        val txtResponsable = findViewById<TextView>(R.id.txtResponsable)

        txtLugar.text = lugar
        txtMotivo.text = motivo
        txtResponsable.text = responsable
    }


    override fun onMapReady(googleMap: GoogleMap) {

        try {
            mapa = googleMap
            mapa!!.mapType = GoogleMap.MAP_TYPE_HYBRID

            val visitaLatLng = LatLng(visitaLat!!, visitaLon!!)

            mapa!!.addMarker(
                MarkerOptions().position(visitaLatLng)
            )
            val zoomLevel = 15f
            mapa!!.moveCamera(CameraUpdateFactory.newLatLngZoom(visitaLatLng, zoomLevel))
            mapa!!.uiSettings.setAllGesturesEnabled(false)
        } catch (ex: Exception) {
            Log.e("Error al cargar el mapa", ex.toString())
        }
    }
}