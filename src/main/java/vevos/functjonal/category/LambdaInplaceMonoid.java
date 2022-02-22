package vevos.functjonal.category;

import java.util.function.Supplier;

record LambdaInplaceMonoid<M>(Supplier<M> empty, InplaceSemigroup<M> append) implements InplaceMonoid<M> {
    @Override
    public M neutral() {
        return empty.get();
    }

    @Override
    public void appendToFirst(final M a, final M b) {
        append.appendToFirst(a, b);
    }
}
