package pe.edu.upeu.domain.repository

import pe.edu.upeu.domain.model.Cita
import pe.edu.upeu.domain.model.Medico
import pe.edu.upeu.domain.model.Paciente
import pe.edu.upeu.domain.model.Sede
import pe.edu.upeu.domain.model.SolicitudCita

interface CitaRepository {
    suspend fun obtenerPaciente(): Paciente
    suspend fun obtenerSedes(): List<Sede>
    suspend fun obtenerEspecialidades(): List<String>
    suspend fun obtenerMedicos(): List<Medico>
    suspend fun obtenerCitas(): List<Cita>
    suspend fun solicitarCita(solicitud: SolicitudCita): Cita
    suspend fun cancelarCita(citaId: Long, motivo: String): Cita
}
