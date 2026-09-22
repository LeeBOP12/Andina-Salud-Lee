package pe.edu.upeu

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import pe.edu.upeu.presentation.navigation.AppNavHost
import pe.edu.upeu.presentation.theme.AndinaSaludTheme

@Composable
@Preview
fun App() {
    KoinContext {
        var modoOscuro by rememberSaveable { mutableStateOf(false) }

        AndinaSaludTheme(darkTheme = modoOscuro) {
            AppNavHost(
                modoOscuro = modoOscuro,
                onModoOscuroChange = { modoOscuro = it },
                modifier = Modifier
            )
        }
    }
}
