package de.variantsync.functjonal.category;

import java.util.function.BiFunction;
import java.util.function.Supplier;

record LambdaMonoid<M>(Supplier<M> empty, BiFunction<M, M, M> compose) implements Monoid<M> {
    @Override
    public M mEmpty() {
        return empty.get();
    }

    @Override
    public M mAppend(M a, M b) {
        return compose.apply(a, b);
    }
}
