package br.com.warehouse.shortenlinks.features.shorten.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

/**
 * A composable that displays an error dialog.
 *
 * @param title The title of the dialog.
 * @param message The message to be displayed in the dialog.
 * @param onDismiss A callback that is invoked when the dialog is dismissed.
 */
@Composable
fun ErrorDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "OK")
            }
        }
    )
}
