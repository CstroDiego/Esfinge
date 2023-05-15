package mx.itson.esfinge

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    var mapa: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapaFragment = supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment?
        mapaFragment!!.getMapAsync(this)

        obtenerMarcadores()

        val btnAgregarMarcador = findViewById<FloatingActionButton>(R.id.btnAgregarMarcador)
        btnAgregarMarcador.setOnClickListener {
            val intent = Intent(this, FormVisitaActivity::class.java)
            startActivity(intent)
        }
    }

    fun obtenerMarcadores() {
        val call: Call<List<Visita>>? = RetrofitUtil.getApi()?.getVisitas()
        call?.enqueue(object : Callback<List<Visita>> {
            override fun onResponse(call: Call<List<Visita>>, response: Response<List<Visita>>) {
                if (response.isSuccessful) {
                    val visitas: List<Visita>? = response.body()
                    visitas?.let {
                        mapa?.clear()
                        for (v in it) {
                            val latLng = LatLng(v.latitud ?: 0.0, v.longitud ?: 0.0)
                            val marker = mapa?.addMarker(
                                MarkerOptions().position(latLng)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cheems))
                            )
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

    fun obtenerVisita(visitaId: Int) {
        val call: Call<Visita> = RetrofitUtil.getApi()!!.getVisita(visitaId)
        call.enqueue(object : Callback<Visita> {
            override fun onResponse(call: Call<Visita>, response: Response<Visita>) {
                if (response.isSuccessful) {
                    val visita: Visita? = response.body()
                    if (visita != null) {
                        val intent = Intent(this@MainActivity, ListVisitaActivity::class.java)
                        intent.putExtra("visitaId", visita.id)
                        intent.putExtra("visitaLugar", visita.lugar)
                        intent.putExtra("visitaMotivo", visita.motivo)
                        intent.putExtra("visitaResponsable", visita.responsable)
                        intent.putExtra("visitaLatitud", visita.latitud)
                        intent.putExtra("visitaLongitud", visita.longitud)
                        startActivity(intent)
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
            mapa!!.setOnMarkerClickListener(this)
        } catch (ex: Exception) {
            Log.e("Error al cargar el mapa", ex.toString())
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val visitaId = marker.tag as? Int
        visitaId?.let {
            obtenerVisita(it)
        }
        return false
    }

    override fun onResume() {
        super.onResume()
        obtenerMarcadores()
    }
}
