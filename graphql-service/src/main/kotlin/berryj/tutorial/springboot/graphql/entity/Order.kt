package berryj.tutorial.springboot.graphql.entity

data class Order(val orderId: String, val customerId: String, val items: List<OrderItem>)
data class OrderItem(val productId: String, val amount: Int)