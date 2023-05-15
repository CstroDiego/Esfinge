package mx.itson.esfinge.entidades

import com.google.gson.annotations.SerializedName

/**
 * Clase de datos para la entidad Visita
 *
 * @property lugar Lugar de la visita
 * @property motivo Motivo de la visita
 * @property responsable Responsable de la visita
 * @property latitud Longitud de la ubicacion de la visita
 * @property longitud Latitud de la ubicacion de la visita
 * @constructor Crea un objeto de tipo vicita vacio
 *
 * @author Diego Castro Arce
 */
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
    /**
     * Id de la visita en la base de datos
     */
    @SerializedName("id")
    var id: Int? = null
}
