package pe.edu.upeu.presentation.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun DetalleCitaScreen(
    citaId: Long,
    onVolver: () -> Unit,
    viewModel: DetalleCitaViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsState()

    LaunchedEffect(citaId) {
        viewModel.cargarCita(citaId)
    }

    DetalleCitaScreen(
        estado = estado,
        onVolver = onVolver,
        onCancelarClick = viewModel::mostrarDialogoCancelacion,
        onOcultarDialogo = viewModel::ocultarDialogoCancelacion,
        onMotivoChange = viewModel::onMotivoCancelacionChange,
        onConfirmarCancelacion = viewModel::confirmarCancelacion,
        modifier = modifier
    )
}

@Composable
fun DetalleCitaScreen(
    estado: DetalleCitaUiState,
    onVolver: () -> Unit,
    onCancelarClick: () -> Unit,
    onOcultarDialogo: () -> Unit,
    onMotivoChange: (String) -> Unit,
    onConfirmarCancelacion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(onClick = onVolver) {
            Text("Volver")
        }

        estado.mensaje?.let { mensaje ->
            MensajeError(mensaje = mensaje)
        }

        when (val fase = estado.fase) {
            DetalleCitaUiState.Fase.Cargando -> EstadoCarga(mensaje = "Cargando detalle")
            is DetalleCitaUiState.Fase.Error -> EstadoVacio(
                titulo = "Detalle no disponible",
                descripcion = fase.mensaje,
                textoAccion = "Volver",
                onAccion = onVolver
            )
            is DetalleCitaUiState.Fase.Contenido -> DetalleContenido(
                cita = fase.cita,
                onCancelarClick = onCancelarClick
            )
        }
    }

    if (estado.mostrarDialogoCancelacion) {
        DialogoCancelacion(
            motivo = estado.motivoCancelacion,
            onMotivoChange = onMotivoChange,
            onDismiss = onOcultarDialogo,
            onConfirmar = onConfirmarCancelacion
        )
    }
}

@Composable
private fun DetalleContenido(
    cita: DetalleCitaUi,
    onCancelarClick: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = cita.especialidad,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            DatoDetalle(etiqueta = "Medico", valor = cita.medico)
            DatoDetalle(etiqueta = "Sede", valor = cita.sede)
            DatoDetalle(etiqueta = "Fecha", valor = cita.fecha)
            DatoDetalle(etiqueta = "Hora", valor = cita.hora)
            DatoDetalle(etiqueta = "Estado", valor = cita.estado)
            DatoDetalle(etiqueta = "Indicaciones", valor = cita.detalleEstado)
            DatoDetalle(etiqueta = "Motivo", valor = cita.motivo)

            if (cita.puedeCancelar) {
                Button(onClick = onCancelarClick) {
                    Text("Cancelar cita")
                }
            }
        }
    }
}

@Composable
private fun DatoDetalle(
    etiqueta: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DialogoCancelacion(
    motivo: String,
    onMotivoChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirmar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cancelar cita") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Confirma la cancelacion de esta cita programada.")
                ValidatedTextField(
                    valor = motivo,
                    etiqueta = "Motivo de cancelacion",
                    error = null,
                    onValorChange = onMotivoChange,
                    minLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
