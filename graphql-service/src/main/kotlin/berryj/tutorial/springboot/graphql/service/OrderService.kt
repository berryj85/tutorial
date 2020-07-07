package berryj.tutorial.springboot.graphql.service

import berryj.tutorial.springboot.graphql.entity.Order
import berryj.tutorial.springboot.graphql.entity.OrderItem
import org.springframework.stereotype.Service

@Service
class OrderService {
    companion object {

        private val orders = listOf(
                Order("1", "1", listOf(OrderItem("1", 10), OrderItem("2", 20))),
                Order("2", "1", listOf(OrderItem("2", 10), OrderItem("3", 20))),
                Order("3", "2", listOf(OrderItem("1", 10), OrderItem("3", 20))),
                Order("4", "3", listOf(OrderItem("2", 10)))
        )
    }

    fun getOrder(id: String): Order {
        return orders.find { it.orderId == id } ?: throw Exception("Order not found.")
    }

    fun getOrderByCustomerId(customerId: String): List<Order> {
        return orders.filter { it.customerId == customerId }
    }
}