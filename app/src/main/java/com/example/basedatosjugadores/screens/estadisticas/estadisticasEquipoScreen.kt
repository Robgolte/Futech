package com.example.basedatosjugadores.screens.estadisticas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun EstadisticasEquipoScreen(nombreEquipo: String, navController: NavController) {
    val context = LocalContext.current
    var equipoSeleccionado by remember { mutableStateOf<EquipoEstadisticas?>(null) }
    var mediaLiga by remember { mutableStateOf<EquipoEstadisticas?>(null) }

    LaunchedEffect(nombreEquipo) {
        withContext(Dispatchers.IO) {
            val equipos = cargarDatosDesdeCSV(context)
            equipoSeleccionado = equipos.firstOrNull { it.Squad.equals(nombreEquipo, ignoreCase = true) }
            mediaLiga = calcularMediaLiga(equipos)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        equipoSeleccionado?.let { equipo ->
            Text(
                text = "Estadísticas de ${equipo.Squad}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            mediaLiga?.let { media ->
                BarChart(
                    equipo = equipo,
                    media = media,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Legend()

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para ver la plantilla del equipo
            Button(
                onClick = {
                    // Navega a la ruta "plantilla/<nombreEquipo>" (asegúrate de codificar el nombre si es necesario)
                    navController.navigate("plantillaPremier/${java.net.URLEncoder.encode(equipo.Squad, "UTF-8")}")
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Ver Plantilla")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Volver al menú")
            }
        } ?: CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

fun calcularMediaLiga(equipos: List<EquipoEstadisticas>): EquipoEstadisticas? {
    val cantidadEquipos = equipos.size.toFloat()

    return EquipoEstadisticas(
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
fun BarChart(equipo: EquipoEstadisticas, media: EquipoEstadisticas, modifier: Modifier) {
    val datos = listOf(
        Triple("SCA", equipo.sca.toFloat(), media.sca.toFloat()),
        Triple("PassLive", equipo.passLive.toFloat(), media.passLive.toFloat()),
        Triple("PassDead", equipo.passDead.toFloat(), media.passDead.toFloat()),
        Triple("Sh", equipo.sh.toFloat(), media.sh.toFloat()),
        Triple("Fld", equipo.fld.toFloat(), media.fld.toFloat()),
        Triple("Def", equipo.def.toFloat(), media.def.toFloat()),
        Triple("GCA", equipo.gca.toFloat(), media.gca.toFloat())
    )

    // Encontramos el valor máximo entre todas las barras (tanto del equipo como de la media)
    val maxValor = datos.maxOf { maxOf(it.second, it.third) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(400.dp) // Tamaño fijo para activar scroll si hay muchos datos
            .verticalScroll(rememberScrollState())
            .padding(8.dp)
    ) {
        datos.forEach { (nombre, valorEquipo, valorMedia) ->

            // Título de cada estadística
            Text(text = nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp)

            Column(modifier = Modifier.fillMaxWidth()) {

                // Barra del equipo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(20.dp) // Altura fija para las barras
                            .width((valorEquipo / maxValor) * 250.dp) // El ancho es proporcional al valor
                            .background(Color(0xFF606B6D)) // Gris oscuro
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // Valor del equipo
                    Text(
                        text = valorEquipo.toInt().toString(),
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color(0xFF606B6D))
                            .padding(4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Barra de la media
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .height(20.dp) // Altura fija para las barras
                            .width((valorMedia / maxValor) * 250.dp) // El ancho es proporcional al valor
                            .background(Color(0xFF02B8AB)) // Verde azulado
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    // Valor de la media
                    Text(
                        text = valorMedia.toInt().toString(),
                        fontSize = 12.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color(0xFF02B8AB))
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}




@Composable
fun Legend() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text("📌 Leyenda:", fontWeight = FontWeight.Bold)

        // Leyenda de las estadísticas
        Text("• SCA: Acciones que conducen a un disparo")
        Text("• PassLive: Pases vivos exitosos")
        Text("• PassDead: Pases muertos")
        Text("• Sh: Disparos totales")
        Text("• Fld: Faltas recibidas")
        Text("• Def: Acciones defensivas clave")
        Text("• GCA: Acciones que conducen a un gol")

        Spacer(modifier = Modifier.height(8.dp))

        // Leyenda con círculos de color
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Círculo de color para el equipo seleccionado
            Box(
                modifier = Modifier
                    .size(16.dp) // Tamaño del círculo
                    .background(Color(0xFF606B6D), shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp)) // Espacio entre el círculo y el texto
            Text("Equipo seleccionado", color = Color(0xFF606B6D), fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Círculo de color para la media de la liga
            Box(
                modifier = Modifier
                    .size(16.dp) // Tamaño del círculo
                    .background(Color(0xFF02B8AB), shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(4.dp)) // Espacio entre el círculo y el texto
            Text("Media de la liga", color = Color(0xFF02B8AB),fontWeight = FontWeight.Bold)
        }
    }
}