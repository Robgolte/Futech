package com.example.basedatosjugadores.server2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.basedatosjugadores.R
import java.net.URLEncoder

@Composable
fun TablaScreen2(
    navController: NavController,
    viewModel: TablaViewModel2 = TablaViewModel2(LocalContext.current)
) {
    val tablaState = viewModel.tabla.collectAsState()
    val favoritosState = viewModel.favoritos.collectAsState()
    val auth = viewModel.auth
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Clasificación La Liga",
                            modifier = Modifier.weight(1f),
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        // Logo sin tint ni recorte
                        Image(
                            painter = painterResource(id = R.drawable.logo_la_liga),
                            contentDescription = "Logo La Liga",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp)
                        )
                        IconButton(onClick = { viewModel.toggleFavorito("La Liga") }) {
                            val isFavorito = favoritosState.value.contains("La Liga")
                            if (isFavorito) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Favorito",
                                    tint = Color.Red,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Outlined.FavoriteBorder,
                                    contentDescription = "No favorito",
                                    tint = Color.Gray,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                },
                backgroundColor = Color(0xFF215679)
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
                        if (auth.currentUser != null) {
                            navController.navigate("favoritos_screen")
                        } else {
                            navController.navigate("session_verification")
                        }
                    }) {
                        Icon(Icons.Filled.Star, contentDescription = "Favoritos", tint = Color.White)
                    }
                    IconButton(onClick = {
                        if (auth.currentUser != null) {
                            navController.navigate("session_verification")
                        } else {
                            navController.navigate("login_screen")
                        }
                    }) {
                        Icon(Icons.Filled.Person, contentDescription = "Usuario", tint = Color.White)
                    }
                }
            }
        }
    ) { innerPadding ->
        when (val resultado = tablaState.value) {
            is ResultState2.Loading -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF2de0cb))
                }
            }
            is ResultState2.Error -> {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error: ${resultado.error}", color = Color.Red)
                }
            }
            is ResultState2.Success -> {
                val equipos = resultado.data
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding
                ) {
                    // Encabezado de columna
                    item {
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
                                modifier = Modifier
                                    .weight(0.3f)
                                    .padding(end = 6.dp),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GF",
                                modifier = Modifier
                                    .weight(0.3f)
                                    .padding(end = 8.dp),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GC",
                                modifier = Modifier
                                    .weight(0.3f)
                                    .padding(end = 24.dp),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "GD",
                                modifier = Modifier
                                    .weight(0.3f)
                                    .padding(end = 28.dp),
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Filas de equipos
                    itemsIndexed(equipos) { index, equipo ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(
                                    if (index % 2 == 0) MaterialTheme.colors.background
                                    else MaterialTheme.colors.surface.copy(alpha = 0.8f)
                                )
                        ) {
                            // Posición con barra
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

                            // Logo y nombre
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
                                    .padding(horizontal = 8.dp)
                                    .clickable {
                                        navController.navigate(
                                            "estadisticas2/${URLEncoder.encode(equipo.Equipo, "UTF-8")}"
                                        )
                                    },
                                style = MaterialTheme.typography.body2.copy(fontSize = 14.sp)
                            )

                            // Pts, GF, GC, GD
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = equipo.Pts.toString(),
                                    style = MaterialTheme.typography.body2,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(0.2f)
                                )
                                Text(
                                    text = equipo.GF.toString(),
                                    style = MaterialTheme.typography.body2,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(0.2f)
                                )
                                Text(
                                    text = equipo.GC.toString(),
                                    style = MaterialTheme.typography.body2,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(0.2f)
                                )
                                Text(
                                    text = equipo.DG.toString(),
                                    style = MaterialTheme.typography.body2,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(0.2f)
                                )
                            }

                            // Icono de favorito
                            val isFav = favoritosState.value.contains(equipo.Equipo)
                            IconButton(
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(0.dp),
                                onClick = { viewModel.toggleFavorito(equipo.Equipo) }
                            ) {
                                if (isFav) {
                                    Icon(
                                        imageVector = Icons.Filled.Favorite,
                                        contentDescription = "Eliminar favorito",
                                        tint = Color.Red,
                                        modifier = Modifier.size(16.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Añadir favorito",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                        Divider(color = Color.Gray, thickness = 1.dp)
                    }
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
        pos == 1 -> Color(0xFFFFD700)
        pos in 2..4 -> Color(0xFF1E90FF)
        pos == 5 -> Color(0xFFFF6C41)
        pos == 6 -> Color(0xFF4CAF50)
        pos > totalEquipos - 3 -> Color(0xFFFF0000)
        else -> Color.Transparent
    }
}

data class Liga(val name: String, val logoRes: Int, val route: String)
