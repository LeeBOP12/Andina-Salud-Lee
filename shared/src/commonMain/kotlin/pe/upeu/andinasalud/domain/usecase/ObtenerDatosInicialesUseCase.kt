package pe.upeu.andinasalud.domain.usecase

import pe.upeu.andinasalud.domain.model.Medico
import pe.upeu.andinasalud.domain.model.Paciente
import pe.upeu.andinasalud.domain.model.Sede
import pe.upeu.andinasalud.domain.repository.CitaRepository

data class DatosIniciales(
    val paciente: Paciente,
    val sedes: List<Sede>,
    val especialidades: List<String>,
    val medicos: List<Medico>
)

class ObtenerDatosInicialesUseCase(
    private val repository: CitaRepository
) {

    suspend operator fun invoke(): Result<DatosIniciales> = resultadoDe {
        DatosIniciales(
            paciente = repository.obtenerPaciente(),
            sedes = repository.obtenerSedes(),
            especialidades = repository.obtenerEspecialidades(),
            medicos = repository.obtenerMedicos()
        )
    }
}
