package pe.edu.upeu.presentation.citas

import pe.edu.upeu.domain.model.Cita
import pe.edu.upeu.domain.model.EstadoCita

data class CitaUi(
    val id: Long,
    val especialidad: String,
    val medico: String,
    val sede: String,
    val fechaHora: String,
    val estado: String,
    val detalleEstado: String,
    val motivo: String
)

fun Cita.aUi(): CitaUi {
    return CitaUi(
        id = id,
        especialidad = especialidad,
        medico = medico,
        sede = sede,
        fechaHora = "$fecha - $hora",
        estado = estado.etiqueta(),
        detalleEstado = estado.detalle(),
        motivo = motivo
    )
}

fun EstadoCita.etiqueta(): String {
    return when (this) {
        is EstadoCita.Programada -> "Programada"
        is EstadoCita.Atendida -> "Atendida"
        is EstadoCita.Cancelada -> "Cancelada"
    }
}

private fun EstadoCita.detalle(): String {
    return when (this) {
        is EstadoCita.Programada -> if (recordatorioActivo) {
            "Recordatorio activo"
        } else {
            "Sin recordatorio"
        }
        is EstadoCita.Atendida -> indicaciones
        is EstadoCita.Cancelada -> motivo
    }
}
