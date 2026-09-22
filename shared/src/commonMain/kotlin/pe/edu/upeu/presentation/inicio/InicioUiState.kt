package pe.edu.upeu.presentation.inicio

data class InicioUiState(
    val fase: Fase = Fase.Cargando
) {

    sealed interface Fase {
        data object Cargando : Fase
        data class Contenido(
            val nombrePaciente: String,
            val proximaCita: ProximaCitaUi?
        ) : Fase
        data class Error(val mensaje: String) : Fase
    }
}

data class ProximaCitaUi(
    val especialidad: String,
    val medico: String,
    val sede: String,
    val fechaHora: String
)
