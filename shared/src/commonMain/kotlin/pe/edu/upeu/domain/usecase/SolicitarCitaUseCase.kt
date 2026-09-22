package pe.edu.upeu.domain.usecase

import pe.edu.upeu.domain.model.Cita
import pe.edu.upeu.domain.model.EstadoCita
import pe.edu.upeu.domain.model.SolicitudCita
import pe.edu.upeu.domain.repository.CitaRepository

data class ErroresDeSolicitud(
    val especialidad: String? = null,
    val medico: String? = null,
    val sede: String? = null,
    val fecha: String? = null,
    val hora: String? = null,
    val motivo: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(especialidad, medico, sede, fecha, hora, motivo).any { it != null }
}

class SolicitudInvalidaException(
    val errores: ErroresDeSolicitud
) : IllegalArgumentException("La solicitud de cita contiene datos invalidos")

class SolicitarCitaUseCase(
    private val repository: CitaRepository,
    private val fechaActual: () -> String = { "2026-09-22" },
    private val horaActual: () -> String = { "00:00" }
) {

    suspend operator fun invoke(
        especialidad: String,
        medico: String,
        sede: String,
        fecha: String,
        hora: String,
        motivo: String
    ): Result<Cita> = resultadoDe {
        val solicitud = SolicitudCita(
            especialidad = especialidad.trim(),
            medico = medico.trim(),
            sede = sede.trim(),
            fecha = fecha.trim(),
            hora = hora.trim(),
            motivo = motivo.trim()
        )
        val citas = repository.obtenerCitas()
        val errores = validarSolicitud(solicitud, citas)

        if (errores.hayErrores) {
            throw SolicitudInvalidaException(errores)
        }

        repository.solicitarCita(solicitud)
    }

    private fun validarSolicitud(
        solicitud: SolicitudCita,
        citas: List<Cita>
    ): ErroresDeSolicitud {
        val programadas = citas.filter { it.estado is EstadoCita.Programada }

        return ErroresDeSolicitud(
            especialidad = if (solicitud.especialidad.isBlank()) "La especialidad es obligatoria" else null,
            medico = if (solicitud.medico.isBlank()) "El medico es obligatorio" else null,
            sede = if (solicitud.sede.isBlank()) "La sede es obligatoria" else null,
            fecha = validarFecha(solicitud),
            hora = validarHora(solicitud, programadas),
            motivo = validarMotivo(solicitud.motivo)
        )
    }

    private fun validarFecha(solicitud: SolicitudCita): String? {
        if (solicitud.fecha.isBlank()) return "La fecha es obligatoria"
        if (solicitud.fecha < fechaActual()) return "La cita no puede ser anterior al momento actual"
        return null
    }

    private fun validarHora(
        solicitud: SolicitudCita,
        programadas: List<Cita>
    ): String? {
        if (solicitud.hora.isBlank()) return "La hora es obligatoria"
        if (solicitud.fecha == fechaActual() && solicitud.hora < horaActual()) {
            return "La cita no puede ser anterior al momento actual"
        }
        if (programadas.size >= MAX_CITAS_PROGRAMADAS) {
            return "El paciente ya tiene tres citas programadas"
        }
        val existeMismaFechaHora = programadas.any {
            it.fecha == solicitud.fecha && it.hora == solicitud.hora
        }
        if (existeMismaFechaHora) {
            return "Ya existe una cita programada en la misma fecha y hora"
        }
        return null
    }

    private fun validarMotivo(motivo: String): String? {
        if (motivo.isBlank()) return "El motivo es obligatorio"
        if (motivo.length < MIN_MOTIVO) return "El motivo debe tener al menos 10 caracteres"
        if (motivo.length > MAX_MOTIVO) return "El motivo no debe superar 200 caracteres"
        return null
    }

    private companion object {
        const val MAX_CITAS_PROGRAMADAS = 3
        const val MIN_MOTIVO = 10
        const val MAX_MOTIVO = 200
    }
}
