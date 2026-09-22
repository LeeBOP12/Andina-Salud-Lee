package pe.edu.upeu.domain.usecase

import pe.edu.upeu.domain.model.Cita
import pe.edu.upeu.domain.repository.CitaRepository

class ObtenerCitasUseCase(
    private val repository: CitaRepository
) {

    suspend operator fun invoke(): Result<List<Cita>> = resultadoDe {
        repository.obtenerCitas()
            .sortedWith(compareBy<Cita> { it.fecha }.thenBy { it.hora })
    }
}
