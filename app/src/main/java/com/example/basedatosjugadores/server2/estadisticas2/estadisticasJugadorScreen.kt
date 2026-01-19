package com.example.basedatosjugadores

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.basedatosjugadores.screens.PorteriaPalosVector
import com.example.basedatosjugadores.screens.PorteriaVector
import com.example.basedatosjugadores.server2.estadisticas2.Jugador2
import com.example.basedatosjugadores.server2.estadisticas2.cargarPlantillaDesdeCSV2
import com.example.basedatosjugadores.server2.Liga
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
fun getPlayerPhotoResource(playerName: String): Int {
    val context = LocalContext.current
    val photoName = playerName.lowercase().replace(" ", "_")
        .replace("á", "a").replace("à", "a").replace("é", "e").replace("í", "i")
        .replace("ó", "o").replace("ú", "u").replace("-", "0").replace("ã", "1")
        .replace("ë", "2").replace("ø", "3").replace("ñ", "4").replace("ě", "5")
        .replace("ü", "6").replace("ć", "7").replace("č", "8").replace("î", "9")
        .replace("ö", "10").replace("’", "11").replace("š", "12").replace("ğ", "13")
        .replace("ı", "d").replace("ä","14").replace("ï","15").replace("'","16")
        .replace("ł","t").replace("ń","17").replace("ý","18").replace("ă","19")
        .replace("ș","20").replace("ç","21").replace("ę","23")

    val resId = context.resources.getIdentifier(photoName, "drawable", context.packageName)
    return if (resId != 0) resId else R.drawable.placeholder
}

// Modelo de disparo
enum class ShotResult { Miss, AttemptSaved, Goal, Post }

data class Shot(
    val x: Double,
    val y: Double,
    val result: ShotResult,
    val shotType: String,
    val situation: String,
    val isFromInsideBox: Boolean,
    val matchDate: String,
    val homeTeamName: String,
    val awayTeamName: String
)

@Composable
fun EstadisticasJugadorScreen(playerId: String, navController: NavController) {
    // Validación básica de ID
    if (playerId.isEmpty() || playerId.toIntOrNull() == null) {
        Text("ID de jugador inválido", color = Color.Red)
        return
    }

    val context = LocalContext.current
    val pid = playerId.toIntOrNull() ?: -1

    var shotData by remember { mutableStateOf<List<Shot>>(emptyList()) }
    var mensajeError by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<ShotResult?>(null) }
    val scope = rememberCoroutineScope()

    // Carga jugadores para obtener nombre y estadísticas
    var playerName by remember { mutableStateOf("") }
    LaunchedEffect(pid) {
        val lista = withContext(Dispatchers.IO) { cargarPlantillaDesdeCSV2(context) }
        lista.find { it.id == pid }?.let { playerName = it.player }
    }

    // Datos de ligas para el menú de la barra inferior
    val ligas = listOf(
        Liga("Liga Española", R.drawable.logo_la_liga, "tabla2"),
        Liga("Premier League", R.drawable.logo_premier, "tabla1"),
        Liga("Serie A", R.drawable.logo_serie_a, "tabla3"),
        Liga("Bundesliga", R.drawable.logo_bundesliga, "tabla4"),
        Liga("Ligue 1", R.drawable.logo_ligue1, "tabla5"),
        Liga("Champions League", R.drawable.logo_champions, "tabla6"),
        Liga("Europa League", R.drawable.logo_europa, "tabla7")
    )
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    Scaffold(
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color(0xFF215679),
                contentPadding = PaddingValues(horizontal = 32.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Ícono Home
                    IconButton(onClick = {
                        navController.navigate("menu")
                    }) {
                        Icon(Icons.Filled.Home, contentDescription = "menu", tint = Color.White)
                    }

                    // Menú desplegable de competiciones
                    var competicionesExpanded by remember { mutableStateOf(false) }
                    Box {
                        IconButton(onClick = { competicionesExpanded = true }) {
                            Icon(Icons.Filled.SportsSoccer, contentDescription = "Competiciones", tint = Color.White)
                        }
                        DropdownMenu(
                            expanded = competicionesExpanded,
                            onDismissRequest = { competicionesExpanded = false }
                        ) {
                            ligas.forEach { liga ->
                                DropdownMenuItem(onClick = {
                                    competicionesExpanded = false
                                    if (auth.currentUser != null) {
                                        navController.navigate(liga.route)
                                    } else {
                                        navController.navigate("session_verification")
                                    }
                                }) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = liga.logoRes),
                                            contentDescription = liga.name,
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(text = liga.name)
                                    }
                                }
                            }
                        }
                    }

                    // Ícono Favoritos
                    IconButton(onClick = {
                        if (auth.currentUser != null)
                            navController.navigate("favoritos_screen")
                        else
                            navController.navigate("session_verification")
                    }) {
                        Icon(Icons.Filled.Star, contentDescription = "Favoritos", tint = Color.White)
                    }

                    // Ícono Usuario
                    IconButton(onClick = {
                        if (auth.currentUser != null)
                            navController.navigate("session_verification")
                        else
                            navController.navigate("login_screen")
                    }) {
                        Icon(Icons.Filled.Person, contentDescription = "Usuario", tint = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .background(Color(0xFF272e3f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto del jugador, si ya se obtuvo el nombre
            if (playerName.isNotEmpty()) {
                Image(
                    painter = painterResource(id = getPlayerPhotoResource(playerName)),
                    contentDescription = "Foto de $playerName",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(top = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(playerName, color = Color.White, style = MaterialTheme.typography.h6)
            }

            Text("Estadísticas del Jugador", color = Color.White, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                scope.launch {
                    val response = obtenerEstadisticasDeJugadorAPI(context, playerId)
                    if (response.startsWith("Error")) {
                        mensajeError = response
                        shotData = emptyList()
                    } else {
                        shotData = parseShotData(response)
                        mensajeError = if (shotData.isEmpty()) "No se encontraron tiros." else ""
                    }
                }
            }) {
                Text("Obtener estadísticas")
            }

            Spacer(modifier = Modifier.height(16.dp))
            if (mensajeError.isNotEmpty()) {
                Text(mensajeError, color = Color.Red)
            }

            if (shotData.isNotEmpty()) {
                Text(
                    "Filtrar por tipo de tiro:",
                    color = Color.White,
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Se elimina la opción "Post"
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip("Todos", selectedFilter == null) {
                        selectedFilter = null
                    }
                    FilterChip("AttemptSaved", selectedFilter == ShotResult.AttemptSaved) {
                        selectedFilter = ShotResult.AttemptSaved
                    }
                    FilterChip("Goal", selectedFilter == ShotResult.Goal) {
                        selectedFilter = ShotResult.Goal
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Ahora excluimos Miss y Post
                val displayedShots = shotData
                    .filter { it.result != ShotResult.Miss && it.result != ShotResult.Post }
                    .filter { selectedFilter == null || it.result == selectedFilter }

                Text(
                    "Tiros mostrados: ${displayedShots.size}",
                    color = Color.White,
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.height(16.dp))
                PorteriaCanvas(shots = displayedShots)
            }

            Spacer(modifier = Modifier.height(24.dp))
            EstadisticasRadarScreen(context = context, playerId = pid)
        }
    }
}

@Composable
fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        color = if (isSelected) MaterialTheme.colors.primary else Color.LightGray,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .height(32.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Text(text, color = if (isSelected) Color.White else Color.Black)
        }
    }
}

@Composable
fun PorteriaCanvas(shots: List<Shot>) {
    var selectedShot by remember { mutableStateOf<Shot?>(null) }
    val radiusMap: Map<Shot, State<Float>> = shots.associateWith { shot ->
        val isSelected = shot == selectedShot
        animateFloatAsState(targetValue = if (isSelected) 24f else 12f)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberVectorPainter(PorteriaVector),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            Image(
                painter = rememberVectorPainter(PorteriaPalosVector),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )

            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(shots) {
                        detectTapGestures { tapOffset ->
                            val w = size.width
                            val h = size.height
                            val xStart = 55.1566f / 400f
                            val xEnd = 319.844f / 400f
                            val yTop = 72f / 300f
                            val yBot = 167f / 300f

                            selectedShot = shots.find { shot ->
                                val xRaw = shot.x.toFloat()
                                val xNorm = if (xRaw > 1f) xRaw - 1f else xRaw
                                val yNorm = shot.y.toFloat().coerceIn(0f, 1f)
                                val xCanvas = (xStart + xNorm * (xEnd - xStart)) * w
                                val yCanvas = (yBot - yNorm * (yBot - yTop)) * h
                                (tapOffset - Offset(xCanvas, yCanvas)).getDistance() < 20f
                            }
                        }
                    }
            ) {
                val w = size.width
                val h = size.height
                val xStart = 55.1566f / 400f
                val xEnd = 319.844f / 400f
                val yTop = 72f / 300f
                val yBot = 167f / 300f

                shots.forEach { shot ->
                    val xRaw = shot.x.toFloat()
                    val xNorm = if (xRaw > 1f) xRaw - 1f else xRaw
                    val yNorm = shot.y.toFloat().coerceIn(0f, 1f)
                    val xCanvas = (xStart + xNorm * (xEnd - xStart)) * w
                    val yCanvas = (yBot - yNorm * (yBot - yTop)) * h

                    val color = when (shot.result) {
                        ShotResult.Goal -> Color.Green
                        ShotResult.AttemptSaved -> Color(0xFFFFA500)
                        // Ya no dibujamos Post porque llegan filtrados
                        else -> Color.Transparent
                    }

                    val radius = radiusMap[shot]?.value ?: 12f
                    drawCircle(color = color, radius = radius, center = Offset(xCanvas, yCanvas))
                }
            }
        }

        // Leyenda reducida: sólo Gol y Parada
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LegendItem(Color.Green, "Gol")
            LegendItem(Color(0xFFFFA500), "Parada")
        }

        selectedShot?.let { shot ->
            Spacer(modifier = Modifier.height(16.dp))
            Text("Detalles del disparo:", color = Color.White)
            Text("Fecha: ${shot.matchDate}", color = Color.White)
            Text("Equipos: ${shot.homeTeamName} vs ${shot.awayTeamName}", color = Color.White)
            Text("Tipo: ${shot.shotType}", color = Color.White)
            Text("Situación: ${shot.situation}", color = Color.White)
            Text(
                if (shot.isFromInsideBox) "Desde dentro del área" else "Desde fuera del área",
                color = Color.White
            )
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Canvas(modifier = Modifier.size(16.dp)) { drawCircle(color) }
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, fontSize = 14.sp, color = Color.White)
    }
}

// === Radar Chart ===
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EstadisticasRadarScreen(
    context: Context,
    playerId: Int,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var allPlayers by remember { mutableStateOf<List<Jugador2>>(emptyList()) }
    var player by remember { mutableStateOf<Jugador2?>(null) }
    var opponent by remember { mutableStateOf<Jugador2?>(null) }

    // Estados para búsqueda y dropdown
    var searchExpanded by remember { mutableStateOf(false) }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var selectedName by remember { mutableStateOf("Selecciona jugador") }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val lista = withContext(Dispatchers.IO) { cargarPlantillaDesdeCSV2(context) }
        allPlayers = lista
        player = lista.find { it.id == playerId }
    }

    player?.let { p ->
        val mismos = allPlayers.filter { it.mainPos == p.mainPos && it.id != p.id }
        val filteredList = mismos
            .filter { it.player.contains(searchQuery, ignoreCase = true) }
            .sortedBy { it.player }

        Column(
            modifier = modifier
                .padding(16.dp)
                .background(Color(0xFF272e3f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Jugador principal
            Text(
                "Rasgos del jugador — ${p.player}",
                color = Color.White,
                style = MaterialTheme.typography.h5
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = getPlayerPhotoResource(p.player)),
                    contentDescription = "Foto de ${p.player}",
                    modifier = Modifier.size(80.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(p.player, color = Color.White, style = MaterialTheme.typography.h6)
            }
            Spacer(Modifier.height(16.dp))

            Text(
                "Comparar con otro de posición ${p.mainPos}",
                color = Color.LightGray
            )
            Spacer(Modifier.height(8.dp))

            // 1) Campo de búsqueda instantánea
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    searchExpanded = it.isNotBlank()
                },
                label = { Text("Buscar jugador") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = MaterialTheme.colors.surface,
                    focusedBorderColor = MaterialTheme.colors.primary,
                    unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colors.primary,
                    textColor = MaterialTheme.colors.onSurface,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.LightGray
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (searchExpanded && filteredList.isNotEmpty()) {
                Card(
                    backgroundColor = MaterialTheme.colors.surface,
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 200.dp)
                ) {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        filteredList.forEach { j ->
                            Text(
                                text = j.player,
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedName = j.player
                                        opponent = j
                                        searchExpanded = false
                                    }
                                    .padding(vertical = 12.dp, horizontal = 16.dp)
                            )
                            Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f))
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            // 2) Dropdown tradicional
            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = !dropdownExpanded }
            ) {
                OutlinedTextField(
                    value = selectedName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Jugador (dropdown)") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = MaterialTheme.colors.surface,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                        textColor = MaterialTheme.colors.onSurface,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    mismos.sortedBy { it.player }.forEach { j ->
                        DropdownMenuItem(onClick = {
                            selectedName = j.player
                            opponent = j
                            dropdownExpanded = false
                        }) {
                            Text(j.player)
                        }
                    }
                }
            }

            // Mostrar oponente y radar comparativo
            opponent?.let { opp ->
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = getPlayerPhotoResource(opp.player)),
                        contentDescription = "Foto de ${opp.player}",
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(opp.player, color = Color.White, style = MaterialTheme.typography.h6)
                }
                Spacer(Modifier.height(16.dp))

                val metrics = listOf<Jugador2.() -> Double>(
                    { xAG }, { xg }, { gls.toDouble() }, { ast.toDouble() }, { rating }
                )
                val labels = listOf("xA/90", "xG/90", "Goles", "Asist", "Rating")
                val maxValues = metrics.map { sel -> mismos.map(sel).maxOrNull() ?: 1.0 }
                val playerVals = metrics.mapIndexed { i, sel -> (sel(p) / maxValues[i]).toFloat() }
                val oppVals = metrics.mapIndexed { i, sel -> (sel(opp) / maxValues[i]).toFloat() }
                val avgVals = metrics.mapIndexed { i, sel -> (mismos.map(sel).average() / maxValues[i]).toFloat() }

                RadarChartComparativo(
                    labels = labels,
                    playerValues = playerVals,
                    opponentValues = oppVals,
                    avgValues = avgVals
                )
            }
        }
    } ?: Text(
        "Cargando radar…",
        color = Color.LightGray,
        modifier = Modifier.padding(16.dp)
    )
}

// Nuevo composable para radar comparativo:
@Composable
fun RadarChartComparativo(
    labels: List<String>,
    playerValues: List<Float>,
    opponentValues: List<Float>,
    avgValues: List<Float>,
    modifier: Modifier = Modifier
        .size(300.dp)
        .padding(8.dp),
    maxValue: Float = 1f,
    playerColor: Color = Color(0xFF1EB980),
    opponentColor: Color = Color(0xFFE91E63),
    avgColor: Color = Color.Gray
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val radius = min(w, h) / 2 * 0.8f
        val center = Offset(w / 2, h / 2)
        val angleStep = 2 * PI.toFloat() / labels.size

        // 1. Niveles concéntricos
        repeat(5) { i ->
            drawCircle(
                color = Color.DarkGray.copy(alpha = 0.3f),
                radius = radius * (i + 1) / 5f,
                center = center,
                style = Stroke(width = 1f)
            )
        }

        // 2. Líneas radiales
        labels.indices.forEach { i ->
            val ang = -PI.toFloat() / 2 + i * angleStep
            val end = Offset(
                x = center.x + cos(ang) * radius,
                y = center.y + sin(ang) * radius
            )
            drawLine(
                color = Color.DarkGray.copy(alpha = 0.4f),
                start = center,
                end = end,
                strokeWidth = 1f
            )
        }

        // Función auxiliar para un punto en el polígono
        fun point(value: Float, index: Int): Offset {
            val ang = -PI.toFloat() / 2 + index * angleStep
            val r = radius * (value.coerceIn(0f, maxValue) / maxValue)
            return Offset(
                x = center.x + cos(ang) * r,
                y = center.y + sin(ang) * r
            )
        }

        // 3. Polígono jugador
        val pathPlayer = Path().apply {
            playerValues.forEachIndexed { idx, v ->
                val p = point(v, idx)
                if (idx == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
            }
            close()
        }
        drawPath(pathPlayer, playerColor.copy(alpha = 0.4f), style = Fill)
        drawPath(pathPlayer, playerColor, style = Stroke(width = 2f))

        // 4. Polígono oponente
        val pathOpponent = Path().apply {
            opponentValues.forEachIndexed { idx, v ->
                val p = point(v, idx)
                if (idx == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
            }
            close()
        }
        drawPath(pathOpponent, opponentColor.copy(alpha = 0.4f), style = Fill)
        drawPath(pathOpponent, opponentColor, style = Stroke(width = 2f))

        // 5. Polígono promedio (dash)
        val pathAvg = Path().apply {
            avgValues.forEachIndexed { idx, v ->
                val p = point(v, idx)
                if (idx == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y)
            }
            close()
        }
        drawPath(pathAvg, avgColor.copy(alpha = 0.3f), style = Fill)
        drawPath(
            pathAvg,
            avgColor,
            style = Stroke(
                width = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
            )
        )

        // 6. Etiquetas alrededor
        labels.forEachIndexed { idx, label ->
            val ang = -PI.toFloat() / 2 + idx * angleStep
            val labelPos = Offset(
                x = center.x + cos(ang) * (radius + 20f),
                y = center.y + sin(ang) * (radius + 20f)
            )
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    label,
                    labelPos.x,
                    labelPos.y + 4f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 32f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    }
}

suspend fun obtenerEstadisticasDeJugadorAPI(context: Context, playerId: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = context.assets.open("datosTiros.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            val json = String(buffer, Charsets.UTF_8)
            val arr = JsonParser.parseString(json).asJsonArray
            val filt = arr.filter { it.asJsonObject["playerId"].asString == playerId }
            if (filt.isEmpty()) return@withContext "[]"
            filt.toString()
        } catch (e: Exception) {
            "Error al leer JSON: ${e.message}"
        }
    }
}

fun parseShotData(response: String): List<Shot> {
    val shots = mutableListOf<Shot>()
    val arr = JsonParser.parseString(response).asJsonArray
    val gson = GsonBuilder().setLenient().create()
    for (elem in arr) {
        val obj = elem.asJsonObject
        val eventType = obj["eventType"].asString
        if (eventType == "Miss") continue
        val onGoalStr = obj["onGoalShot"]?.asString ?: continue
        val corrected = onGoalStr.replace("'", "\"")
        val map = gson.fromJson(corrected, Map::class.java)
        val rawX = (map["x"] as? Double) ?: continue
        val rawY = (map["y"] as? Double) ?: continue
        shots += Shot(
            x = if (rawX > 1.0) rawX - 1.0 else rawX,
            y = rawY,
            result = ShotResult.valueOf(eventType),
            shotType = obj["shotType"]?.asString ?: "Desconocido",
            situation = obj["situation"]?.asString ?: "Desconocida",
            isFromInsideBox = obj["isFromInsideBox"]?.asBoolean ?: false,
            matchDate = obj["matchDate"]?.asString ?: "Sin fecha",
            homeTeamName = obj["homeTeamName"]?.asString ?: "Local",
            awayTeamName = obj["awayTeamName"]?.asString ?: "Visitante"
        )
    }
    return shots
}
