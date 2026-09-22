package pe.edu.upeu.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import pe.edu.upeu.presentation.citas.CitasScreen
import pe.edu.upeu.presentation.detalle.DetalleCitaScreen
import pe.edu.upeu.presentation.inicio.InicioScreen
import pe.edu.upeu.presentation.perfil.PerfilScreen
import pe.edu.upeu.presentation.solicitud.SolicitudScreen

@Composable
fun AppNavHost(
    modoOscuro: Boolean,
    onModoOscuroChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var destinoActual by rememberSaveable { mutableStateOf(Destino.Inicio.ruta) }
    var citaSeleccionadaId by rememberSaveable { mutableLongStateOf(1L) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BarraInferior(
                destinoActual = destinoActual,
                onDestinoClick = { destinoActual = it.ruta }
            )
        }
    ) { paddingValues ->
        val contenidoModifier = Modifier.padding(paddingValues)

        when (destinoActual) {
            Destino.Inicio.ruta -> InicioScreen(
                onMisCitasClick = { destinoActual = Destino.Citas.ruta },
                onSolicitarClick = { destinoActual = Destino.Solicitud.ruta },
                modifier = contenidoModifier
            )

            Destino.Citas.ruta -> CitasScreen(
                onVerDetalle = { citaId ->
                    citaSeleccionadaId = citaId
                    destinoActual = Destino.Detalle(citaId).ruta
                },
                onSolicitarCita = { destinoActual = Destino.Solicitud.ruta },
                modifier = contenidoModifier
            )

            Destino.Perfil.ruta -> PerfilScreen(
                modoOscuro = modoOscuro,
                onModoOscuroChange = onModoOscuroChange,
                modifier = contenidoModifier
            )

            Destino.Solicitud.ruta -> SolicitudScreen(
                onVolver = { destinoActual = Destino.Citas.ruta },
                modifier = contenidoModifier
            )

            else -> DetalleCitaScreen(
                citaId = citaSeleccionadaId,
                onVolver = { destinoActual = Destino.Citas.ruta },
                modifier = contenidoModifier
            )
        }
    }
}

@Composable
private fun BarraInferior(
    destinoActual: String,
    onDestinoClick: (Destino) -> Unit
) {
    NavigationBar {
        Destino.barraInferior.forEach { destino ->
            NavigationBarItem(
                selected = destinoActual == destino.ruta,
                onClick = { onDestinoClick(destino) },
                icon = { Text(destino.titulo.take(1)) },
                label = { Text(destino.titulo) }
            )
        }
    }
}
