package dev.goerner.geozen.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PositionTest : FunSpec({

    test("longitude and latitude constructor") {
        //given
        val position = Position(1.0, 2.0)

        //when
        // constructor is the action

        //then
        position.longitude shouldBe 1.0
        position.latitude shouldBe 2.0
        position.altitude shouldBe 0.0
    }

    test("longitude latitude and altitude constructor") {
        //given
        val position = Position(1.0, 2.0, 3.0)

        //when
        // constructor is the action

        //then
        position.longitude shouldBe 1.0
        position.latitude shouldBe 2.0
        position.altitude shouldBe 3.0
    }

    test("get longitude") {
        //given
        val position = Position(1.0, 2.0, 3.0)

        //when
        // access property

        //then
        position.longitude shouldBe 1.0
    }

    test("get latitude") {
        //given
        val position = Position(1.0, 2.0, 3.0)

        //when
        // access property

        //then
        position.latitude shouldBe 2.0
    }

    test("get altitude") {
        //given
        val position = Position(1.0, 2.0, 3.0)

        //when
        // access property

        //then
        position.altitude shouldBe 3.0
    }

    test("longitude infinite") {
        //given
        // when & then
        shouldThrow<IllegalArgumentException> {
            Position(Double.POSITIVE_INFINITY, 0.0)
        }
    }

    test("latitude infinite") {
        //given
        // when & then
        shouldThrow<IllegalArgumentException> {
            Position(0.0, Double.NEGATIVE_INFINITY)
        }
    }

    test("altitude infinite") {
        //given
        // when & then
        shouldThrow<IllegalArgumentException> {
            Position(0.0, 0.0, Double.POSITIVE_INFINITY)
        }
    }

    test("longitude NaN not allowed") {
        //given
        // when & then
        shouldThrow<IllegalArgumentException> {
            Position(Double.NaN, 0.0)
        }
    }

    test("latitude NaN not allowed") {
        //given
        // when & then
        shouldThrow<IllegalArgumentException> {
            Position(0.0, Double.NaN)
        }
    }

    test("altitude NaN not allowed") {
        //given
        // when & then
        shouldThrow<IllegalArgumentException> {
            Position(0.0, 0.0, Double.NaN)
        }
    }
})
