package com.banklannister.technologyshop.data.repository

import com.banklannister.technologyshop.data.State
import com.banklannister.technologyshop.data.model.Product
import com.banklannister.technologyshop.data.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException

class ProductsRepositoryImpl(
    private val api: ApiService,
) : ProductsRepository {

    override suspend fun getProductsList(): Flow<State<List<Product>>> {
        return flow {
            val productFromApi = try {
                api.getProductList()

            } catch (e: IOException) {
                e.printStackTrace()
                emit(State.Error(message = "Error loading"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(State.Error(message = "Error loading"))
                return@flow
            }


            emit(State.Success(productFromApi.products))
        }


    }
}