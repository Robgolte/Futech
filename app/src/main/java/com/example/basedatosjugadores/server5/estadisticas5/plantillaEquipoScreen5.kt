package com.example.basedatosjugadores.server5.estadisticas5

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.basedatosjugadores.R

@Composable
fun PlantillaEquipoScreen5(nombreEquipo: String, navController: NavController) {
    val context = LocalContext.current
    var listaJugadores by remember { mutableStateOf<List<Jugador5>>(emptyList()) }
    var showInfoDialog by remember { mutableStateOf(false) }
    val horizontalScrollState = rememberScrollState()

    LaunchedEffect(nombreEquipo) {
        // Filtramos usando trim() para eliminar espacios innecesarios
        listaJugadores = cargarPlantillaDesdeCSV5(
            context
        )
            .filter { it.Squad.trim().equals(nombreEquipo.trim(), ignoreCase = true) }
            .sortedWith(compareBy({ getPosOrder(it.mainPos) }, { it.player }))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plantilla de $nombreEquipo") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.rennes),
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            // CABECERA FIJA
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .background(Color(0xFFE3F2FD))
                        .padding(vertical = 1.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Imagen",
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier
                        .horizontalScroll(horizontalScrollState)
                        .fillMaxWidth()
                        .background(Color(0xFFF5F5F5))
                        .padding(vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Jugador",
                        modifier = Modifier.width(140.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Bandera",
                        modifier = Modifier.width(80.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Pos",
                        modifier = Modifier.width(80.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Goles",
                        modifier = Modifier.width(80.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Asistencias",
                        modifier = Modifier.width(100.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "xG",
                        modifier = Modifier.width(80.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Starts",
                        modifier = Modifier.width(80.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "G+A",
                        modifier = Modifier.width(80.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "xa",
                        modifier = Modifier.width(80.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "xG+xa",
                        modifier = Modifier.width(80.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Divider(color = Color.Gray, thickness = 1.dp)

            // Lista de jugadores
            LazyColumn {
                items(listaJugadores) { jugador ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Sección fija: Imagen del jugador
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .background(Color(0xFFE3F2FD))
                                .padding(vertical = 1.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = getPlayerPhotoResource(jugador.player)),
                                contentDescription = jugador.player,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        // Sección desplazable: Datos del jugador
                        Row(
                            modifier = Modifier
                                .horizontalScroll(horizontalScrollState)
                                .fillMaxWidth()
                                .background(Color(0xFFF5F5F5))
                                .padding(vertical = 1.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = jugador.player,
                                modifier = Modifier.width(140.dp)
                            )
                            Image(
                                painter = painterResource(id = getFlagResource(jugador.nation)),
                                contentDescription = jugador.nation,
                                modifier = Modifier
                                    .width(80.dp)
                                    .size(18.dp)
                            )
                            Text(
                                text = if (jugador.altPos != null)
                                    "${jugador.mainPos} (${jugador.altPos})"
                                else
                                    jugador.mainPos,
                                modifier = Modifier.width(80.dp)
                            )
                            Text(
                                text = jugador.gls.toString(),
                                modifier = Modifier.width(80.dp)
                            )
                            Text(
                                text = jugador.ast.toString(),
                                modifier = Modifier.width(100.dp)
                            )
                            Text(
                                text = "%.2f".format(jugador.xg),
                                modifier = Modifier.width(80.dp)
                            )
                            Text(
                                text = jugador.starts.toString(),
                                modifier = Modifier.width(80.dp)
                            )
                            Text(
                                text = (jugador.gls + jugador.ast).toString(),
                                modifier = Modifier.width(80.dp)
                            )
                            Text(
                                text = "%.2f".format(jugador.xAG),
                                modifier = Modifier.width(80.dp)
                            )
                            Text(
                                text = "%.2f".format(jugador.xg + jugador.xAG),
                                modifier = Modifier.width(80.dp)
                            )
                        }
                    }
                    Divider(color = Color.LightGray, thickness = 0.5.dp)
                }
            }
        }

        // Diálogo informativo (opcional)
        if (showInfoDialog) {
            AlertDialog(
                onDismissRequest = { showInfoDialog = false },
                title = { Text("Significado de cada valor") },
                text = {
                    Column {
                        Text("• Info: Pulsa para ver esta leyenda")
                        Text("• Jugador: Nombre del jugador")
                        Text("• Bandera: Nacionalidad del jugador (imagen)")
                        Text("• Pos: Posición principal (y alternativa, si existe)")
                        Text("• Goles: Número de goles")
                        Text("• Asistencias: Número de asistencias")
                        Text("• xG: Goles esperados")
                        Text("• Starts: Partidos iniciados")
                        Text("• G+A: Goles y asistencias reales")
                        Text("• xa: Asistencias esperadas")
                        Text("• xG+xa: Suma de xG y xa")
                    }
                },
                confirmButton = {
                    Button(onClick = { showInfoDialog = false }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}

@Composable
fun getFlagResource(nation: String): Int {
    val context = LocalContext.current
    val flagName = nation.lowercase()
    val resId = context.resources.getIdentifier(flagName, "drawable", context.packageName)
    return if (resId != 0) resId else R.drawable.placeholder
}

@Composable
fun getPlayerPhotoResource(playerName: String): Int {
    val context = LocalContext.current
    val photoName = playerName.lowercase().replace(" ", "_")
        .replace("á", "a")
        .replace("à", "a")
        .replace("é", "e")
        .replace("í", "i")
        .replace("ó", "o")
        .replace("ú", "u")
        .replace("-", "0")
        .replace("ã", "1")
        .replace("ë", "2")
        .replace("ø", "3")
        .replace("ñ", "4")
        .replace("ě", "5")
        .replace("ü", "6")
        .replace("ć", "7")
        .replace("č", "8")
        .replace("î", "9")
        .replace("ö", "10")
        .replace("’", "11")
        .replace("š", "12")
        .replace("ğ", "13")
        .replace("ı", "d")
        .replace("ä","14")
        .replace("ï","15")
        .replace("'","16")
        .replace("ł","t")
        .replace("ń","17")
        .replace("ý","18")
        .replace("ă","19")
        .replace("ș","20")
        .replace("ç","21")
        .replace("ı","22")
        .replace("ę","23")

    val resId = context.resources.getIdentifier(photoName, "drawable", context.packageName)
    return if (resId != 0) resId else R.drawable.placeholder
}

fun getPosOrder(pos: String): Int {
    return when (pos.uppercase()) {
        "GK" -> 1
        "DF" -> 2
        "MF" -> 3
        "FW" -> 4
        else -> 5
    }
}