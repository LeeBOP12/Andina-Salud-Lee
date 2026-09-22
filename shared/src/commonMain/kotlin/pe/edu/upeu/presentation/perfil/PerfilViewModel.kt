package pe.edu.upeu.presentation.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pe.edu.upeu.domain.usecase.ObtenerDatosInicialesUseCase

class PerfilViewModel(
    private val obtenerDatosIniciales: ObtenerDatosInicialesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        cargarPerfil()
    }

    fun cargarPerfil() {
        viewModelScope.launch {
            _uiState.update { it.copy(fase = PerfilUiState.Fase.Cargando) }

            obtenerDatosIniciales()
                .onSuccess { datos ->
                    val paciente = datos.paciente
                    _uiState.update {
                        it.copy(
                            fase = PerfilUiState.Fase.Contenido(
                                nombre = paciente.nombre,
                                documento = paciente.documento,
                                correo = paciente.correo,
                                telefono = paciente.telefono
                            )
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            fase = PerfilUiState.Fase.Error(
                                error.message?.takeIf(String::isNotBlank)
                                    ?: "No se pudo cargar el perfil"
                            )
                        )
                    }
                }
        }
    }
}
