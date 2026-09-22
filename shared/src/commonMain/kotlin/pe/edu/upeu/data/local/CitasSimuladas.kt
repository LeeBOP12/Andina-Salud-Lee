package pe.edu.upeu.data.local

import pe.edu.upeu.domain.model.Cita
import pe.edu.upeu.domain.model.EstadoCita
import pe.edu.upeu.domain.model.Medico
import pe.edu.upeu.domain.model.Paciente
import pe.edu.upeu.domain.model.Sede

object CitasSimuladas {
    val paciente = Paciente(
        id = "P-0417",
        nombre = "Lucia Quispe Mamani",
        documento = "70154823",
        correo = "lucia.quispe@correo.pe",
        telefono = "987654321"
    )

    val sedes = listOf(
        Sede(id = "S-01", nombre = "Nana"),
        Sede(id = "S-02", nombre = "Chosica"),
        Sede(id = "S-03", nombre = "Chaclacayo"),
        Sede(id = "S-04", nombre = "Santa Anita")
    )

    val especialidades = listOf(
        "Medicina General",
        "Odontologia",
        "Pediatria",
        "Nutricion",
        "Psicologia"
    )

    val medicos = listOf(
        Medico("M-01", "Dr. Ivan Rojas", "Medicina General", listOf(sedes[0], sedes[1])),
        Medico("M-02", "Dra. Elena Paredes", "Medicina General", listOf(sedes[2], sedes[3])),
        Medico("M-03", "Dra. Rosa Flores", "Odontologia", listOf(sedes[1], sedes[3])),
        Medico("M-04", "Dr. Marco Salas", "Odontologia", listOf(sedes[0], sedes[2])),
        Medico("M-05", "Dra. Carla Nunez", "Pediatria", listOf(sedes[2], sedes[3])),
        Medico("M-06", "Dr. Raul Cardenas", "Pediatria", listOf(sedes[0], sedes[1])),
        Medico("M-07", "Lic. Ana Bermudez", "Nutricion", listOf(sedes[1], sedes[3])),
        Medico("M-08", "Lic. Pedro Huaman", "Nutricion", listOf(sedes[0], sedes[2])),
        Medico("M-09", "Ps. Luis Tapia", "Psicologia", listOf(sedes[0], sedes[1])),
        Medico("M-10", "Ps. Mariana Vidal", "Psicologia", listOf(sedes[2], sedes[3]))
    )

    val citas = listOf(
        Cita(
            id = 1L,
            especialidad = "Medicina General",
            medico = "Dr. Ivan Rojas",
            sede = "Nana",
            fecha = "2026-10-05",
            hora = "09:00",
            motivo = "Control general por dolor de cabeza frecuente",
            estado = EstadoCita.Programada(recordatorioActivo = true)
        ),
        Cita(
            id = 2L,
            especialidad = "Odontologia",
            medico = "Dra. Rosa Flores",
            sede = "Chosica",
            fecha = "2026-10-08",
            hora = "16:30",
            motivo = "Revision dental preventiva y limpieza",
            estado = EstadoCita.Programada(recordatorioActivo = false)
        ),
        Cita(
            id = 3L,
            especialidad = "Nutricion",
            medico = "Lic. Ana Bermudez",
            sede = "Santa Anita",
            fecha = "2026-10-15",
            hora = "11:15",
            motivo = "Evaluacion nutricional para plan alimenticio",
            estado = EstadoCita.Programada(recordatorioActivo = true)
        ),
        Cita(
            id = 4L,
            especialidad = "Pediatria",
            medico = "Dra. Carla Nunez",
            sede = "Chaclacayo",
            fecha = "2026-08-30",
            hora = "08:45",
            motivo = "Control pediatrico regular",
            estado = EstadoCita.Atendida(indicaciones = "Control en tres meses")
        ),
        Cita(
            id = 5L,
            especialidad = "Psicologia",
            medico = "Ps. Luis Tapia",
            sede = "Nana",
            fecha = "2026-09-02",
            hora = "15:00",
            motivo = "Seguimiento de sesiones de orientacion",
            estado = EstadoCita.Atendida(indicaciones = "Continuar sesiones quincenales")
        ),
        Cita(
            id = 6L,
            especialidad = "Medicina General",
            medico = "Dr. Ivan Rojas",
            sede = "Chosica",
            fecha = "2026-09-05",
            hora = "10:30",
            motivo = "Consulta por malestar general",
            estado = EstadoCita.Cancelada(
                motivo = "Viaje del paciente",
                canceladaPorPaciente = true
            )
        )
    )
}
