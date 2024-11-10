package org.example

import io.kotest.core.spec.style.BehaviorSpec

//Feature: Create Order
//    As an e-commerce platform user
//    I want to be able to create a new order
//    So that I can place orders for customers with detailed information about the items
class CreateOrderFeature : BehaviorSpec({

    // Scenario: Successfully create a new order
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
                And("the response contains the order_id") {

                }
                And("""the response contains a status of "Pending" """) {

                }
                And("the total_amount is 76.00") {

                }
            }
        }
    }

})