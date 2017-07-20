package com.ps.functional.monad.optional;

import java.util.Optional;
import java.util.function.BiFunction;

public interface Optionals {

    static <R, T, Z> Optional<Z> lift(Optional<T> left, Optional<R> right,
                                      BiFunction<? super T, ? super R, ? extends Z> function) {
        return left.flatMap(leftVal -> right.map(rightVal -> function.apply(leftVal, rightVal)));
    }

    static <R, T, Z> BiFunction<Optional<T>, Optional<R>, Optional<Z>> lift(BiFunction<? super T, ? super R, ? extends Z> function) {
        return (left, right) -> left.flatMap(leftVal -> right.map(rightVal -> function.apply(leftVal, rightVal)));
    }

}