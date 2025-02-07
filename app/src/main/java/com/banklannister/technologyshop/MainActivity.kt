package com.banklannister.technologyshop

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.banklannister.technologyshop.data.model.Product
import com.banklannister.technologyshop.data.network.RetrofitInstance
import com.banklannister.technologyshop.data.repository.ProductsRepositoryImpl
import com.banklannister.technologyshop.presentation.ProductsViewModel
import com.banklannister.technologyshop.ui.theme.TechnologyShopTheme
import kotlinx.coroutines.flow.collectLatest

@Suppress("UNCHECKED_CAST")
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ProductsViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProductsViewModel(ProductsRepositoryImpl(RetrofitInstance.api))
                        as T
            }
        }
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TechnologyShopTheme {


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val productList = viewModel.products.collectAsState().value
                    val context = LocalContext.current


                    LaunchedEffect(
                        key1 = viewModel.showToastChannel
                    ) {
                        viewModel.showToastChannel.collectLatest { show ->
                            if (show) {
                                Toast.makeText(
                                    context,
                                    "Error",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }

                    if (productList.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(productList.size) { index ->
                                GetProducts(productList[index])

                                Spacer(modifier = Modifier.height(16.dp))


                            }
                        }
                    }

                }
            }
        }
    }
}


@Composable
fun GetProducts(product: Product) {

    val context = LocalContext.current

    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(product.thumbnail)
            .size(Size.ORIGINAL)
            .build()
    ).state


    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .height(380.dp)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (imageState is AsyncImagePainter.State.Error) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (imageState is AsyncImagePainter.State.Success) {
            Image(
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp),
                painter = imageState.painter,
                contentDescription = product.title,
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = product.title,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "${product.price}$",
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = product.description,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal
        )
    }


}
