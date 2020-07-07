package berryj.tutorial.springboot.graphql.data.fetcher

import berryj.tutorial.springboot.graphql.entity.Order
import berryj.tutorial.springboot.graphql.service.CustomerService
import graphql.schema.DataFetcher
import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomerDataFetcher {
    companion object {
        val log = LogManager.getLogger(CustomerDataFetcher::class.java)
    }

    @Autowired
    private lateinit var customerService: CustomerService

    fun getCustomer() = DataFetcher {
        val order = it.getSource<Order>()
        val customerId = order?.customerId ?: it.arguments["customerId"] as String
        customerService.getCustomerById(customerId)


    }
}