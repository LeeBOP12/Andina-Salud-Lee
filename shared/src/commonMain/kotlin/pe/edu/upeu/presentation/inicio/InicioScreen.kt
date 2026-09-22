package pe.edu.upeu.presentation.inicio

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.presentation.component.EstadoCarga
import pe.edu.upeu.presentation.component.EstadoVacio

@Composable
fun InicioScreen(
    viewModel: InicioViewModel = koinViewModel(),
    onMisCitasClick: () -> Unit,
    onSolicitarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsState()

    InicioScreen(
        estado = estado,
        onMisCitasClick = onMisCitasClick,
        onSolicitarClick = onSolicitarClick,
        onReintentar = viewModel::cargarInicio,
        modifier = modifier
    )
}

@Composable
fun InicioScreen(
    estado: InicioUiState,
    onMisCitasClick: () -> Unit,
    onSolicitarClick: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (val fase = estado.fase) {
            InicioUiState.Fase.Cargando -> item {
                EstadoCarga(mensaje = "Cargando inicio")
            }

            is InicioUiState.Fase.Error -> item {
                EstadoVacio(
                    titulo = "No se pudo cargar",
                    descripcion = fase.mensaje,
                    textoAccion = "Reintentar",
                    onAccion = onReintentar
                )
            }

            is InicioUiState.Fase.Contenido -> {
                item {
                    EncabezadoInicio(nombrePaciente = fase.nombrePaciente)
                }
                item {
                    ProximaCitaCard(proximaCita = fase.proximaCita)
                }
                item {
                    AccesosRapidos(
                        onMisCitasClick = onMisCitasClick,
                        onSolicitarClick = onSolicitarClick
                    )
                }
            }
        }
    }
}

@Composable
private fun EncabezadoInicio(nombrePaciente: String) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "Hola, $nombrePaciente",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Gestiona tus citas medicas desde AndinaSalud.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProximaCitaCard(proximaCita: ProximaCitaUi?) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Proxima cita",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            if (proximaCita == null) {
                Text(
                    text = "No tienes citas programadas.",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            } else {
                Text(
                    text = proximaCita.especialidad,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${proximaCita.medico} - ${proximaCita.sede}",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = proximaCita.fechaHora,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun AccesosRapidos(
    onMisCitasClick: () -> Unit,
    onSolicitarClick: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = onMisCitasClick,
            modifier = Modifier.weight(1f)
        ) {
            Text("Mis citas")
        }
        Button(
            onClick = onSolicitarClick,
            modifier = Modifier.weight(1f)
        ) {
            Text("Solicitar cita")
        }
    }
}
