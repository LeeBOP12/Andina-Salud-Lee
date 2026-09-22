package pe.edu.upeu.presentation.solicitud

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.domain.usecase.ErroresDeSolicitud
import pe.edu.upeu.domain.usecase.ObtenerDatosInicialesUseCase
import pe.edu.upeu.domain.usecase.SolicitarCitaUseCase
import pe.edu.upeu.domain.usecase.SolicitudInvalidaException

class SolicitudViewModel(
    private val obtenerDatosIniciales: ObtenerDatosInicialesUseCase,
    private val solicitarCita: SolicitarCitaUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SolicitudUiState())
    val uiState: StateFlow<SolicitudUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = SolicitudUiState.Fase.Cargando) }

            obtenerDatosIniciales()
                .onSuccess { datos ->
                    _uiState.update {
                        it.copy(
                            fase = SolicitudUiState.Fase.Listo(
                                especialidades = datos.especialidades,
                                sedes = datos.sedes.map { sede -> sede.nombre },
                                medicos = datos.medicos.map { medico -> medico.nombre }
                            )
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            fase = SolicitudUiState.Fase.Error(
                                error.message?.takeIf(String::isNotBlank)
                                    ?: "No se pudieron cargar los datos del formulario"
                            )
                        )
                    }
                }
        }
    }

    fun onEspecialidadChange(valor: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(
                    especialidad = valor,
                    especialidadError = null
                ),
                mensajeExito = null
            )
        }
    }

    fun onMedicoChange(valor: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(medico = valor, medicoError = null),
                mensajeExito = null
            )
        }
    }

    fun onSedeChange(valor: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(sede = valor, sedeError = null),
                mensajeExito = null
            )
        }
    }

    fun onFechaChange(valor: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(fecha = valor, fechaError = null),
                mensajeExito = null
            )
        }
    }

    fun onHoraChange(valor: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(hora = valor, horaError = null),
                mensajeExito = null
            )
        }
    }

    fun onMotivoChange(valor: String) {
        _uiState.update {
            it.copy(
                formulario = it.formulario.copy(motivo = valor, motivoError = null),
                mensajeExito = null
            )
        }
    }

    fun registrar() {
        if (_uiState.value.registrando) return

        viewModelScope.launch {
            val formulario = _uiState.value.formulario
            _uiState.update {
                it.copy(
                    registrando = true,
                    formulario = formulario.sinErrores(),
                    mensajeExito = null
                )
            }

            solicitarCita(
                especialidad = formulario.especialidad,
                medico = formulario.medico,
                sede = formulario.sede,
                fecha = formulario.fecha,
                hora = formulario.hora,
                motivo = formulario.motivo
            ).onSuccess { cita ->
                _uiState.update {
                    it.copy(
                        formulario = FormularioSolicitud(),
                        registrando = false,
                        mensajeExito = "Cita registrada para ${cita.fecha} a las ${cita.hora}"
                    )
                }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        registrando = false,
                        formulario = if (error is SolicitudInvalidaException) {
                            formulario.conErrores(error.errores)
                        } else {
                            formulario
                        },
                        fase = if (error is SolicitudInvalidaException) {
                            it.fase
                        } else {
                            SolicitudUiState.Fase.Error(
                                error.message?.takeIf(String::isNotBlank)
                                    ?: "No se pudo registrar la cita"
                            )
                        }
                    )
                }
            }
        }
    }

    private fun FormularioSolicitud.conErrores(
        errores: ErroresDeSolicitud
    ): FormularioSolicitud {
        return copy(
            especialidadError = errores.especialidad,
            medicoError = errores.medico,
            sedeError = errores.sede,
            fechaError = errores.fecha,
            horaError = errores.hora,
            motivoError = errores.motivo
        )
    }
}
