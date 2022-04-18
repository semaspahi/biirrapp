package com.sema.biirrapp.ui.beer

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
class BeerViewModel @Inject constructor(
    private val repository: BeerRepository
) : ViewModel() {

    private val _beerListFlow = Channel<Resource<List<Beer>>>(Channel.BUFFERED)
    val beerListFlow = _beerListFlow.receiveAsFlow()
    var page = 1

    fun getBeers() {
        resetPageId()
        viewModelScope.launch {
            repository.getBeers()
                .catch { _beerListFlow.send(Resource.error(it)) }
                .collect {
                    _beerListFlow.send(it)
                    page +=1
                }
        }
    }

    fun getNextBeers(nextPage: Int? = null) {
        viewModelScope.launch {
            repository.getBeers(nextPage)
                .catch { _beerListFlow.send(Resource.error(it)) }
                .collect {
                    _beerListFlow.send(it)
                    page +=1
                }
        }
    }

    private fun resetPageId(){
        page = 1
    }

}