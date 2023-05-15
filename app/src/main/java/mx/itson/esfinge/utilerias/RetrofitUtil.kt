package mx.itson.esfinge.utilerias

import com.google.gson.GsonBuilder
import mx.itson.esfinge.interfaces.CheemsAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Esta clase proporciona una utilidad para configurar y obtener una instancia de la interfaz CheemsAPI utilizando Retrofit.
 * Permite realizar peticiones HTTP a la API CheemsAPI para obtener y enviar datos.
 *
 * @author Diego Castro Arce
 */
object RetrofitUtil {

    /**
     * Obtiene una instancia de la interfaz CheemsAPI configurada con Retrofit.
     *
     * @return la instancia de la interfaz CheemsAPI
     */
    fun getApi(): CheemsAPI? {
        // Crear un objeto Gson para la conversión de JSON
        val gson = GsonBuilder().create()

        // Crear una instancia de Retrofit y configurarla con la URL base y el convertidor Gson
        val retrofit = Retrofit.Builder().baseUrl("http://cheemsapi.ddns.net/api/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

        // Devolver una instancia de la interfaz CheemsAPI creada por Retrofit
        return retrofit.create(CheemsAPI::class.java)
    }
}