package berryj.tutorial.springboot.protobuf.microservice.service

import berryj.tutorial.springboot.protobuf.microservice.entity.CustomerEntity
import berryj.tutorial.springboot.protobuf.microservice.message.Customer
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Service

@Service
class CustomerService {

    companion object {
        private val log = LogManager.getLogger(CustomerService::class.java)
    }

    private val customers: MutableMap<String, CustomerEntity> = mutableMapOf(
            "1" to CustomerEntity("1", "Mr.A", "lstA", null, true),
            "2" to CustomerEntity("2", "Mr.B", "lstB", listOf("66900000001", "66900000002"), true)
    )

    fun getAll(): List<CustomerEntity> {
        return customers.values.sortedBy { it.customerId }
    }

    fun getByCustomerId(customerId: String): CustomerEntity? {
        return customers.get(customerId)


    }

    fun insertCustomer(customer: Customer): CustomerEntity {
        return customer.let {
            var id = 0
            if (it.customerId == 0) {
                id = getLastestCustomerId()
            } else {
                id = it.customerId
            }
            CustomerEntity(
                    id.toString(),
                    it.firstname,
                    it.lastname,
                    it.phoneNumbersList.toList(),
                    it.activeStatus
            )
        }.let {
            customers.put(it.customerId, it)
            it
        }
    }

    private fun getLastestCustomerId(): Int {
        return (customers.keys.map { it.toInt() }.max() ?: 0) + 1
    }

}