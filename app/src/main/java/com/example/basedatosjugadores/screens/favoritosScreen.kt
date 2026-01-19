package com.example.basedatosjugadores.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.basedatosjugadores.R
import com.example.basedatosjugadores.server2.Liga
import com.example.basedatosjugadores.server2.TablaViewModel2
import com.example.basedatosjugadores.server2.ResultState2
import com.example.basedatosjugadores.server2.estadisticas2.Jugador2
import com.example.basedatosjugadores.server2.estadisticas2.cargarPlantillaDesdeCSV2
import com.example.basedatosjugadores.server2.getLogoResource
import com.example.basedatosjugadores.server2.estadisticas2.getFlagResource
import com.example.basedatosjugadores.server2.estadisticas2.getPlayerPhotoResource
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FavoritosScreen(
    navController: NavController,
    viewModel: TablaViewModel2 = TablaViewModel2(LocalContext.current)
) {
    val context = LocalContext.current
    val favoritos = viewModel.favoritos.collectAsState().value.toSet()

    // Cargo todos los jugadores para marcar favoritos
    var allPlayers by remember { mutableStateOf<List<Jugador2>>(emptyList()) }
    LaunchedEffect(favoritos) {
        allPlayers = cargarPlantillaDesdeCSV2(context).map { player ->
            player.apply { isFavorite = favoritos.contains(player.player) }
        }
    }
    val playersSet = remember(allPlayers) { allPlayers.map { it.player }.toSet() }

    var selectedTab by remember { mutableStateOf("Liga") }

    // Variables para la barra inferior
    var competicionesExpanded by remember { mutableStateOf(false) }
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
        topBar = {
            TopAppBar(
                title = { Text("⭐ Favoritos") },
                backgroundColor = Color(0xFF215679),
                contentColor = Color.White,
                actions = {
                    Icon(Icons.Filled.Star, "Ligas",
                        tint = if (selectedTab == "Liga") Color.Yellow else Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { selectedTab = "Liga" }
                    )
                    Icon(Icons.Filled.SportsSoccer, "Equipos",
                        tint = if (selectedTab == "Equipo") Color.Yellow else Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { selectedTab = "Equipo" }
                    )
                    Icon(Icons.Outlined.Person, "Jugadores",
                        tint = if (selectedTab == "Jugador") Color.Yellow else Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { selectedTab = "Jugador" }
                    )
                }
            )
        },
        backgroundColor = Color(0xFF272e3f),
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
                    IconButton(onClick = {
                        navController.navigate("menu")
                    }) {
                        Icon(Icons.Filled.Home, contentDescription = "Inicio", tint = Color.White)
                    }

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

                    IconButton(onClick = {
                        if (auth.currentUser != null) navController.navigate("favoritos_screen")
                        else navController.navigate("session_verification")
                    }) {
                        Icon(Icons.Filled.Star, contentDescription = "Favoritos", tint = Color.White)
                    }

                    IconButton(onClick = {
                        if (currentUser != null) navController.navigate("session_verification")
                        else navController.navigate("login_screen")
                    }) {
                        Icon(Icons.Filled.Person, contentDescription = "Usuario", tint = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            when (selectedTab) {
                "Liga" -> {
                    val ligasFav = favoritos.filter { it == "La Liga" || it == "Premier League" }
                    if (ligasFav.isEmpty()) {
                        item {
                            Text("- No tienes favoritos en esta categoría", color = Color.LightGray)
                        }
                    } else {
                        items(ligasFav) { liga ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        // Al pulsar liga → tablaScreen2
                                        navController.navigate("tabla2")
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val logo = when (liga) {
                                    "La Liga" -> R.drawable.logo_la_liga
                                    "Premier League" -> R.drawable.logo_premier
                                    else -> getLogoResource(liga)
                                }
                                Image(
                                    painter = painterResource(id = logo),
                                    contentDescription = liga,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(end = 8.dp)
                                )
                                Text(liga, color = Color.White, fontSize = 18.sp)
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                }
                "Equipo" -> {
                    val equipos = favoritos.filter { fav ->
                        fav != "La Liga"
                                && fav != "Premier League"
                                && !playersSet.contains(fav)
                    }
                    if (equipos.isEmpty()) {
                        item {
                            Text("- No tienes favoritos en esta categoría", color = Color.LightGray)
                        }
                    } else {
                        items(equipos) { equipo ->
                            val encoded = java.net.URLEncoder.encode(equipo, "UTF-8")
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        // Al pulsar equipo → estadisticas equipo Screen2
                                        navController.navigate("estadisticas2/$encoded")
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = getLogoResource(equipo)),
                                    contentDescription = equipo,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .padding(end = 8.dp)
                                )
                                Text(equipo, color = Color.White, fontSize = 18.sp)
                            }
                            Divider(color = Color.Gray, thickness = 1.dp)
                        }
                    }
                }
                "Jugador" -> {
                    val jugadores = allPlayers.filter { it.isFavorite }
                    if (jugadores.isEmpty()) {
                        item {
                            Text("- No tienes favoritos en esta categoría", color = Color.LightGray)
                        }
                    } else {
                        items(jugadores) { jugador ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {
                                        // Al pulsar jugador → estadisticas jugador Screen
                                        navController.navigate("estadisticas_jugador/${jugador.id}")
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = getPlayerPhotoResource(jugador.player)),
                                    contentDescription = jugador.player,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .padding(end = 8.dp)
                                )
                                Image(
                                    painter = painterResource(id = getFlagResource(jugador.nation)),
                                    contentDescription = jugador.nation,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .padding(end = 8.dp)
                                )
                                Column {
                                    Text(jugador.player, color = Color.White, fontSize = 16.sp)
                                    Text(
                                        text = jugador.altPos?.let { "${jugador.mainPos} ($it)" }
                                            ?: jugador.mainPos,
                                        color = Color.LightGray,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                            Divider(color = Color.Gray, thickness = 0.5.dp)
                        }
                    }
                }
            }
        }
    }
}
