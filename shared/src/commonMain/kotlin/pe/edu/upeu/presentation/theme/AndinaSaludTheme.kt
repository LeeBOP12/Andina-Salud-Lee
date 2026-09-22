package pe.edu.upeu.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AndinaSaludTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) AndinaDarkColors else AndinaLightColors,
        typography = AndinaTypography,
        content = content
    )
}
