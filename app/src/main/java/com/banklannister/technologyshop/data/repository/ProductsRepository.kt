package com.banklannister.technologyshop.data.repository

import com.banklannister.technologyshop.data.State
import com.banklannister.technologyshop.data.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {


    suspend fun getProductsList(): Flow<State<List<Product>>>


}