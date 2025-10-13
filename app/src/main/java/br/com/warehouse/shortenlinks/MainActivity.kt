package br.com.warehouse.shortenlinks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.warehouse.shortenlinks.features.shorten.presentation.ShortenScreen
import br.com.warehouse.shortenlinks.ui.theme.ShortenLinksTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShortenLinksTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ShortenScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}