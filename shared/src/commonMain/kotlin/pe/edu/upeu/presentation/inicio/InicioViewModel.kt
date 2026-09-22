package pe.edu.upeu.presentation.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.domain.model.EstadoCita
import pe.edu.upeu.domain.usecase.ObtenerCitasUseCase
import pe.edu.upeu.domain.usecase.ObtenerDatosInicialesUseCase

class InicioViewModel(
    private val obtenerDatosIniciales: ObtenerDatosInicialesUseCase,
    private val obtenerCitas: ObtenerCitasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        cargarInicio()
    }

    fun cargarInicio() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = InicioUiState.Fase.Cargando) }

            val datos = obtenerDatosIniciales()
            val citas = obtenerCitas()

            datos.fold(
                onSuccess = { datosIniciales ->
                    citas.fold(
                        onSuccess = { lista ->
                            val proxima = lista
                                .firstOrNull { it.estado is EstadoCita.Programada }
                                ?.let {
                                    ProximaCitaUi(
                                        especialidad = it.especialidad,
                                        medico = it.medico,
                                        sede = it.sede,
                                        fechaHora = "${it.fecha} - ${it.hora}"
                                    )
                                }
                            _uiState.update {
                                it.copy(
                                    fase = InicioUiState.Fase.Contenido(
                                        nombrePaciente = datosIniciales.paciente.nombre,
                                        proximaCita = proxima
                                    )
                                )
                            }
                        },
                        onFailure = { mostrarError(it) }
                    )
                },
                onFailure = { mostrarError(it) }
            )
        }
    }

    private fun mostrarError(error: Throwable) {
        _uiState.update {
            it.copy(
                fase = InicioUiState.Fase.Error(
                    error.message?.takeIf(String::isNotBlank)
                        ?: "No se pudo cargar el inicio"
                )
            )
        }
    }
}
