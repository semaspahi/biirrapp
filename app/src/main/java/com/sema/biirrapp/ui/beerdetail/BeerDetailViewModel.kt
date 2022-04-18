package com.sema.biirrapp.ui.beerdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sema.base.common.Resource
import com.sema.base.data.model.Beer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeerDetailViewModel @Inject constructor(
    private val repository: BeerDetailRepository
) : ViewModel() {

    private val _beerDetailFlow = Channel<Resource<List<Beer>>>(Channel.BUFFERED)
    val beerDetailFlow = _beerDetailFlow.receiveAsFlow()

    fun getBeer(id: Int) {
        viewModelScope.launch {
            repository.getBeer(id)
                .catch { _beerDetailFlow.send(Resource.error(it)) }
                .collect { _beerDetailFlow.send(it) }
        }
    }

}