package com.example.basedatosjugadores.server2.estadisticas2

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import com.example.basedatosjugadores.server2.Liga
import com.example.basedatosjugadores.server2.TablaViewModel2
import com.example.basedatosjugadores.server2.getLogoResource

@Composable
fun PlantillaEquipoScreen2(
    nombreEquipo: String,
    navController: NavController,
    viewModel: TablaViewModel2 = TablaViewModel2(LocalContext.current)
) {
    val context = LocalContext.current
    var listaJugadores by remember { mutableStateOf<List<Jugador2>>(emptyList()) }
    val horizontalScrollState = rememberScrollState()
    val favoritosState = viewModel.favoritos.collectAsState()
    val auth = viewModel.auth

    // dropdown de competiciones
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

    LaunchedEffect(nombreEquipo) {
        listaJugadores = cargarPlantillaDesdeCSV2(context)
            .filter { it.Squad.trim().equals(nombreEquipo.trim(), ignoreCase = true) }
            .sortedWith(compareBy({ getPosOrder(it.mainPos) }, { it.player }))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Plantilla de $nombreEquipo")
                        Spacer(Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = getLogoResource(nombreEquipo)),
                            contentDescription = "Logo $nombreEquipo",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(start = 4.dp)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.rennes),
                            contentDescription = "Atrás"
                        )
                    }
                },
                backgroundColor = Color(0xFF215679),
                contentColor = Color.White
            )
        },
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
                        Icon(Icons.Filled.Home, contentDescription = "menu", tint = Color.White)
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
                                    if (auth.currentUser != null)
                                        navController.navigate(liga.route)
                                    else
                                        navController.navigate("session_verification")
                                }) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            painter = painterResource(id = liga.logoRes),
                                            contentDescription = liga.name,
                                            modifier = Modifier
                                                .size(24.dp)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(text = liga.name)
                                    }
                                }
                            }
                        }
                    }
                    IconButton(onClick = {
                        if (auth.currentUser != null)
                            navController.navigate("favoritos_screen")
                        else
                            navController.navigate("session_verification")
                    }) {
                        Icon(Icons.Filled.Star, contentDescription = "Favoritos", tint = Color.White)
                    }
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
        // Un único LazyColumn con header + items, usa contentPadding para top + bottom
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding()
            )
        ) {
            // Header de columnas
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE3F2FD))
                        .padding(vertical = 1.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .padding(vertical = 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Imagen", fontWeight = FontWeight.Bold)
                    }
                    Row(
                        modifier = Modifier
                            .horizontalScroll(horizontalScrollState)
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5))
                            .padding(vertical = 1.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Jugador", Modifier.width(140.dp), fontWeight = FontWeight.Bold)
                        Text("Bandera", Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                        Text("Pos", Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                        Text("Goles", Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                        Text("Asistencias", Modifier.width(100.dp), fontWeight = FontWeight.Bold)
                        Text("G+A", Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                        Text("Rating", Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                        Text("xG 90'", Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                        Text("xA 90'", Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                        Text("xG+xA", Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                    }
                }
                Divider(color = Color.Gray, thickness = 1.dp)
            }

            // Filas de jugadores
            items(listaJugadores) { jugador ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
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

                    Row(
                        modifier = Modifier
                            .horizontalScroll(horizontalScrollState)
                            .fillMaxWidth()
                            .background(Color(0xFFF5F5F5))
                            .padding(vertical = 1.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isFavorito = favoritosState.value.contains(jugador.player)
                        IconButton(
                            modifier = Modifier.size(24.dp),
                            onClick = { viewModel.toggleFavorito(jugador.player) }
                        ) {
                            if (isFavorito) {
                                Icon(Icons.Filled.Favorite, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp))
                            } else {
                                Icon(Icons.Outlined.FavoriteBorder, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                            }
                        }

                        Row(
                            modifier = Modifier
                                .clickable { navController.navigate("estadisticas_jugador/${jugador.id}") },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(jugador.player, Modifier.width(140.dp))
                        }

                        Image(
                            painter = painterResource(id = getFlagResource(jugador.nation)),
                            contentDescription = jugador.nation,
                            modifier = Modifier
                                .width(80.dp)
                                .size(18.dp)
                        )
                        Text(
                            text = jugador.altPos?.let { "${jugador.mainPos} ($it)" } ?: jugador.mainPos,
                            Modifier.width(80.dp)
                        )
                        Text(jugador.gls.toString(), Modifier.width(80.dp))
                        Text(jugador.ast.toString(), Modifier.width(100.dp))
                        Text((jugador.gls + jugador.ast).toString(), Modifier.width(80.dp))
                        Text(jugador.rating.toString(), Modifier.width(80.dp))
                        Text("%.2f".format(jugador.xg), Modifier.width(80.dp))
                        Text("%.2f".format(jugador.xAG), Modifier.width(80.dp))
                        Text("%.2f".format(jugador.xg + jugador.xAG), Modifier.width(80.dp))
                    }
                }
                Divider(color = Color.LightGray, thickness = 0.5.dp)
            }
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

fun getPosOrder(pos: String): Int {
    return when (pos.uppercase()) {
        "GK" -> 1
        "DF" -> 2
        "MF" -> 3
        "FW" -> 4
        else -> 5
    }
}
