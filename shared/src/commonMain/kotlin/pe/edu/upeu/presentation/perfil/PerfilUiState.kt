package pe.edu.upeu.presentation.perfil

data class PerfilUiState(
    val fase: Fase = Fase.Cargando
) {

    sealed interface Fase {
        data object Cargando : Fase
        data class Contenido(
            val nombre: String,
            val documento: String,
            val correo: String,
            val telefono: String
        ) : Fase
        data class Error(val mensaje: String) : Fase
    }
}
