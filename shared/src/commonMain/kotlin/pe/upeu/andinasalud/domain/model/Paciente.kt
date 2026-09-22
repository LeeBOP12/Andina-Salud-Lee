package pe.upeu.andinasalud.domain.model

data class Paciente(
    val id: String,
    val nombre: String,
    val documento: String,
    val correo: String,
    val telefono: String
) {
    init {
        require(id.isNotBlank()) { "El id del paciente es obligatorio" }
        require(nombre.isNotBlank()) { "El nombre del paciente es obligatorio" }
        require(documento.isNotBlank()) { "El documento del paciente es obligatorio" }
        require(correo.isNotBlank()) { "El correo del paciente es obligatorio" }
        require(telefono.isNotBlank()) { "El telefono del paciente es obligatorio" }
    }
}
