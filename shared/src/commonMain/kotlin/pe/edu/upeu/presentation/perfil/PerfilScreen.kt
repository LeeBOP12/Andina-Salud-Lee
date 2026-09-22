package pe.edu.upeu.presentation.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import pe.edu.upeu.presentation.component.EstadoCarga
import pe.edu.upeu.presentation.component.EstadoVacio

@Composable
fun PerfilScreen(
    modoOscuro: Boolean,
    onModoOscuroChange: (Boolean) -> Unit,
    viewModel: PerfilViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val estado by viewModel.uiState.collectAsState()

    PerfilScreen(
        estado = estado,
        modoOscuro = modoOscuro,
        onModoOscuroChange = onModoOscuroChange,
        onReintentar = viewModel::cargarPerfil,
        modifier = modifier
    )
}

@Composable
fun PerfilScreen(
    estado: PerfilUiState,
    modoOscuro: Boolean,
    onModoOscuroChange: (Boolean) -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Perfil y ajustes",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        when (val fase = estado.fase) {
            PerfilUiState.Fase.Cargando -> item {
                EstadoCarga(mensaje = "Cargando perfil")
            }

            is PerfilUiState.Fase.Error -> item {
                EstadoVacio(
                    titulo = "Perfil no disponible",
                    descripcion = fase.mensaje,
                    textoAccion = "Reintentar",
                    onAccion = onReintentar
                )
            }

            is PerfilUiState.Fase.Contenido -> {
                item {
                    DatosPacienteCard(fase)
                }
                item {
                    TemaCard(
                        modoOscuro = modoOscuro,
                        onModoOscuroChange = onModoOscuroChange
                    )
                }
            }
        }
    }
}

@Composable
private fun DatosPacienteCard(paciente: PerfilUiState.Fase.Contenido) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = paciente.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            DatoPerfil("Documento", paciente.documento)
            DatoPerfil("Correo", paciente.correo)
            DatoPerfil("Telefono", paciente.telefono)
        }
    }
}

@Composable
private fun TemaCard(
    modoOscuro: Boolean,
    onModoOscuroChange: (Boolean) -> Unit
) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Tema oscuro",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Se aplica inmediatamente a toda la aplicacion.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = modoOscuro,
                onCheckedChange = onModoOscuroChange
            )
        }
    }
}

@Composable
private fun DatoPerfil(
    etiqueta: String,
    valor: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
