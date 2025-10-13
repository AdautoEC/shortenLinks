package br.com.warehouse.shortenlinks.features.shorten.presentation

import androidx.lifecycle.ViewModel
import br.com.warehouse.shortenlinks.core.presentation.model.submittable
import br.com.warehouse.shortenlinks.features.shorten.domain.usecase.ShortenUrlUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ShortenViewModel @Inject constructor(
    private val shortenUrlUseCase: ShortenUrlUseCase
) : ViewModel() {

    val url = MutableStateFlow("")

    val shortenUrlState = submittable(url) {
        shortenUrlUseCase(it)
    }
}
