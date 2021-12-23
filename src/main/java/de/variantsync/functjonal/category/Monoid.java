package de.variantsync.functjonal.category;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public interface Monoid<M> {
    M mEmpty();
    M mAppend(final M a, final M b);

    static <N> Monoid<N> Create(
            final Supplier<N> empty, final BiFunction<N, N, N> compose) {
        return new Monoid<>() {
            @Override
            public N mEmpty() {
                return empty.get();
            }

            @Override
            public N mAppend(final N a, final N b) {
                return compose.apply(a, b);
            }
        };
    }
}
