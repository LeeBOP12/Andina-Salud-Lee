package pe.edu.upeu.presentation.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.domain.model.Cita
import pe.edu.upeu.domain.model.EstadoCita
import pe.edu.upeu.domain.usecase.CancelarCitaUseCase
import pe.edu.upeu.domain.usecase.ObtenerCitasUseCase

class DetalleCitaViewModel(
    private val obtenerCitas: ObtenerCitasUseCase,
    private val cancelarCita: CancelarCitaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleCitaUiState())
    val uiState: StateFlow<DetalleCitaUiState> = _uiState.asStateFlow()

    private var citaId: Long? = null

    fun cargarCita(id: Long) {
        citaId = id
        viewModelScope.launch {
            _uiState.update { it.copy(fase = DetalleCitaUiState.Fase.Cargando, mensaje = null) }

            obtenerCitas()
                .onSuccess { citas ->
                    val cita = citas.firstOrNull { it.id == id }
                    _uiState.update {
                        it.copy(
                            fase = if (cita == null) {
                                DetalleCitaUiState.Fase.Error("No se encontro la cita seleccionada")
                            } else {
                                DetalleCitaUiState.Fase.Contenido(cita.aDetalleUi())
                            }
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            fase = DetalleCitaUiState.Fase.Error(
                                error.message?.takeIf(String::isNotBlank)
                                    ?: "No se pudo cargar el detalle"
                            )
                        )
                    }
                }
        }
    }

    fun mostrarDialogoCancelacion() {
        _uiState.update { it.copy(mostrarDialogoCancelacion = true, mensaje = null) }
    }

    fun ocultarDialogoCancelacion() {
        _uiState.update {
            it.copy(
                mostrarDialogoCancelacion = false,
                motivoCancelacion = ""
            )
        }
    }

    fun onMotivoCancelacionChange(valor: String) {
        _uiState.update { it.copy(motivoCancelacion = valor) }
    }

    fun confirmarCancelacion() {
        val id = citaId ?: return
        val motivo = _uiState.value.motivoCancelacion

        viewModelScope.launch {
            cancelarCita(id, motivo)
                .onSuccess { cita ->
                    _uiState.update {
                        it.copy(
                            fase = DetalleCitaUiState.Fase.Contenido(cita.aDetalleUi()),
                            mostrarDialogoCancelacion = false,
                            motivoCancelacion = "",
                            mensaje = "Cita cancelada correctamente"
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            mostrarDialogoCancelacion = false,
                            motivoCancelacion = "",
                            mensaje = error.message ?: "No se pudo cancelar la cita"
                        )
                    }
                }
        }
    }

    private fun Cita.aDetalleUi(): DetalleCitaUi {
        return DetalleCitaUi(
            id = id,
            especialidad = especialidad,
            medico = medico,
            sede = sede,
            fecha = fecha,
            hora = hora,
            motivo = motivo,
            estado = estado.etiqueta(),
            detalleEstado = estado.detalle(),
            puedeCancelar = estado is EstadoCita.Programada
        )
    }

    private fun EstadoCita.etiqueta(): String {
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
}
