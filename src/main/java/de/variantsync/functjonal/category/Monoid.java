package de.variantsync.functjonal.category;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface Monoid<M> {
    M mEmpty();
    M mAppend(final M a, final M b);

    static <N> Monoid<N> From(final Supplier<N> empty, final BiFunction<N, N, N> compose) {
        return new LambdaMonoid<>(empty, compose);
    }
}
