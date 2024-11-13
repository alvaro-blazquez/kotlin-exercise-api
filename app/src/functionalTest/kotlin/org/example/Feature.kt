package org.example

import io.kotest.assertions.json.shouldEqualJson
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import java.time.LocalDate

class Feature : BehaviorSpec({

    Given("""I have a customer with ID "customer_123" """) {
        And(
            """And I have items with product details
            | product_id | quantity | price |
            | prod_001   | 2        | 15.50 |
            | prod_002   | 1        | 45.00 |
        """.trimMargin()
        ) {

            When("""I send a POST request to "/orders" with the order data""") {

                Then("I receive a 201 Created status code") {

                }
                And("the response should be the created order response") {

                }
            }
        }
    }

})
