package mx.itson.esfinge.utilerias

import com.google.gson.GsonBuilder
import mx.itson.esfinge.interfaces.CheemsAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitUtil {

    fun getApi(): CheemsAPI? {
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .baseUrl("http://cheemsapi.ddns.net/api/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        return retrofit.create(CheemsAPI::class.java)

    }

}