

package com.example.miplata

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miplata.ui.screens.AgregarMovimientoScreen
import com.example.miplata.ui.screens.InicioScreen
import com.example.miplata.ui.screens.MetaAhorroScreen
import com.example.miplata.ui.screens.MovimientosScreen
import com.example.miplata.ui.screens.ResumenFinancieroScreen
import com.example.miplata.viewmodel.MovimientoViewModel

private val VerdePrincipal = Color(0xFF176B50)
private val VerdeClaro = Color(0xFFD9F3E5)
private val FondoAplicacion = Color(0xFFF5F8F6)
private val TextoPrincipal = Color(0xFF19352B)
private val RojoGasto = Color(0xFFB93838)

private val MiPlataColores = lightColorScheme(
    primary = VerdePrincipal,
    onPrimary = Color.White,
    primaryContainer = VerdeClaro,
    onPrimaryContainer = TextoPrincipal,
    secondary = Color(0xFF477D68),
    background = FondoAplicacion,
    onBackground = TextoPrincipal,
    surface = Color.White,
    onSurface = TextoPrincipal,
    error = RojoGasto
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(colorScheme = MiPlataColores) {
                MiPlataApp()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MiPlataApp() {
    var pantalla by rememberSaveable { mutableIntStateOf(0) }
    val viewModel: MovimientoViewModel = viewModel()

    val opciones = listOf(
        "Inicio",
        "Movimientos",
        "Agregar",
        "Metas",
        "Resumen"
    )

    val iconos = listOf("🏠", "📋", "➕", "🎯", "💰")

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                opciones.forEachIndexed { index, titulo ->
                    NavigationBarItem(
                        selected = pantalla == index,
                        onClick = { pantalla = index },
                        icon = {
                            Text(text = iconos[index])
                        },
                        label = {
                            Text(text = titulo, maxLines = 1,
                                style = MaterialTheme.typography.labelSmall,
                                overflow = TextOverflow.Ellipsis)
                        }
                    )
                }
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                AnimatedContent(
                    targetState = pantalla,
                    transitionSpec = {
                        (
                                fadeIn(animationSpec = tween(220)) +
                                        slideInHorizontally(
                                            animationSpec = tween(220)
                                        ) { ancho -> ancho / 12 }
                                ) togetherWith (
                                fadeOut(animationSpec = tween(150)) +
                                        slideOutHorizontally(
                                            animationSpec = tween(150)
                                        ) { ancho -> -ancho / 12 }
                                )
                    },
                    label = "Transicion entre pantallas"
                ) { pantallaActual ->
                    when (pantallaActual) {
                        0 -> InicioScreen(
                            viewModel = viewModel,
                            onAgregar = { pantalla = 2 },
                            onVerMovimientos = { pantalla = 1 }
                        )

                        1 -> MovimientosScreen(
                            viewModel = viewModel
                        )

                        2 -> AgregarMovimientoScreen(
                            viewModel = viewModel,
                            onGuardado = { pantalla = 1 }
                        )

                        3 -> MetaAhorroScreen()

                        4 -> ResumenFinancieroScreen(
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
