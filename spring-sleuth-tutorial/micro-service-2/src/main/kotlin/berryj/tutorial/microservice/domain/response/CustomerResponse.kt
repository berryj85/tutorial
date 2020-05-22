package berryj.tutorial.microservice.domain.response

data class CustomerResponse(val customerId: String, val customerName: String, val phone: List<String>? = null) {
}