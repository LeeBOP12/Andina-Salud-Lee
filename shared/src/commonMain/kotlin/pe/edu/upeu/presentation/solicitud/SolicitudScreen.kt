package pe.edu.upeu.presentation.solicitud

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.presentation.component.EstadoCarga
import pe.edu.upeu.presentation.component.EstadoVacio
import pe.edu.upeu.presentation.component.ValidatedTextField

@Composable
fun SolicitudScreen(
    viewModel: SolicitudViewModel = koinViewModel(),
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsState()

    SolicitudScreen(
        estado = estado,
        onEspecialidadChange = viewModel::onEspecialidadChange,
        onMedicoChange = viewModel::onMedicoChange,
        onSedeChange = viewModel::onSedeChange,
        onFechaChange = viewModel::onFechaChange,
        onHoraChange = viewModel::onHoraChange,
        onMotivoChange = viewModel::onMotivoChange,
        onRegistrar = viewModel::registrar,
        onReintentar = viewModel::cargarDatos,
        onVolver = onVolver,
        modifier = modifier
    )
}

@Composable
fun SolicitudScreen(
    estado: SolicitudUiState,
    onEspecialidadChange: (String) -> Unit,
    onMedicoChange: (String) -> Unit,
    onSedeChange: (String) -> Unit,
    onFechaChange: (String) -> Unit,
    onHoraChange: (String) -> Unit,
    onMotivoChange: (String) -> Unit,
    onRegistrar: () -> Unit,
    onReintentar: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EncabezadoSolicitud()
        }

        when (val fase = estado.fase) {
            SolicitudUiState.Fase.Cargando -> item {
                EstadoCarga(mensaje = "Cargando formulario")
            }

            is SolicitudUiState.Fase.Error -> item {
                EstadoVacio(
                    titulo = "Formulario no disponible",
                    descripcion = fase.mensaje,
                    textoAccion = "Reintentar",
                    onAccion = onReintentar
                )
            }

            is SolicitudUiState.Fase.Listo -> item {
                FormularioSolicitudCard(
                    estado = estado,
                    opciones = fase,
                    onEspecialidadChange = onEspecialidadChange,
                    onMedicoChange = onMedicoChange,
                    onSedeChange = onSedeChange,
                    onFechaChange = onFechaChange,
                    onHoraChange = onHoraChange,
                    onMotivoChange = onMotivoChange,
                    onRegistrar = onRegistrar,
                    onVolver = onVolver
                )
            }
        }
    }
}

@Composable
private fun EncabezadoSolicitud() {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Solicitar cita",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Completa los datos requeridos para programar una nueva atencion.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FormularioSolicitudCard(
    estado: SolicitudUiState,
    opciones: SolicitudUiState.Fase.Listo,
    onEspecialidadChange: (String) -> Unit,
    onMedicoChange: (String) -> Unit,
    onSedeChange: (String) -> Unit,
    onFechaChange: (String) -> Unit,
    onHoraChange: (String) -> Unit,
    onMotivoChange: (String) -> Unit,
    onRegistrar: () -> Unit,
    onVolver: () -> Unit
) {
    val formulario = estado.formulario

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OpcionesDisponibles(
                titulo = "Especialidades",
                opciones = opciones.especialidades,
                onSeleccion = onEspecialidadChange
            )
            ValidatedTextField(
                valor = formulario.especialidad,
                etiqueta = "Especialidad",
                error = formulario.especialidadError,
                onValorChange = onEspecialidadChange
            )
            OpcionesDisponibles(
                titulo = "Medicos",
                opciones = opciones.medicos,
                onSeleccion = onMedicoChange
            )
            ValidatedTextField(
                valor = formulario.medico,
                etiqueta = "Medico",
                error = formulario.medicoError,
                onValorChange = onMedicoChange
            )
            OpcionesDisponibles(
                titulo = "Sedes",
                opciones = opciones.sedes,
                onSeleccion = onSedeChange
            )
            ValidatedTextField(
                valor = formulario.sede,
                etiqueta = "Sede",
                error = formulario.sedeError,
                onValorChange = onSedeChange
            )
            ValidatedTextField(
                valor = formulario.fecha,
                etiqueta = "Fecha (YYYY-MM-DD)",
                error = formulario.fechaError,
                keyboardType = KeyboardType.Number,
                onValorChange = onFechaChange
            )
            ValidatedTextField(
                valor = formulario.hora,
                etiqueta = "Hora (HH:mm)",
                error = formulario.horaError,
                keyboardType = KeyboardType.Number,
                onValorChange = onHoraChange
            )
            ValidatedTextField(
                valor = formulario.motivo,
                etiqueta = "Motivo",
                error = formulario.motivoError,
                onValorChange = onMotivoChange,
                minLines = 3
            )

            estado.mensajeExito?.let { mensaje ->
                MensajeExitoSolicitud(mensaje)
            }

            Button(
                onClick = onRegistrar,
                enabled = !estado.registrando,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (estado.registrando) "Registrando..." else "Registrar cita")
            }
            Button(
                onClick = onVolver,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}

@Composable
private fun OpcionesDisponibles(
    titulo: String,
    opciones: List<String>,
    onSeleccion: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        opciones.take(3).forEach { opcion ->
            AssistChip(
                onClick = { onSeleccion(opcion) },
                label = { Text(opcion) }
            )
        }
    }
}

@Composable
private fun MensajeExitoSolicitud(mensaje: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            text = mensaje,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
