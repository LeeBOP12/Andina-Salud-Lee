package pe.edu.upeu.domain.model

data class Medico(
    val id: String,
    val nombre: String,
    val especialidad: String,
    val sedes: List<Sede>
) {
    init {
        require(id.isNotBlank()) { "El id del medico es obligatorio" }
        require(nombre.isNotBlank()) { "El nombre del medico es obligatorio" }
        require(especialidad.isNotBlank()) { "La especialidad es obligatoria" }
        require(sedes.isNotEmpty()) { "El medico debe atender al menos en una sede" }
    }
}
