package com.example.sumadora_davidsanchez

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.example.sumadora_davidsanchez.ui.theme.Sumadora_DavidSanchezTheme

import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    val listaResultados = mutableStateListOf<String>()
}

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<CalculatorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sumadora_DavidSanchezTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController, startDestination = "@+id/sumar") {
                        // Pantalla 1 (Sumar)
                        composable("@+id/sumar") {
                            Sumar(navController = navController, viewModel.listaResultados)
                        }
                        // Pantalla 2 (Resultado)
                        composable(
                            route = "@+id/resultado/{operacion}",
                            arguments = listOf(
                                navArgument("operacion") { type = NavType.StringType; nullable = false },
                        )
                        ) { backStackEntry ->
                            val operacion = backStackEntry.arguments?.getString("operacion") ?: ""
                            Resultado(navController = navController, operacion, viewModel.listaResultados)
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Sumar(navController: NavController, listaResultados: MutableList<String>) {
    var firstNumber by remember { mutableIntStateOf(0) }
    var secondNumber by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cuadro de texto para el primer número
        TextField(
            value = firstNumber.toString(),
            onValueChange = {
                firstNumber = it.toIntOrNull() ?: 0
            },
            label = { Text(
                text = "Primer número",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            ) },
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp),
                )
        )

        // Cuadro de texto para el segundo número
        TextField(
            value = secondNumber.toString(),
            onValueChange = {
                secondNumber = it.toIntOrNull() ?: 0
            },
            label = { Text(
                text = "Segundo número",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            ) },
            modifier = Modifier
                .padding(16.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(20.dp),
                )
        )

        // Botón de suma
        Button(
            onClick = {
                val operacion = ("$firstNumber + $secondNumber = ${firstNumber + secondNumber}").toString()
                listaResultados.add(operacion)
                navController.navigate("@+id/resultado/$operacion")
            },
        ) {
            Text(
                text = "SUMAR",
                modifier = Modifier
                    .padding(8.dp),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun Resultado(navController: NavController, operacion: String, listaResultados: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(16.dp))
                        // Encabezado con la última operación
                        Text(
                            text = operacion,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Divider(
                        color = Color.Black,
                        thickness = 2.dp
                    )
                    Text(
                        text = "Lista de operaciones realizadas:",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(8.dp),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    // Mostrar todas las operaciones realizadas en listaResultados
                    for (operation in listaResultados) {
                        Text(
                            text = operation,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold

                        )
                    }
                    Button(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .padding(top = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            Image(
                                painter = painterResource(R.drawable.flecha),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "VOLVER",
                                modifier = Modifier
                                    .padding(8.dp),
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}