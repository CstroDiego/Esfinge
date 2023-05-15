package mx.itson.esfinge.interfaces

import mx.itson.esfinge.entidades.Visita
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CheemsAPI {

    @GET("visitas")
    fun getVisitas(): Call<List<Visita>>

    @POST("visitas")
    fun createVisita(@Body visita: Visita): Call<Visita>

}