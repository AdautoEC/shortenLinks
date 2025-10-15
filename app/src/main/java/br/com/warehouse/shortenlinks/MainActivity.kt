package br.com.warehouse.shortenlinks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import br.com.warehouse.shortenlinks.features.shorten.presentation.ShortenScreen
import br.com.warehouse.shortenlinks.ui.theme.ShortenLinksTheme
import br.com.warehouse.shortenlinks.ui.theme.spacing
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShortenLinksTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    ShortenScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(MaterialTheme.spacing.medium),
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}