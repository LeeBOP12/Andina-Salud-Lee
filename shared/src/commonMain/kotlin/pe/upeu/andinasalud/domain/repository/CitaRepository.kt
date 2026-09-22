package pe.upeu.andinasalud.domain.repository

import pe.upeu.andinasalud.domain.model.Cita
import pe.upeu.andinasalud.domain.model.Medico
import pe.upeu.andinasalud.domain.model.Paciente
import pe.upeu.andinasalud.domain.model.Sede
import pe.upeu.andinasalud.domain.model.SolicitudCita

interface CitaRepository {
    suspend fun obtenerPaciente(): Paciente
    suspend fun obtenerSedes(): List<Sede>
    suspend fun obtenerEspecialidades(): List<String>
    suspend fun obtenerMedicos(): List<Medico>
    suspend fun obtenerCitas(): List<Cita>
    suspend fun solicitarCita(solicitud: SolicitudCita): Cita
    suspend fun cancelarCita(citaId: Long, motivo: String): Cita
}
