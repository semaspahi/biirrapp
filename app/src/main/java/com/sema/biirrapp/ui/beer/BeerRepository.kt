package com.sema.biirrapp.ui.beer

import com.sema.base.data.api.ApiService
import com.sema.base.data.model.Beer
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import com.sema.base.common.BaseApiResponse
import com.sema.base.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import javax.inject.Inject

@ActivityRetainedScoped
class BeerRepository @Inject constructor(
    private val apiService: ApiService,
    private val defaultDispatcher: CoroutineDispatcher
) : BaseApiResponse() {

    suspend fun getBeers(page: Int? = null): Flow<Resource<List<Beer>>> {
        return flow {
            val result = safeApiCall { apiService.getBeers(page) }
            emit(result)
        }.flowOn(defaultDispatcher)
    }

}