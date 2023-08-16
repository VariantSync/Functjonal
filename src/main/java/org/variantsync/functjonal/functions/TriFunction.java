package org.variantsync.functjonal.functions;

import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<A, B, C, D> {
    D apply(A a, B b, C c);

    default TriFunction<A, B, C, D> andThen(final Function<D, D> f) {
        return (n, l, d) -> f.apply(apply(n, l, d));
    }
}
