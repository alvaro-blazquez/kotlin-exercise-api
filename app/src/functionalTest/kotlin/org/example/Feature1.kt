package org.example

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith

class Feature1 : BehaviorSpec({

    Given("a new feature is requested for a project") {
        When("some action of a feature is triggered") {
            Then("some result is expected") {
                "hello".length shouldBe 5
            }
            And("some other result is expected") {
                "world" should startWith("wor")
            }
        }
    }

})