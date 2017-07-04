package com.ps.functional.monad.optional;

import java.util.Optional;
import java.util.function.BiFunction;

public interface Optionals {

    static <R, T, Z> Optional<Z> lift(Optional<T> left, Optional<R> right,
                                      BiFunction<? super T, ? super R, ? extends Z> function) {
        return left.flatMap(leftVal -> right.map(rightVal -> function.apply(leftVal, rightVal)));
    }

    static <T> Optional<T> getPresentOrApplyBoth(Optional<T> left, Optional<T> right,
                                                 BiFunction<? super T, ? super T, ? extends T> function) {
        if (left.isPresent()) {
            if(right.isPresent()) {
                return lift(left, right, function);
            }
            return left;
        }
        return right;
    }
}