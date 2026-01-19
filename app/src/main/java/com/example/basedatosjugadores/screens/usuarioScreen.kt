package com.example.basedatosjugadores.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SessionVerificationScreen(navController: NavController) {
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // Verificar si el usuario está autenticado
    if (currentUser == null) {
        // Redirigir a la pantalla de login si no está autenticado
        navController.navigate("login_screen") {
            popUpTo("login_screen") { inclusive = true }
        }
    } else {
        // Mostrar la pantalla si el usuario está autenticado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF272e3f))   // ← Fondo agregado
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Bienvenido, ${currentUser.email}",
                style = MaterialTheme.typography.h5,
                color = Color.White               // Ajusta el color del texto si necesitas contraste
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                // Navegar a la pantalla de menú
                navController.navigate("menu") {
                    popUpTo("session_verification_screen") { inclusive = true }
                }
            }) {
                Text("Ir al Menú")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ✅ Botón de Cerrar Sesión
            Button(
                onClick = {
                    auth.signOut() // Cierra la sesión
                    navController.navigate("login_screen") {
                        popUpTo("login_screen") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary)
            ) {
                Text("Cerrar Sesión", color = MaterialTheme.colors.onSecondary)
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = {
                // Mostrar la opción para cambiar la contraseña
                error = null
            }) {
                Text("¿Olvidaste tu contraseña?", color = Color.White)
            }

            error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colors.error)
            }

            if (error == null) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Ingresa tu correo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        focusedBorderColor = MaterialTheme.colors.primary,
                        unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                        cursorColor = MaterialTheme.colors.primary,
                        textColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (isLoading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Button(onClick = {
                        if (email.isEmpty()) {
                            error = "Por favor, ingresa tu correo."
                            return@Button
                        }

                        isLoading = true
                        auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                if (task.isSuccessful) {
                                    error = "Te hemos enviado un enlace para restablecer tu contraseña."
                                } else {
                                    error = task.exception?.localizedMessage
                                }
                            }
                    }) {
                        Text("Restablecer Contraseña")
                    }
                }
            }
        }
    }
}
