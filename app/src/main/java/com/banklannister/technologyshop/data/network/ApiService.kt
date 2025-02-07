package com.banklannister.technologyshop.data.network

import com.banklannister.technologyshop.data.model.Products
import com.banklannister.technologyshop.util.PATH_URL
import retrofit2.http.GET

interface ApiService {

    @GET(PATH_URL)
    suspend fun getProductList(): Products
}