package berryj.tutorial.springboot.graphql.controller

import graphql.ExecutionInput
import graphql.GraphQL
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/graphql")
class GraphQLController {
    @Autowired
    private lateinit var graphQL: GraphQL

    @PostMapping
    fun executePost(@RequestBody request: GraphQLRequestBody): Any {
        return executeRequest(request)

    }

    private fun executeRequest(request: GraphQLRequestBody): Any {
        val executionInput = ExecutionInput.newExecutionInput().apply {
            this.query(request.query)

            if (!request.variables.isNullOrEmpty()) {
                this.variables(request.variables)
            }
        }.build()
        val executionResult = graphQL.execute(executionInput)
        val errors = executionResult.errors
        return executionResult
    }
}

data class GraphQLRequestBody(val query: String, val variables: Map<String, Any>?)
