package com.ps.functional.util

import spock.lang.Specification

import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Predicate

class FunctionUtilsSpec extends Specification {

    def "function method should just return the passed argument "() {
        given:
        Function addOne = {it + 1}

        expect:
        FunctionUtils.function(addOne).is(addOne)
    }

    def "biFunction method should just return the passed argument "() {
        given:
        BiFunction add = { a, b -> a + b}

        expect:
        FunctionUtils.biFunction(add).is(add)
    }

    def "method not should negate the predicate "() {
        given:
        Predicate equalsOne = { x -> x == 1 }
        Predicate notEqualsOne = FunctionUtils.not(equalsOne)

        expect:
        equalsOne.test(1)
        !notEqualsOne.test(1)
    }
}
