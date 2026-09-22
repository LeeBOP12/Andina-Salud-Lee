package pe.edu.upeu.domain.model

data class SolicitudCita(
    val especialidad: String,
    val medico: String,
    val sede: String,
    val fecha: String,
    val hora: String,
    val motivo: String
)
