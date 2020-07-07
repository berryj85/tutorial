package berryj.tutorial.springboot.graphql.entity

data class Customer (val customerId:String,val customerName:String,val email:String,val phone:List<String>?,val address:String?=null,val active:Boolean = true)