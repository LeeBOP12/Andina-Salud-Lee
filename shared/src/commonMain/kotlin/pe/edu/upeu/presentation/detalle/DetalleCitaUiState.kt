package pe.edu.upeu.presentation.detalle

data class DetalleCitaUiState(
    val fase: Fase = Fase.Cargando,
    val mostrarDialogoCancelacion: Boolean = false,
    val motivoCancelacion: String = "",
    val mensaje: String? = null
) {

    sealed interface Fase {
        data object Cargando : Fase
        data class Contenido(val cita: DetalleCitaUi) : Fase
        data class Error(val mensaje: String) : Fase
    }
}

data class DetalleCitaUi(
    val id: Long,
    val especialidad: String,
    val medico: String,
    val sede: String,
    val fecha: String,
    val hora: String,
    val motivo: String,
    val estado: String,
    val detalleEstado: String,
    val puedeCancelar: Boolean
)
