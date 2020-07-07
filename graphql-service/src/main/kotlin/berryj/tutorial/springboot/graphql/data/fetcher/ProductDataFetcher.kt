package berryj.tutorial.springboot.graphql.data.fetcher

import berryj.tutorial.springboot.graphql.entity.OrderItem
import berryj.tutorial.springboot.graphql.service.ProductService
import graphql.schema.DataFetcher
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProductDataFetcher {
    companion object {
        private val log = LogManager.getLogger(ProductDataFetcher::class.java)
    }

    @Autowired
    private lateinit var productService: ProductService

    fun getProduct() = DataFetcher {
        val orderItem = it.getSource<OrderItem>()
        val productId = orderItem?.productId ?: it.arguments["productId"] as String?
        if (!productId.isNullOrBlank()) {
            productService.getProductById(productId)
        } else {
            productService.getAllProduct()
        }
    }
}