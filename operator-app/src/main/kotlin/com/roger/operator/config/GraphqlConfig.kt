package com.roger.operator.config

import com.expediagroup.graphql.federation.execution.FederatedTypeRegistry
import com.expediagroup.graphql.federation.execution.FederatedTypeResolver
import com.roger.operator.dao.OperatorDao
import com.roger.operator.domain.Operator
import graphql.schema.DataFetchingEnvironment
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class GraphqlConfig(@Autowired private val operatorDao: OperatorDao) {

    @Bean
    fun federatedTypeRegistry() = FederatedTypeRegistry(mapOf("Service" to operatorResolver))

    val operatorResolver = object : FederatedTypeResolver<Operator> {
        override suspend fun resolve(
            environment: DataFetchingEnvironment,
            representations: List<Map<String, Any>>
        ): List<Operator?> = representations.map {
            operatorDao.get(it["code"].toString())
        }
    }
}