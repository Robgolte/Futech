package com.example.basedatosjugadores.server2.estadisticas2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import com.example.basedatosjugadores.R
import com.example.basedatosjugadores.screens.estadisticas.calcularMediaLiga
import com.example.basedatosjugadores.server2.Liga
import com.example.basedatosjugadores.server2.getLogoResource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun EstadisticasEquipoScreen2(nombreEquipo: String, navController: NavController) {
    val context = LocalContext.current
    var equipoSeleccionado by remember { mutableStateOf<EquipoEstadisticas2?>(null) }
    var mediaLiga by remember { mutableStateOf<EquipoEstadisticas2?>(null) }

    var legendExpanded by remember { mutableStateOf(false) }

    // estados para dropdown de competiciones
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
    // auth de tu viewModel
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    LaunchedEffect(nombreEquipo) {
        withContext(Dispatchers.IO) {
            val equipos = cargarDatosDesdeCSV(context)
            equipoSeleccionado = equipos.firstOrNull { it.Squad.equals(nombreEquipo, ignoreCase = true) }
            mediaLiga = calcularMediaLiga(equipos)
        }
    }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .background(Color(0xFF272e3f)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            equipoSeleccionado?.let { equipo ->
                // Barra superior con nombre y escudo
                TopAppBar(
                    title = {
                        Text(
                            text = "Estadísticas de ${equipo.Squad}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    backgroundColor = Color(0xFF215679),
                    actions = {
                        val logoRes = getLogoResource(equipo.Squad)
                        Image(
                            painter = painterResource(id = logoRes),
                            contentDescription = equipo.Squad,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                mediaLiga?.let { media ->
                    BarChart(
                        equipo = equipo,
                        media = media,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(onClick = { legendExpanded = !legendExpanded }) {
                        Text(text = "Leyenda")
                    }
                    DropdownMenu(
                        expanded = legendExpanded,
                        onDismissRequest = { legendExpanded = false },
                        modifier = Modifier
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        DropdownMenuItem(onClick = { }) {
                            Column {
                                Text("📌 Leyenda", fontWeight = FontWeight.Bold)
                                Text("• SCA: Acciones que conducen a un disparo")
                                Text("• PassLive: Pases vivos exitosos")
                                Text("• PassDead: Pases muertos")
                                Text("• Sh: Disparos totales")
                                Text("• Fld: Faltas recibidas")
                                Text("• Def: Acciones defensivas clave")
                                Text("• GCA: Acciones que conducen a un gol")
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(Color(0xFF606B6D), shape = CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Equipo seleccionado", color = Color(0xFF606B6D), fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(Color(0xFF02B8AB), shape = CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Media de la liga", color = Color(0xFF02B8AB), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        navController.navigate("plantillaLaLiga/${java.net.URLEncoder.encode(equipo.Squad, "UTF-8")}")
                    }
                ) {
                    Text("Ver Plantilla")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.popBackStack() }
                ) {
                    Text("Volver al menú")
                }
            } ?: CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }
}




fun calcularMediaLiga(equipos: List<EquipoEstadisticas2>): EquipoEstadisticas2 {
    val cantidadEquipos = equipos.size.toFloat()
    return EquipoEstadisticas2(
        Squad = "Media Liga",
        sca = (equipos.sumOf { it.sca } / cantidadEquipos).toInt(),
        sca90 = equipos.sumOf { it.sca90 } / cantidadEquipos,
        passLive = (equipos.sumOf { it.passLive } / cantidadEquipos).toInt(),
        passDead = (equipos.sumOf { it.passDead } / cantidadEquipos).toInt(),
        to = (equipos.sumOf { it.to } / cantidadEquipos).toInt(),
        sh = (equipos.sumOf { it.sh } / cantidadEquipos).toInt(),
        fld = (equipos.sumOf { it.fld } / cantidadEquipos).toInt(),
        def = (equipos.sumOf { it.def } / cantidadEquipos).toInt(),
        gca = (equipos.sumOf { it.gca } / cantidadEquipos).toInt(),
        gca90 = equipos.sumOf { it.gca90 } / cantidadEquipos
    )
}

@Composable
fun BarChart(equipo: EquipoEstadisticas2, media: EquipoEstadisticas2, modifier: Modifier) {
    val datos = listOf(
        Triple("SCA", equipo.sca.toFloat(), media.sca.toFloat()),
        Triple("PassLive", equipo.passLive.toFloat(), media.passLive.toFloat()),
        Triple("PassDead", equipo.passDead.toFloat(), media.passDead.toFloat()),
        Triple("Sh", equipo.sh.toFloat(), media.sh.toFloat()),
        Triple("Fld", equipo.fld.toFloat(), media.fld.toFloat()),
        Triple("Def", equipo.def.toFloat(), media.def.toFloat()),
        Triple("GCA", equipo.gca.toFloat(), media.gca.toFloat())
    )

    val maxValor = datos.maxOf { maxOf(it.second, it.third) }

    Column(
        modifier = modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        datos.forEach { (nombre, valorEquipo, valorMedia) ->
            Text(text = nombre, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Barra del equipo (color cambiado)
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width((valorEquipo / maxValor) * 250.dp)
                        .background(
                            brush = Brush.horizontalGradient(listOf(Color(0xFF4F9CBB), Color(0xFF396C7F))),
                            shape = RoundedCornerShape(6.dp)
                        )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = valorEquipo.toInt().toString(),
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFF396C7F), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Barra de la media (color cambiado)
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .width((valorMedia / maxValor) * 250.dp)
                        .background(
                            brush = Brush.horizontalGradient(listOf(Color(0xFF01A89C), Color(0xFF00D7A7))),
                            shape = RoundedCornerShape(6.dp)
                        )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = valorMedia.toInt().toString(),
                    fontSize = 12.sp,
                    color = Color.White,
                    modifier = Modifier
                        .background(Color(0xFF00D7A7), RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
