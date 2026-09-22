package pe.edu.upeu.presentation.solicitud

data class SolicitudUiState(
    val fase: Fase = Fase.Cargando,
    val formulario: FormularioSolicitud = FormularioSolicitud(),
    val registrando: Boolean = false,
    val mensajeExito: String? = null
) {

    sealed interface Fase {
        data object Cargando : Fase
        data class Listo(
            val especialidades: List<String>,
            val sedes: List<String>,
            val medicos: List<String>
        ) : Fase
        data class Error(val mensaje: String) : Fase
    }
}

data class FormularioSolicitud(
    val especialidad: String = "",
    val medico: String = "",
    val sede: String = "",
    val fecha: String = "",
    val hora: String = "",
    val motivo: String = "",
    val especialidadError: String? = null,
    val medicoError: String? = null,
    val sedeError: String? = null,
    val fechaError: String? = null,
    val horaError: String? = null,
    val motivoError: String? = null
) {

    fun sinErrores(): FormularioSolicitud {
        return copy(
            especialidadError = null,
            medicoError = null,
            sedeError = null,
            fechaError = null,
            horaError = null,
            motivoError = null
        )
    }
}
