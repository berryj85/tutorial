package berryj.tutorial.springboot.graphql.data.fetcher

import berryj.tutorial.springboot.graphql.entity.Order
import berryj.tutorial.springboot.graphql.service.OrderService
import berryj.tutorial.springboot.graphql.service.ProductService
import graphql.schema.DataFetcher
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrderDataFetcher {
    companion object {
        private val log = LogManager.getLogger(OrderDataFetcher::class.java)
    }

    @Autowired
    private lateinit var orderService: OrderService
    @Autowired
    private lateinit var productService: ProductService

    fun getOrder() = DataFetcher {
        val orderId = it.arguments["orderId"] as String
        orderService.getOrder(orderId)
    }

    fun getTotalPrice() = DataFetcher {
        val order = it.getSource<Order>()
        if (!order.items.isNullOrEmpty()) {
            order.items.sumByDouble { productService.getProductById(it.productId).productPrice * it.amount }
        } else {
            0
        }
    }

    fun getTotalAmount() = DataFetcher {
        val order = it.getSource<Order>()
        if (!order.items.isNullOrEmpty()) {
            order.items.sumBy { it.amount }
        } else {
            0
        }

    }

    fun getOrderByCustomerId() = DataFetcher {
        val customerId = it.arguments["customerId"] as String
        orderService.getOrderByCustomerId(customerId)
    }

}