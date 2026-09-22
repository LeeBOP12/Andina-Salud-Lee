package pe.edu.upeu.presentation.citas

data class CitasUiState(
    val fase: Fase = Fase.Cargando,
    val busqueda: String = "",
    val filtro: FiltroEstado = FiltroEstado.Todos
) {

    sealed interface Fase {
        data object Cargando : Fase
        data object Vacia : Fase
        data class Contenido(val citas: List<CitaUi>) : Fase
        data class Error(val mensaje: String) : Fase
    }
}

enum class FiltroEstado(val etiqueta: String) {
    Todos("Todas"),
    Programada("Programadas"),
    Atendida("Atendidas"),
    Cancelada("Canceladas")
}
