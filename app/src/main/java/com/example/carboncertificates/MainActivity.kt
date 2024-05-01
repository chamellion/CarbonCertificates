package com.example.carboncertificates

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carboncertificates.lib.models.Certificate
import com.example.carboncertificates.ui.theme.CarbonCertificatesTheme
import com.example.carboncertificates.ui.viewmodel.CertificatesViewModel
import com.example.carboncertificates.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val certificatesViewModel: CertificatesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CarbonCertificatesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewState = certificatesViewModel.viewState.collectAsState()
                    when (viewState.value) {
                        is NetworkResult.Loading -> {
                            LoadingSpinner()
                        }

                        is NetworkResult.Error -> {
                            ErrorBox(
                                errorMessage = (viewState.value as NetworkResult.Error).error
                                    ?: "One or more error has occurred"
                            )
                        }

                        is NetworkResult.Success<*> -> {
                            val data = (viewState.value as NetworkResult.Success<*>).data as List<Certificate>
                            Log.d(TAG, "onCreate: Data is $data")
                            DisplayCertificate(certificateList = data)
                        }

                        NetworkResult.NoInternet -> {
                            ErrorBox(errorMessage = "No Internet Connection")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayCertificate(certificateList: List<Certificate>) {
    LazyColumn {
        items(certificateList) {
            Certificate(id = it.id, originator = it.originator, owner = it.owner)
        }
    }
}

@Composable
fun ErrorBox(errorMessage: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = errorMessage,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}

@Composable
fun Certificate(id: String, originator: String, owner: String) {
    var isFavourite by rememberSaveable {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                isFavourite = !isFavourite
            }
    ) {
        Row {
            Text(
                text = "ID: ",
                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = id,
                modifier = Modifier.padding(top = 8.dp, start = 2.dp),
                fontWeight = FontWeight.ExtraBold,
                overflow = TextOverflow.Ellipsis
            )
        }
        Icon(
            imageVector = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favourite icon",
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp)
        )
        Row {
            Text(text = "Originator: ", modifier = Modifier.padding(top = 4.dp, start = 8.dp))
            Text(
                text = originator,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp),
                fontWeight = FontWeight.ExtraBold
            )
        }
        Row(modifier = Modifier.padding(bottom = 12.dp)) {
            Text(text = "Owner: ", modifier = Modifier.padding(top = 4.dp, start = 8.dp))
            Text(
                text = owner,
                modifier = Modifier.padding(top = 4.dp, start = 8.dp),
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun LoadingSpinner() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(60.dp),
            color = Color.White,
            trackColor = Color.Black,
            strokeWidth = 3.dp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    CarbonCertificatesTheme {
//        Certificate("3876", "Webpoint Jakub Kpupski PL", "Webpoint Jakub Kpupski ES")
        //LoadingSpinner()
        ErrorBox(errorMessage = "One or more error")
    }
}