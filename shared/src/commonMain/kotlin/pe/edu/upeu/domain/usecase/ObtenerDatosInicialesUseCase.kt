package pe.edu.upeu.domain.usecase

import pe.edu.upeu.domain.model.Medico
import pe.edu.upeu.domain.model.Paciente
import pe.edu.upeu.domain.model.Sede
import pe.edu.upeu.domain.repository.CitaRepository

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
