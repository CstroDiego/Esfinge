package mx.itson.esfinge.entidades

import com.google.gson.annotations.SerializedName

data class Visita(

    @SerializedName("lugar")
    val lugar: String? = null,
    @SerializedName("motivo")
    val motivo: String? = null,
    @SerializedName("responsable")
    val responsable: String? = null,
    @SerializedName("latitud")
    val latitud: Double? = null,
    @SerializedName("longitud")
    val longitud: Double? = null
) {
    @SerializedName("id")
    var id: Int? = null
}
