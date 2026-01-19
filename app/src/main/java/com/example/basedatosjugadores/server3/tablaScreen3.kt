package com.example.basedatosjugadores.server3

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.basedatosjugadores.R
import com.example.basedatosjugadores.server2.Liga
import com.google.firebase.auth.FirebaseAuth

@Composable
fun TablaScreen3(
    navController: NavController,
    viewModel: TablaViewModel3 = TablaViewModel3(LocalContext.current)
) {
    val tablaState = viewModel.tabla.collectAsState()
    val auth = FirebaseAuth.getInstance()
    val ligas = listOf(
        Liga("Liga Española", R.drawable.logo_la_liga, "tabla2"),
        Liga("Premier League", R.drawable.logo_premier, "tabla1"),
        Liga("Serie A", R.drawable.logo_serie_a, "tabla3"),
        Liga("Bundesliga", R.drawable.logo_bundesliga, "tabla4"),
        Liga("Ligue 1", R.drawable.logo_ligue1, "tabla5"),
        Liga("Champions League", R.drawable.logo_champions, "tabla6"),
        Liga("Europa League", R.drawable.logo_europa, "tabla7")
    )
    var competicionesExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Título de la barra superior
                        Text(
                            text = "Clasificación Serie A",
                            modifier = Modifier.weight(1f)
                        )

                        // Imagen del logo a la derecha
                        Image(
                            painter = painterResource(id = R.drawable.logo_serie_a),
                            contentDescription = "Logo Serie A",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
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
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val tabla = tablaState.value) {
                is ResultState3.Loading -> {
                    CircularProgressIndicator()
                }
                is ResultState3.Success -> {
                    val equipos = tabla.data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        // Encabezado de la tabla
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .background(MaterialTheme.colors.primary),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Pos",
                                modifier = Modifier
                                    .weight(0.2f)
                                    .padding(start = 8.dp),
                                color = MaterialTheme.colors.onPrimary
                            )
                            Text(
                                text = "Equipo",
                                modifier = Modifier
                                    .weight(0.6f)
                                    .padding(start = 8.dp),
                                color = MaterialTheme.colors.onPrimary
                            )
                            Text(
                                text = "Pts",
                                modifier = Modifier.weight(0.2f),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GF",
                                modifier = Modifier.weight(0.2f),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GC",
                                modifier = Modifier.weight(0.2f),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GD",
                                modifier = Modifier.weight(0.2f),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Filas dinámicas para los equipos
                        LazyColumn {
                            itemsIndexed(equipos) { index, equipo ->
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .background(
                                                if (index % 2 == 0) MaterialTheme.colors.background
                                                else MaterialTheme.colors.surface.copy(alpha = 0.8f)
                                            )
                                    ) {
                                        // Posición con color destacado
                                        Row(
                                            modifier = Modifier
                                                .weight(0.2f)
                                                .padding(start = 8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .width(4.dp)
                                                    .height(24.dp)
                                                    .background(getPositionColor(equipo.RL, equipos.size))
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = equipo.RL.toString(),
                                                style = MaterialTheme.typography.body2,
                                                color = MaterialTheme.colors.onSurface
                                            )
                                        }

                                        // Logo y nombre del equipo
                                        val logoResId = getLogoResource(equipo.Equipo)
                                        Image(
                                            painter = painterResource(id = logoResId),
                                            contentDescription = "${equipo.Equipo} Logo",
                                            modifier = Modifier
                                                .size(24.dp)
                                                .padding(end = 8.dp)
                                        )
                                        Text(
                                            text = equipo.Equipo,
                                            modifier = Modifier
                                                .weight(0.6f)
                                                .padding(horizontal = 8.dp),
                                            style = MaterialTheme.typography.body2.copy(fontSize = 14.sp)
                                        )

                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(
                                            text = equipo.Pts.toString(),
                                            modifier = Modifier.weight(0.2f),
                                            style = MaterialTheme.typography.body2,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = equipo.GF.toString(),
                                            modifier = Modifier.weight(0.2f),
                                            style = MaterialTheme.typography.body2,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = equipo.GC.toString(),
                                            modifier = Modifier.weight(0.2f),
                                            style = MaterialTheme.typography.body2,
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = equipo.DG.toString(),
                                            modifier = Modifier.weight(0.2f),
                                            style = MaterialTheme.typography.body2,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                    Divider(color = Color.Gray, thickness = 1.dp)
                                }
                            }
                        }
                    }
                }
                is ResultState3.Error -> {
                    Text("Error: ${tabla.error}")
                }
            }
        }
    }
}

@Composable
fun getLogoResource(teamName: String): Int {
    val context = LocalContext.current
    val cleanedTeamName = teamName
        .lowercase()
        .replace(" ", "_")
        .replace("á", "a")
        .replace("é", "e")
        .replace("í", "i")
        .replace("ó", "o")
        .replace("ú", "u")
        .replace("'", "i")

    return try {
        val resId = context.resources.getIdentifier(cleanedTeamName, "drawable", context.packageName)
        if (resId != 0) resId else R.drawable.placeholder
    } catch (e: Exception) {
        R.drawable.placeholder
    }
}

private fun getPositionColor(pos: Int, totalEquipos: Int): Color {
    return when {
        pos == 1 -> Color(0xFFFFD700) // Oro (campeón)
        pos in 2..4 -> Color(0xFF1E90FF) // Azul (Champions League)
        pos == 5 -> Color(0xFFFF6C41) // Europa League
        pos == 6 -> Color(0xFF4CAF50) // Fase previa Conference League
        pos > totalEquipos - 3 -> Color(0xFFFF0000) // Descenso
        else -> Color.Transparent // Sin color especial
    }
}
