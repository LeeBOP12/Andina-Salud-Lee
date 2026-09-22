package pe.edu.upeu.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pe.edu.upeu.data.local.CitasSimuladas
import pe.edu.upeu.domain.model.Cita
import pe.edu.upeu.domain.model.EstadoCita
import pe.edu.upeu.domain.model.Medico
import pe.edu.upeu.domain.model.Paciente
import pe.edu.upeu.domain.model.Sede
import pe.edu.upeu.domain.model.SolicitudCita
import pe.edu.upeu.domain.repository.CitaRepository

class CitaRepositoryFake : CitaRepository {
    private val mutex = Mutex()
    private var citas = CitasSimuladas.citas
    private var siguienteId = (citas.maxOfOrNull { it.id } ?: 0L) + 1L

    override suspend fun obtenerPaciente(): Paciente {
        simularRetardo()
        return CitasSimuladas.paciente
    }

    override suspend fun obtenerSedes(): List<Sede> {
        simularRetardo()
        return CitasSimuladas.sedes
    }

    override suspend fun obtenerEspecialidades(): List<String> {
        simularRetardo()
        return CitasSimuladas.especialidades
    }

    override suspend fun obtenerMedicos(): List<Medico> {
        simularRetardo()
        return CitasSimuladas.medicos
    }

    override suspend fun obtenerCitas(): List<Cita> {
        simularRetardo()
        return mutex.withLock { citas.sortedWith(compareBy<Cita> { it.fecha }.thenBy { it.hora }) }
    }

    override suspend fun solicitarCita(solicitud: SolicitudCita): Cita {
        simularRetardo()
        return mutex.withLock {
            val cita = Cita(
                id = siguienteId++,
                especialidad = solicitud.especialidad,
                medico = solicitud.medico,
                sede = solicitud.sede,
                fecha = solicitud.fecha,
                hora = solicitud.hora,
                motivo = solicitud.motivo,
                estado = EstadoCita.Programada(recordatorioActivo = true)
            )
            citas = citas + cita
            cita
        }
    }

    override suspend fun cancelarCita(citaId: Long, motivo: String): Cita {
        simularRetardo()
        return mutex.withLock {
            val cita = citas.firstOrNull { it.id == citaId }
                ?: error("No se encontro la cita seleccionada")
            val cancelada = cita.copy(
                estado = EstadoCita.Cancelada(
                    motivo = motivo,
                    canceladaPorPaciente = true
                )
            )
            citas = citas.map { if (it.id == citaId) cancelada else it }
            cancelada
        }
    }

    private suspend fun simularRetardo() {
        delay(RETARDO_MS)
    }

    private companion object {
        const val RETARDO_MS = 800L
    }
}
