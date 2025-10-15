package br.com.warehouse.shortenlinks.previews

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import br.com.warehouse.shortenlinks.core.validation.UrlValidator
import br.com.warehouse.shortenlinks.features.shorten.domain.model.OriginalUrl
import br.com.warehouse.shortenlinks.features.shorten.domain.model.Shorten
import br.com.warehouse.shortenlinks.features.shorten.domain.repository.ShortenRepository
import br.com.warehouse.shortenlinks.features.shorten.domain.usecase.GetOriginalUrlUseCase
import br.com.warehouse.shortenlinks.features.shorten.domain.usecase.ShortenUrlUseCase
import br.com.warehouse.shortenlinks.features.shorten.presentation.ShortenScreen
import br.com.warehouse.shortenlinks.features.shorten.presentation.ShortenViewModel
import br.com.warehouse.shortenlinks.features.shorten.presentation.components.ErrorDialog
import br.com.warehouse.shortenlinks.features.shorten.presentation.components.LinkHistoryList
import br.com.warehouse.shortenlinks.ui.theme.ShortenLinksTheme

/**
 * A fake implementation of the [ShortenRepository] for preview purposes.
 */
private class FakeShortenRepository : ShortenRepository {
    override suspend fun shortenUrl(url: String): Shorten {
        if (url.isNotBlank() && url != "error") {
            return Shorten(
                alias = url,
                links = br.com.warehouse.shortenlinks.features.shorten.domain.model.Links(self = url, shortUrl = "http://short.url/$url")
            )
        } else {
            throw Exception("Invalid URL")
        }
    }

    override suspend fun getOriginalUrl(alias: String): OriginalUrl {
        return OriginalUrl("https://original.url")
    }
}

/**
 * A fake implementation of the [UrlValidator] for preview purposes.
 */
private class FakeUrlValidator : UrlValidator {
    override fun isValid(url: String): Boolean {
        return url.isNotBlank()
    }
}

private fun getFakeGetOriginalUrlUseCase() = GetOriginalUrlUseCase(FakeShortenRepository())

/**
 * A preview of the [ErrorDialog] in dark mode.
 */
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "ErrorDialog Dark Mode")
@Composable
fun ErrorDialogDarkPreview() {
    ShortenLinksTheme {
        ErrorDialog(title = "Error", message = "This is an error message.", onDismiss = {})
    }
}

/**
 * A preview of the [LinkHistoryList] in dark mode.
 */
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "LinkHistoryList Dark Mode")
@Composable
fun LinkHistoryListDarkPreview() {
    ShortenLinksTheme {
        LinkHistoryList(
            history = listOf(
                Shorten("alias1", br.com.warehouse.shortenlinks.features.shorten.domain.model.Links("self1", "short1")),
                Shorten("alias2", br.com.warehouse.shortenlinks.features.shorten.domain.model.Links("self2", "short2"))
            ),
            isLoading = false
        )
    }
}

/**
 * A preview of the [LinkHistoryList] in a loading state in dark mode.
 */
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "LinkHistoryList Loading Dark Mode")
@Composable
fun LinkHistoryListLoadingDarkPreview() {
    ShortenLinksTheme {
        LinkHistoryList(history = emptyList(), isLoading = true)
    }
}

/**
 * A preview of the [ShortenScreen] on a small phone.
 */
@SuppressLint("VisibleForTests", "ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "ShortenScreen - Small Phone", device = "spec:width=320dp,height=480dp,dpi=160")
@Composable
fun ShortenScreenSmallPhonePreview() {
    ShortenLinksTheme {
        val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }
        ShortenScreen(
            viewModel = ShortenViewModel(
                shortenUrlUseCase = ShortenUrlUseCase(FakeShortenRepository()),
                getOriginalUrlUseCase = getFakeGetOriginalUrlUseCase(),
                urlValidator = FakeUrlValidator()
            ),
            snackbarHostState = snackbarHostState
        )
    }
}

/**
 * A preview of the [ShortenScreen] on a large phone.
 */
@SuppressLint("VisibleForTests", "ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "ShortenScreen - Large Phone", device = "spec:width=411dp,height=891dp,dpi=420")
@Composable
fun ShortenScreenLargePhonePreview() {
    ShortenLinksTheme {
        val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }
        ShortenScreen(
            viewModel = ShortenViewModel(
                shortenUrlUseCase = ShortenUrlUseCase(FakeShortenRepository()),
                getOriginalUrlUseCase = getFakeGetOriginalUrlUseCase(),
                urlValidator = FakeUrlValidator()
            ),
            snackbarHostState = snackbarHostState
        )
    }
}

/**
 * A preview of the [ShortenScreen] on a tablet.
 */
@SuppressLint("VisibleForTests", "ViewModelConstructorInComposable")
@Preview(showBackground = true, name = "ShortenScreen - Tablet", device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
fun ShortenScreenTabletPreview() {
    ShortenLinksTheme {
        val snackbarHostState = remember { androidx.compose.material3.SnackbarHostState() }
        ShortenScreen(
            viewModel = ShortenViewModel(
                shortenUrlUseCase = ShortenUrlUseCase(FakeShortenRepository()),
                getOriginalUrlUseCase = getFakeGetOriginalUrlUseCase(),
                urlValidator = FakeUrlValidator()
            ),
            snackbarHostState = snackbarHostState
        )
    }
}