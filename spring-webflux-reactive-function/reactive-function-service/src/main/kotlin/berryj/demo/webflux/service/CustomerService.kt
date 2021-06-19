package berryj.demo.webflux.service

import berryj.demo.webflux.exception.DataNotFoundException
import berryj.demo.webflux.repository.CustomerEntity
import berryj.demo.webflux.repository.CustomerRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
class CustomerService(val customerRepository: CustomerRepository) {

    fun getCustomer(customerId: String): Mono<CustomerEntity> {
        return customerRepository.getCustomerById(customerId.toInt()).doOnSuccess {
            if (it == null)
                Mono.error<CustomerEntity>(DataNotFoundException())
        }
    }

    fun saveCustomer(customer:CustomerEntity): Mono<CustomerEntity> {
        return customerRepository.save(customer)
    }
}