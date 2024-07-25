package com.example.compose_app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose_app.models.CatFacts
import com.example.compose_app.ui.theme.ComposeappTheme
import com.example.compose_app.utils.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : ComponentActivity() {
    //    var fact = { mutableStateOf(CatFacts()) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeappTheme {
//                https://catfact.ninja/fact
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable
                        {
//                            sendRequest()
                        },

                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    var fact by remember { mutableStateOf(CatFacts()) }
                    val scope = rememberCoroutineScope()

                    LaunchedEffect(key1 = true) {
                        scope.launch(Dispatchers.IO) {
                            val response = try {
                                RetrofitInstance.api.getCatFacts()
                            } catch (e: HttpException) {
                                return@launch Toast.makeText(
                                    context,
                                    "http error : ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()

                            } catch (e: IOException) {
                                return@launch Toast.makeText(
                                    context,
                                    "app error : ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            if (response.isSuccessful && response.body() != null) {
                                withContext(Dispatchers.Main) {
                                    fact = response.body()!!
                                }
                            }
                        }
                    }
                    LayoutCat(fact = fact)
                }
            }
        }
    }
}


@Composable
fun LayoutCat(modifier: Modifier = Modifier, fact: CatFacts) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(text = "Cat Fact : ", modifier = modifier.padding(bottom = 25.dp), fontSize = 26.sp)
        Text(text = fact.fact, fontSize = 26.sp, fontWeight = FontWeight.Bold, lineHeight = 40.sp)

    }
}

@Preview(showBackground = true)
@Composable
private fun CatPrev() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LayoutCat(
            fact = CatFacts(
                fact = "is just test",
                length = 2
            )
        )
    }
}