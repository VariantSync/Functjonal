package de.variantsync.functjonal.category;

import java.util.function.Supplier;

record LambdaMonoid<M>(Supplier<M> empty, Semigroup<M> compose) implements Monoid<M> {
    @Override
    public M neutral() {
        return empty.get();
    }

    @Override
    public M append(M a, M b) {
        return compose.append(a, b);
    }
}
