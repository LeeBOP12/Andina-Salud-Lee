package pe.upeu.andinasalud.domain.usecase

import pe.upeu.andinasalud.domain.model.Cita
import pe.upeu.andinasalud.domain.model.EstadoCita
import pe.upeu.andinasalud.domain.repository.CitaRepository

class CitaNoCancelableException(
    message: String
) : IllegalArgumentException(message)

class CancelarCitaUseCase(
    private val repository: CitaRepository,
    private val fechaActual: () -> String = { "2026-09-22" },
    private val horaActual: () -> String = { "00:00" }
) {

    suspend operator fun invoke(
        citaId: Long,
        motivo: String
    ): Result<Cita> = resultadoDe {
        val cita = repository.obtenerCitas().firstOrNull { it.id == citaId }
            ?: throw CitaNoCancelableException("No se encontro la cita seleccionada")

        if (cita.estado !is EstadoCita.Programada) {
            throw CitaNoCancelableException("Solo se puede cancelar una cita programada")
        }
        if (!faltanMasDeVeinticuatroHoras(cita)) {
            throw CitaNoCancelableException("La cita solo puede cancelarse con mas de 24 horas de anticipacion")
        }

        repository.cancelarCita(
            citaId = citaId,
            motivo = motivo.trim().ifBlank { "Cancelada por el paciente" }
        )
    }

    private fun faltanMasDeVeinticuatroHoras(cita: Cita): Boolean {
        val momentoActual = minutosDesdeReferencia(fechaActual(), horaActual())
        val momentoCita = minutosDesdeReferencia(cita.fecha, cita.hora)
        return momentoCita - momentoActual > MINUTOS_EN_24_HORAS
    }

    private fun minutosDesdeReferencia(fecha: String, hora: String): Long {
        return diasDesdeReferencia(fecha) * MINUTOS_EN_DIA + minutosDelDia(hora)
    }

    private fun diasDesdeReferencia(fecha: String): Long {
        val partes = fecha.split("-")
        if (partes.size != 3) return 0L

        val anio = partes[0].toIntOrNull() ?: return 0L
        val mes = partes[1].toIntOrNull() ?: return 0L
        val dia = partes[2].toIntOrNull() ?: return 0L

        val diasAnios = (0 until anio).sumOf { if (esBisiesto(it)) 366L else 365L }
        val diasMeses = (1 until mes).sumOf { diasDelMes(anio, it).toLong() }
        return diasAnios + diasMeses + dia
    }

    private fun minutosDelDia(hora: String): Long {
        val partes = hora.split(":")
        if (partes.size != 2) return 0L
        val horas = partes[0].toLongOrNull() ?: return 0L
        val minutos = partes[1].toLongOrNull() ?: return 0L
        return horas * 60L + minutos
    }

    private fun diasDelMes(anio: Int, mes: Int): Int {
        return when (mes) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (esBisiesto(anio)) 29 else 28
            else -> 0
        }
    }

    private fun esBisiesto(anio: Int): Boolean {
        return anio % 400 == 0 || (anio % 4 == 0 && anio % 100 != 0)
    }

    private companion object {
        const val MINUTOS_EN_DIA = 24L * 60L
        const val MINUTOS_EN_24_HORAS = 24L * 60L
    }
}
