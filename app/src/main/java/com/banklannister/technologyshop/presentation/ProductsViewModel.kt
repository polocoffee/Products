package com.banklannister.technologyshop.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.banklannister.technologyshop.data.State
import com.banklannister.technologyshop.data.model.Product
import com.banklannister.technologyshop.data.repository.ProductsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductsViewModel(
    private val productsRepository: ProductsRepository,
) : ViewModel() {


    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()


    private val _showToastChannel = Channel<Boolean>()
    val showToastChannel = _showToastChannel.receiveAsFlow()

    init {
        getProducts()
    }


    private fun getProducts() {
        viewModelScope.launch {
            productsRepository.getProductsList().collectLatest { result ->
                when (result) {
                    is State.Error -> {
                        _showToastChannel.send(true)
                    }

                    is State.Success -> {
                        result.data?.let { productList ->
                            _products.update { productList }
                        }
                    }
                }
            }
        }
    }
}