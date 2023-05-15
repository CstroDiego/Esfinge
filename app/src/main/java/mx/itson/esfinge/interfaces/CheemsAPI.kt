package mx.itson.esfinge.interfaces

import mx.itson.esfinge.entidades.Visita
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interfaz para los parametos de las peticiones al servidor
 *
 * @constructor Crea un objeto de tipo CheemsAPI
 *
 * @author Diego Castro Arce
 */
interface CheemsAPI {

    /**
     * Obtiene todas la visitas registradas
     *
     * @return Lista de visitas
     */
    @GET("visitas")
    fun getVisitas(): Call<List<Visita>>

    /**
     * Crea un nuevo registro de visita en la base de datos
     *
     * @param visita Objeto de tipo vicita con los datos de la nueva vicita
     * @return Visita creada
     */
    @POST("visitas")
    fun createVisita(@Body visita: Visita): Call<Visita>

    /**
     * Obtiene un registro especifico de la base de datos en base al id de la visita
     *
     * @param visitaId Id de la vicita por consultar
     * @return Visita consultada
     */
    @GET("visitas/{id}")
    fun getVisita(@Path("id") visitaId: Int): Call<Visita>

}