package berryj.tutorial.springboot.protobuf.microservice.entity

data class CustomerEntity(val customerId: String,
                          val firstname: String,
                          val lastname: String,
                          val phoneNumbers: List<String>? = null,
                          val activeStatus: Boolean = true) {
}