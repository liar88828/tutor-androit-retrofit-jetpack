package com.example.compose_app

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.runtime.MutableState
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : ComponentActivity() {
    private val fact = mutableStateOf(CatFacts())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeappTheme {
//                https://catfact.ninja/fact
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { GetCatFact() },

                    color = MaterialTheme.colorScheme.background
                ) {


                    GetCatFact()
                    LayoutCat(fact = fact)
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun GetCatFact() {


        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getCatFacts()
            } catch (e: HttpException) {
                return@launch Toast.makeText(
                    applicationContext,
                    "http error : ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: IOException) {
                return@launch Toast.makeText(
                    applicationContext,
                    "app error : ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    fact.value = response.body()!!
                }
            }
        }
    }
}


@Composable
fun LayoutCat(modifier: Modifier = Modifier, fact: MutableState<CatFacts>) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Text(text = "Cat Fact : ", modifier = modifier.padding(bottom = 25.dp), fontSize = 26.sp)
        Text(
            text = fact.value.fact,
            modifier = modifier.padding(bottom = 25.dp),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 40.sp
        )

    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
private fun CatPrev() {

    val fact = mutableStateOf(
        CatFacts(
            length = 1,
            fact = "is cat"
        )
    )

    Surface(

        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {


        LayoutCat(fact = fact)
    }
}