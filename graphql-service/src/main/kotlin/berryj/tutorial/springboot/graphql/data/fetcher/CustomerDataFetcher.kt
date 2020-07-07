package berryj.tutorial.springboot.graphql.data.fetcher

import berryj.tutorial.springboot.graphql.service.CustomerService
import graphql.schema.DataFetcher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CustomerDataFetcher {
    @Autowired
    private lateinit var customerService: CustomerService
    fun getCustomer() = DataFetcher {
        val customerId = it.arguments["id"] as String?

    }
}