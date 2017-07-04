package com.ps.functional.util;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public interface FunctionUtils {

    /*
        Casts any one argument method reference to a Function type
     */
    static <T, R> Function<T, R> function(Function<T, R> function) {
        return function;
    }

    /*
        Casts any two arguments method reference to a BiFunction type
    */
    static <T, U, R> BiFunction<T, U, R> biFunction(BiFunction<T, U, R> biFunction) {
        return biFunction;
    }

    /*
       Negates the predicate
    */
    static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }

}