package berryj.tutorial.springboot.graphql.service

import berryj.tutorial.springboot.graphql.entity.Product
import org.springframework.stereotype.Service

@Service
class ProductService {
    companion object {
        private val products = listOf(
                Product("1", "Spring Boot cookbook", 550.50),
                Product("2", "Spring Cloud cookbook", 720.50),
                Product("3", "Reactor Programming book", 400.00),
                Product("4", "ReactJs Programming book", 680.00)
        )
    }

    fun getAllProduct(): List<Product> {
        return products
    }

    fun getProductById(productId: String): Product {
        return products.find { it.productId == productId } ?: throw Exception("Data not found.")
    }
}