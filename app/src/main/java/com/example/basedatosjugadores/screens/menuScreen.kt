package com.example.basedatosjugadores.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import com.example.basedatosjugadores.server2.TablaViewModel2    // La Liga
import com.example.basedatosjugadores.server2.ResultState2
import com.example.basedatosjugadores.server3.TablaViewModel3    // Serie A
import com.example.basedatosjugadores.server3.ResultState3
import com.example.basedatosjugadores.server4.TablaViewModel4    // Bundesliga
import com.example.basedatosjugadores.server4.ResultState4
import com.example.basedatosjugadores.server5.TablaViewModel5    // Ligue 1
import com.example.basedatosjugadores.server5.ResultState5
import com.example.basedatosjugadores.server6.TablaViewModel6    // Champions League
import com.example.basedatosjugadores.server6.ResultState6
import com.example.basedatosjugadores.server7.TablaViewModel7    // Europa League
import com.example.basedatosjugadores.server7.ResultState7
import com.google.firebase.auth.FirebaseAuth

@Composable
fun MenuScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    var competicionesExpanded by remember { mutableStateOf(false) }

    // Lista de ligas para slider de selección de top 3
    val ligas = listOf(
        Liga("Liga Española", R.drawable.logo_la_liga, "tabla2"),
        Liga("Premier League", R.drawable.logo_premier, "tabla1"),
        Liga("Serie A", R.drawable.logo_serie_a, "tabla3"),
        Liga("Bundesliga", R.drawable.logo_bundesliga, "tabla4"),
        Liga("Ligue 1", R.drawable.logo_ligue1, "tabla5"),
        Liga("Champions League", R.drawable.logo_champions, "tabla6"),
        Liga("Europa League", R.drawable.logo_europa, "tabla7")
    )
    var selectedLeagueIndex by remember { mutableStateOf(0) }

    // Instanciar cada ViewModel según liga
    val context = LocalContext.current
    val vmEspanola   = remember { TablaViewModel2(context) }  // liga española (tabla2)
    val vmPremier    = remember { TablaViewModel(context) }   // premier (tabla1)
    val vmSerieA     = remember { TablaViewModel3(context) }
    val vmBundesliga = remember { TablaViewModel4(context) }
    val vmLigue1     = remember { TablaViewModel5(context) }
    val vmChampions  = remember { TablaViewModel6(context) }
    val vmEuropa     = remember { TablaViewModel7(context) }

    // Estados de cada tabla
    val stateEspanola   by vmEspanola.tabla.collectAsState()
    val statePremier    by vmPremier.tabla.collectAsState()
    val stateSerieA     by vmSerieA.tabla.collectAsState()
    val stateBundesliga by vmBundesliga.tabla.collectAsState()
    val stateLigue1     by vmLigue1.tabla.collectAsState()
    val stateChampions  by vmChampions.tabla.collectAsState()
    val stateEuropa     by vmEuropa.tabla.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.futech_logo),
                        contentDescription = "Logo Futech",
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "Futech",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                backgroundColor = Color(0xFF215679),
                actions = {
                    if (currentUser != null) {
                        IconButton(onClick = { navController.navigate("favoritos_screen") }) {
                            Icon(Icons.Filled.Star, contentDescription = "Favoritos", tint = Color.Yellow)
                        }
                    }
                    IconButton(onClick = {
                        if (currentUser != null) navController.navigate("session_verification")
                        else navController.navigate("login_screen")
                    }) {
                        Icon(Icons.Filled.Person, contentDescription = "Usuario", tint = Color.White)
                    }
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
                    IconButton(onClick = { navController.navigate("menu") }) {
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
                                    if (auth.currentUser != null) navController.navigate(liga.route)
                                    else navController.navigate("session_verification")
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
        Column(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido a Futech",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2de0cb),
                modifier = Modifier.padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )

            // Primer slider: selección rápida de liga (logos)
            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                items(ligas.size) { index ->
                    val liga = ligas[index]
                    Column(
                        Modifier
                            .padding(horizontal = 12.dp)
                            .clickable {
                                if (auth.currentUser != null) navController.navigate(liga.route)
                                else navController.navigate("session_verification")
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF215679))
                                .border(3.dp, Color(0xFF2de0cb), CircleShape)
                                .shadow(4.dp, CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = liga.logoRes),
                                contentDescription = liga.name,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = liga.name,
                            fontSize = 12.sp,
                            color = Color(0xFFc0e1ec),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // ---------------------------------------------------------------
            // BLOQUE: TOP 3 POR LIGA CON SLIDER PARA SELECCIÓN DE LIGA
            // ---------------------------------------------------------------

            // Slider para seleccionar liga cuyo top 3 mostrar
            LazyRow(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                itemsIndexed(ligas) { index, liga ->
                    Text(
                        text = liga.name,
                        color = if (index == selectedLeagueIndex) Color(0xFF2de0cb) else Color(0xFFc0e1ec),
                        fontWeight = if (index == selectedLeagueIndex) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .clickable { selectedLeagueIndex = index }
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Mostrar top 3 de la liga seleccionada
            when (selectedLeagueIndex) {
                0 -> {
                    // Liga Española
                    when (val state = stateEspanola) {
                        is ResultState2.Loading -> {
                            CircularProgressIndicator(color = Color.White)
                        }
                        is ResultState2.Success -> {
                            val equipos = state.data.take(3)
                            equipos.forEach { eq ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${eq.RL}.",
                                        color = Color(0xFF2de0cb),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.width(24.dp)
                                    )
                                    Image(
                                        painter = painterResource(id = getLogoResource(eq.Equipo)),
                                        contentDescription = "${eq.Equipo} Logo",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text(
                                        eq.Equipo,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        "${eq.Pts} pts",
                                        color = Color(0xFFc0e1ec),
                                        modifier = Modifier.width(72.dp),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                        is ResultState2.Error -> {
                            Text("Error al cargar Liga Española", color = Color.Red)
                        }
                    }
                }
                1 -> {
                    // Premier League
                    when (val state = statePremier) {
                        is ResultState.Loading -> {
                            CircularProgressIndicator(color = Color.White)
                        }
                        is ResultState.Success -> {
                            val equipos = state.data.take(3)
                            equipos.forEach { eq ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${eq.Rk}.",
                                        color = Color(0xFF2de0cb),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.width(24.dp)
                                    )
                                    Image(
                                        painter = painterResource(id = getLogoResource(eq.Squad)),
                                        contentDescription = "${eq.Squad} Logo",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text(
                                        eq.Squad,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        "${eq.Pts} pts",
                                        color = Color(0xFFc0e1ec),
                                        modifier = Modifier.width(72.dp),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                        is ResultState.Error -> {
                            Text("Error al cargar Premier League", color = Color.Red)
                        }
                    }
                }
                2 -> {
                    // Serie A
                    when (val state = stateSerieA) {
                        is ResultState3.Loading -> {
                            CircularProgressIndicator(color = Color.White)
                        }
                        is ResultState3.Success -> {
                            val equipos = state.data.take(3)
                            equipos.forEach { eq ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${eq.RL}.",
                                        color = Color(0xFF2de0cb),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.width(24.dp)
                                    )
                                    Image(
                                        painter = painterResource(id = getLogoResource(eq.Equipo)),
                                        contentDescription = "${eq.Equipo} Logo",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text(
                                        eq.Equipo,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        "${eq.Pts} pts",
                                        color = Color(0xFFc0e1ec),
                                        modifier = Modifier.width(72.dp),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                        is ResultState3.Error -> {
                            Text("Error al cargar Serie A", color = Color.Red)
                        }
                    }
                }
                3 -> {
                    // Bundesliga
                    when (val state = stateBundesliga) {
                        is ResultState4.Loading -> {
                            CircularProgressIndicator(color = Color.White)
                        }
                        is ResultState4.Success -> {
                            val equipos = state.data.take(3)
                            equipos.forEach { eq ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${eq.RL}.",
                                        color = Color(0xFF2de0cb),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.width(24.dp)
                                    )
                                    Image(
                                        painter = painterResource(id = getLogoResource(eq.Equipo)),
                                        contentDescription = "${eq.Equipo} Logo",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text(
                                        eq.Equipo,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        "${eq.Pts} pts",
                                        color = Color(0xFFc0e1ec),
                                        modifier = Modifier.width(72.dp),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                        is ResultState4.Error -> {
                            Text("Error al cargar Bundesliga", color = Color.Red)
                        }
                    }
                }
                4 -> {
                    // Ligue 1
                    when (val state = stateLigue1) {
                        is ResultState5.Loading -> {
                            CircularProgressIndicator(color = Color.White)
                        }
                        is ResultState5.Success -> {
                            val equipos = state.data.take(3)
                            equipos.forEach { eq ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${eq.RL}.",
                                        color = Color(0xFF2de0cb),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.width(24.dp)
                                    )
                                    Image(
                                        painter = painterResource(id = getLogoResource(eq.Equipo)),
                                        contentDescription = "${eq.Equipo} Logo",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text(
                                        eq.Equipo,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        "${eq.Pts} pts",
                                        color = Color(0xFFc0e1ec),
                                        modifier = Modifier.width(72.dp),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                        is ResultState5.Error -> {
                            Text("Error al cargar Ligue 1", color = Color.Red)
                        }
                    }
                }
                5 -> {
                    // Champions League
                    when (val state = stateChampions) {
                        is ResultState6.Loading -> {
                            CircularProgressIndicator(color = Color.White)
                        }
                        is ResultState6.Success -> {
                            val equipos = state.data.take(3)
                            equipos.forEach { eq ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${eq.RL}.",
                                        color = Color(0xFF2de0cb),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.width(24.dp)
                                    )
                                    val teamName = eq.Equipo.substringAfter(" ")
                                    Image(
                                        painter = painterResource(id = getLogoResource(teamName)),
                                        contentDescription = "$teamName Logo",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text(
                                        teamName,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        "${eq.Pts} pts",
                                        color = Color(0xFFc0e1ec),
                                        modifier = Modifier.width(72.dp),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                        is ResultState6.Error -> {
                            Text("Error al cargar Champions League", color = Color.Red)
                        }
                    }
                }
                6 -> {
                    // Europa League
                    when (val state = stateEuropa) {
                        is ResultState7.Loading -> {
                            CircularProgressIndicator(color = Color.White)
                        }
                        is ResultState7.Success -> {
                            val equipos = state.data.take(3)
                            equipos.forEach { eq ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "${eq.RL}.",
                                        color = Color(0xFF2de0cb),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.width(24.dp)
                                    )
                                    val teamName = eq.Equipo.substringAfter(" ")
                                    Image(
                                        painter = painterResource(id = getLogoResource(teamName)),
                                        contentDescription = "$teamName Logo",
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(end = 8.dp)
                                    )
                                    Text(
                                        teamName,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        "${eq.Pts} pts",
                                        color = Color(0xFFc0e1ec),
                                        modifier = Modifier.width(72.dp),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                        }
                        is ResultState7.Error -> {
                            Text("Error al cargar Europa League", color = Color.Red)
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Explora las mejores ligas de fútbol 🌍",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFc0e1ec),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}


