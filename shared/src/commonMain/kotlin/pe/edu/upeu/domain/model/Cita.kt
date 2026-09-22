package pe.edu.upeu.domain.model

data class Cita(
    val id: Long,
    val especialidad: String,
    val medico: String,
    val sede: String,
    val fecha: String,
    val hora: String,
    val motivo: String,
    val estado: EstadoCita
) {
    init {
        require(id > 0L) { "El id de la cita debe ser positivo" }
        require(especialidad.isNotBlank()) { "La especialidad es obligatoria" }
        require(medico.isNotBlank()) { "El medico es obligatorio" }
        require(sede.isNotBlank()) { "La sede es obligatoria" }
        require(fecha.isNotBlank()) { "La fecha es obligatoria" }
        require(hora.isNotBlank()) { "La hora es obligatoria" }
        require(motivo.isNotBlank()) { "El motivo es obligatorio" }
    }
}
