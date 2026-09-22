package pe.edu.upeu.presentation.citas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.domain.model.Cita
import pe.edu.upeu.domain.model.EstadoCita
import pe.edu.upeu.domain.usecase.ObtenerCitasUseCase

class CitasViewModel(
    private val obtenerCitas: ObtenerCitasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CitasUiState())
    val uiState: StateFlow<CitasUiState> = _uiState.asStateFlow()

    private var citas: List<Cita> = emptyList()

    init {
        cargarCitas()
    }

    fun cargarCitas() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = CitasUiState.Fase.Cargando) }

            obtenerCitas()
                .onSuccess { resultado ->
                    citas = resultado
                    aplicarFiltros()
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            fase = CitasUiState.Fase.Error(
                                error.message?.takeIf(String::isNotBlank)
                                    ?: "No se pudieron cargar las citas"
                            )
                        )
                    }
                }
        }
    }

    fun onBusquedaChange(valor: String) {
        _uiState.update { it.copy(busqueda = valor) }
        aplicarFiltros()
    }

    fun onFiltroChange(filtro: FiltroEstado) {
        _uiState.update { it.copy(filtro = filtro) }
        aplicarFiltros()
    }

    private fun aplicarFiltros() {
        val estado = _uiState.value
        val consulta = estado.busqueda.normalizada()

        val filtradas = citas
            .filter { cita -> cita.coincideConFiltro(estado.filtro) }
            .filter { cita ->
                consulta.isBlank() ||
                    cita.especialidad.normalizada().contains(consulta) ||
                    cita.medico.normalizada().contains(consulta)
            }
            .map { it.aUi() }

        _uiState.update {
            it.copy(
                fase = if (filtradas.isEmpty()) {
                    CitasUiState.Fase.Vacia
                } else {
                    CitasUiState.Fase.Contenido(filtradas)
                }
            )
        }
    }

    private fun Cita.coincideConFiltro(filtro: FiltroEstado): Boolean {
        return when (filtro) {
            FiltroEstado.Todos -> true
            FiltroEstado.Programada -> estado is EstadoCita.Programada
            FiltroEstado.Atendida -> estado is EstadoCita.Atendida
            FiltroEstado.Cancelada -> estado is EstadoCita.Cancelada
        }
    }

    private fun String.normalizada(): String {
        return lowercase()
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
            .replace("ñ", "n")
    }
}
