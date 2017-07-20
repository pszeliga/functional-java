package com.ps.functional.monad.optional

import spock.lang.Specification

import java.util.function.BiFunction

class OptionalsSpec extends Specification {

    BiFunction<Integer, Integer, String> sumString = { a, b -> a + b + "" } as BiFunction

    BiFunction<Optional<Integer>, Optional<Integer>, Optional<String>> liftedSumString = Optionals.lift(sumString)

    def "Lift should lift any biFunction to operate on Optionals"() {
        given:
        Optional<Integer> first = Optional.of(1)
        Optional<Integer> second = Optional.of(2)

        expect:
        liftedSumString.apply(first, second).get() == "3"
    }

    def "If one of the arguments of apply is empty then the result should be empty "() {
        expect:
        liftedSumString.apply(Optional.of(1), Optional.of(2)).isPresent()
        !liftedSumString.apply(Optional.of(1), Optional.empty()).isPresent()
        !liftedSumString.apply(Optional.empty(), Optional.of(1)).isPresent()
        !liftedSumString.apply(Optional.empty(), Optional.empty()).isPresent()
    }
}
