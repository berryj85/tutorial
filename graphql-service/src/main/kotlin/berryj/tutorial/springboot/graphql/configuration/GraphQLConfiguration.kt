package berryj.tutorial.springboot.graphql.configuration

import berryj.tutorial.springboot.graphql.data.fetcher.CustomerDataFetcher
import berryj.tutorial.springboot.graphql.data.fetcher.OrderDataFetcher
import berryj.tutorial.springboot.graphql.data.fetcher.ProductDataFetcher
import graphql.GraphQL
import graphql.schema.idl.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.io.File
import java.io.FilenameFilter

@Component
class GraphQLConfiguration {
    @Autowired
    private lateinit var customerDataFetcher: CustomerDataFetcher
    @Autowired
    private lateinit var productDataFetcher: ProductDataFetcher
    @Autowired
    private lateinit var orderDataFetcher:OrderDataFetcher

    @Bean
    fun typeDefinitionRegistry() = TypeDefinitionRegistry().apply {
        val schemaParser = SchemaParser()
        File(GraphQLConfiguration::class.java.getResource("/graphql").toURI())
                .listFiles(FilenameFilter { dir, name -> name.contains(".graphql") })
                .forEach {
                    this.merge(schemaParser.parse(it))
                }
    }

    @Bean
    fun runtimeWiring() = RuntimeWiring.newRuntimeWiring()
            .type(TypeRuntimeWiring.newTypeWiring("Query")
                    .dataFetcher("getCustomer", customerDataFetcher.getCustomer())
                    .dataFetcher("getOrderByCustomer", orderDataFetcher.getOrderByCustomerId())
                    .dataFetcher("getOrder",orderDataFetcher.getOrder())
                    .build()
            )
            .type(TypeRuntimeWiring.newTypeWiring("Order")
                    .dataFetcher("customer", customerDataFetcher.getCustomer())
                    .dataFetcher("totalAmount", orderDataFetcher.getTotalAmount())
                    .dataFetcher("totalPrice", orderDataFetcher.getTotalPrice())
                    .build()
            )
            .type(TypeRuntimeWiring.newTypeWiring("OrderItem")
                    .dataFetcher("product", productDataFetcher.getProduct())
                    .build()
            )
            .build()

    @Bean
    fun graphQL(typeDefinitionRegistry: TypeDefinitionRegistry, runtimeWiring: RuntimeWiring): GraphQL {
        val graphQLSchema = SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring)
        return GraphQL.newGraphQL(graphQLSchema).build()
    }
}