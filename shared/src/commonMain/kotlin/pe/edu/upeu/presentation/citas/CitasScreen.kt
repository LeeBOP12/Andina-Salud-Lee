package pe.edu.upeu.presentation.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.presentation.component.EstadoCarga
import pe.edu.upeu.presentation.component.EstadoVacio
import pe.edu.upeu.presentation.component.MensajeError
import pe.edu.upeu.presentation.component.ValidatedTextField

@Composable
fun CitasScreen(
    viewModel: CitasViewModel = koinViewModel(),
    onVerDetalle: (Long) -> Unit,
    onSolicitarCita: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsState()

    CitasScreen(
        estado = estado,
        onBusquedaChange = viewModel::onBusquedaChange,
        onFiltroChange = viewModel::onFiltroChange,
        onReintentar = viewModel::cargarCitas,
        onVerDetalle = onVerDetalle,
        onSolicitarCita = onSolicitarCita,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitasScreen(
    estado: CitasUiState,
    onBusquedaChange: (String) -> Unit,
    onFiltroChange: (FiltroEstado) -> Unit,
    onReintentar: () -> Unit,
    onVerDetalle: (Long) -> Unit,
    onSolicitarCita: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            EncabezadoCitas(onSolicitarCita = onSolicitarCita)
        }

        item {
            ValidatedTextField(
                valor = estado.busqueda,
                etiqueta = "Buscar por especialidad o medico",
                error = null,
                onValorChange = onBusquedaChange
            )
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FiltroEstado.entries.forEach { filtro ->
                    FilterChip(
                        selected = estado.filtro == filtro,
                        onClick = { onFiltroChange(filtro) },
                        label = { Text(filtro.etiqueta) }
                    )
                }
            }
        }

        when (val fase = estado.fase) {
            CitasUiState.Fase.Cargando -> item {
                EstadoCarga(mensaje = "Cargando citas")
            }

            CitasUiState.Fase.Vacia -> item {
                EstadoVacio(
                    titulo = "Sin citas",
                    descripcion = "No hay citas que coincidan con los filtros seleccionados."
                )
            }

            is CitasUiState.Fase.Error -> item {
                MensajeError(mensaje = fase.mensaje)
                EstadoVacio(
                    titulo = "No se pudo cargar",
                    descripcion = "Vuelve a intentar la carga de citas.",
                    textoAccion = "Reintentar",
                    onAccion = onReintentar
                )
            }

            is CitasUiState.Fase.Contenido -> items(
                items = fase.citas,
                key = { it.id }
            ) { cita ->
                CitaCard(
                    cita = cita,
                    onVerDetalle = { onVerDetalle(cita.id) }
                )
            }
        }
    }
}

@Composable
private fun EncabezadoCitas(
    onSolicitarCita: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Mis citas",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Consulta tus citas ordenadas por fecha y filtra por estado.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        OutlinedButton(onClick = onSolicitarCita) {
            Text("Solicitar cita")
        }
    }
}

@Composable
private fun CitaCard(
    cita: CitaUi,
    onVerDetalle: () -> Unit
) {
    ElevatedCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = cita.especialidad,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = cita.estado,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = cita.medico,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "${cita.sede} - ${cita.fechaHora}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = cita.detalleEstado,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            OutlinedButton(onClick = onVerDetalle) {
                Text("Ver detalle")
            }
        }
    }
}
