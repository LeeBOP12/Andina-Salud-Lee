package pe.edu.upeu.presentation.navigation

sealed class Destino(
    val ruta: String,
    val titulo: String,
    val visibleEnBarra: Boolean = true
) {
    data object Inicio : Destino("inicio", "Inicio")
    data object Citas : Destino("citas", "Citas")
    data object Perfil : Destino("perfil", "Perfil")
    data object Solicitud : Destino("solicitud", "Solicitar cita", visibleEnBarra = false)
    data class Detalle(val citaId: Long) : Destino("detalle/$citaId", "Detalle", visibleEnBarra = false)

    companion object {
        val barraInferior = listOf(Inicio, Citas, Perfil)
    }
}
