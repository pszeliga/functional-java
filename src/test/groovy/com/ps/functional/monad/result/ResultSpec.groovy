package com.ps.functional.monad.result

import spock.lang.Specification

import java.util.function.Consumer
import java.util.function.Function
import static com.ps.functional.monad.result.Success.SUCCESS

class ResultSpec extends Specification {

    def error = ErrorType.of("error occurred", 1, ErrorType.Severity.CRITICAL, ErrorType.ErrorSource.CLIENT, "ERROR")

    def "isSuccess should be true for Success and false for Failure "() {
        expect:
        SUCCESS.isSuccess()
        !Failure.of(error).isSuccess()
    }

    def "isFailure should be false for Success and true for Failure "() {
        expect:
        !SUCCESS.isFailure()
        Failure.of(error).isFailure()
    }

    def "For Failure getError should return the error "() {
        given:
        Result result = Failure.of(error)

        expect:
        result.error == error
    }

    def "For Success getError should throw an exception"() {
        when:
        SUCCESS.error

        then:
        thrown(NoSuchElementException)
    }

    def "For Success getResult should return the result inside "() {
        given:
        String someResult = "some result inside"
        Result daoResult = SUCCESS.of(someResult)

        expect:
        daoResult.getResult() == someResult
    }

    def "For Failure getResult should throw an exception "() {
        when:
        Failure.of(error).getResult()

        then:
        thrown(NoSuchElementException)
    }

    def "Fold operation should reduce the Success to single value using successFunction "() {
        given:
        String resultString = "result"
        Function otherFunction = Mock(Function)
        Result result = SUCCESS.of(resultString)

        when:
        String reduceResult = result.fold({ it.toUpperCase() }, otherFunction)

        then:
        reduceResult == resultString.toUpperCase()
        0 * otherFunction._
    }

    def "Fold operation should reduce the Failure to single value using failureFunction "() {
        given:
        Function otherFunction = Mock(Function)
        Result result = Failure.of(error)

        when:
        String reduceResult = result.fold(otherFunction, { it.getError().getMessage() })

        then:
        reduceResult == error.getMessage()
        0 * otherFunction._
    }

    def "toString should properly format the insides of Result "() {
        given:
        String result = "result"

        expect:
        SUCCESS.of(result).toString() == "Success[${result}]"
        Failure.of(error).toString() == "Failure[1 - ERROR, Sev: CRITICAL, Source: CLIENT, Message: error occurred, optional: ]"
        def failure = Failure.of(error, "optional")
        failure.toString().contains(error.show())
        failure.toString().contains("optional")
    }

    def "map should transform the successful result from one type to another"() {
        given:
        String result = "result"

        expect:
        SUCCESS.of(result).map { it.charAt(0) }.getResult() == 'r'
    }

    def "map should not modify the Failure "() {
        expect:
        Failure.of(error).map { it.charAt(0) }.error == error
    }

    def "flatMap should transform the successful result from one type to another"() {
        given:
        String result = "result"

        expect:
        SUCCESS.of(result).flatMap { SUCCESS.of(it.charAt(0)) }.getResult() == 'r'
    }

    def "flatMap should not modify the Failure "() {
        expect:
        Failure.of(error).flatMap { SUCCESS.of(it.charAt(0)) }.error == error
    }

    def "If one of Results is a failure, using lift should always return this failure"() {
        given:
        Result success = SUCCESS
        Result failure = Failure.of(error)
        Result otherFailure = Failure.of(null)
        def transformer = { a, b -> 5 }

        expect:
        success.lift(success, transformer).isSuccess()
        success.lift(failure, transformer).isFailure()
        failure.lift(failure, transformer).isFailure()
        failure.lift(success, transformer).isFailure()

        and: "'this' should has precedence"
        failure.lift(otherFailure, transformer).getError().is(error)
    }

    def "lift should invoke function on insides of the Results"() {
        given:
        Result<String> resultString = SUCCESS.of("lululu")
        Result<String> resultOtherString = SUCCESS.of(" tengo manzana")

        def function = { str, opt -> str + opt }

        when:
        Result<String> result = resultString.lift(resultOtherString, function)

        then:
        result.isSuccess()
        result.getResult() == "lululu tengo manzana"
    }

    def "Optional error message for Success should be empty "() {
        when:
        SUCCESS.getOptionalErrorMessage()

        then:
        thrown(NoSuchElementException)
    }

    def "liftOptional should invoke lift method from Optionals"() {
        given:
        Result<Optional> first = Success.of(Optional.of(1))
        Result<Optional> second = Success.of(Optional.of(2))

        when:
        def result = first.lift(second, { a, b -> a.get() + b.get() })

        then:
        result.getResult() == 3
    }

    def "ifSuccess should invoke success consumer with wrapped object only if it is a success"() {
        given:
        def object = new Object()
        def success = Success.of(object)
        def failure = Failure.of(Mock(ErrorType))
        def successConsumer = Mock(Consumer.class)

        when:
        success.ifSuccess(successConsumer)

        then:
        1 * successConsumer.accept(object)

        when:
        failure.ifSuccess(successConsumer)

        then:
        0 * _
    }

    def "ifFailure should invoke failure consumer with itself only if it is a failure"() {
        given:
        def object = new Object()
        def success = Success.of(object)
        def failure = Failure.of(Mock(ErrorType))
        def failureConsumer = Mock(Consumer.class)

        when:
        success.ifFailure(failureConsumer)

        then:
        0 * _

        when:
        failure.ifFailure(failureConsumer)

        then:
        1 * failureConsumer.accept(failure)
    }

    def "emptyOptional should return success with Optional.empty()"() {
        when:
        def result = Success.emptyOptional()

        then:
        result.result == Optional.empty()
        result instanceof Success
    }

    def "ofOptional should return success with optional element"() {
        when:
        def success = Success.ofOptional(obj)

        then:
        success instanceof Success
        success.result instanceof Optional
        obj != null ? success.result.get() == obj : success.result == Optional.empty()

        where:
        obj << [null, new Object(), [:], [] as List]

    }
}
