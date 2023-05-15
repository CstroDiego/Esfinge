package mx.itson.esfinge.interfaces

import mx.itson.esfinge.entidades.Visita
import retrofit2.Call
import retrofit2.http.GET

interface CheemsAPI {

    @GET("visitas")
    fun getVisitas(): Call<List<Visita>>

}