package dev.goerner.geozen.model

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
})
