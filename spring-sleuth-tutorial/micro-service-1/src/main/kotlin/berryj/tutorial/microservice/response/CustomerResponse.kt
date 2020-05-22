package berryj.tutorial.microservice.response

data class CustomerResponse(val customerId: String, val customerName: String, val phone: List<String>? = null) {
}