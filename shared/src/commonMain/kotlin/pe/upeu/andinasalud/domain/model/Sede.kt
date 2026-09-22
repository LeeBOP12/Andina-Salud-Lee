package pe.upeu.andinasalud.domain.model

data class Sede(
    val id: String,
    val nombre: String
) {
    init {
        require(id.isNotBlank()) { "El id de la sede es obligatorio" }
        require(nombre.isNotBlank()) { "El nombre de la sede es obligatorio" }
    }
}
