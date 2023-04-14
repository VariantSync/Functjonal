package org.variantsync.functjonal;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public final class Curry {
    private Curry() {}

    public static <A, B, C> Function<A, Function<B, C>> curry(BiFunction<A, B, C> f) {
        return a -> b -> f.apply(a, b);
    }

    public static <A, B> Function<A, Consumer<B>> curry(BiConsumer<A, B> c) {
        return a -> b -> c.accept(a, b);
    }

    public static <A, B, C> BiFunction<B, A, C> flip(BiFunction<A, B, C> f) {
        return (b, a) -> f.apply(a, b);
    }

    public static <A, B, C> Function<B, Function<A, C>> flip(Function<A, Function<B, C>> f) {
        return b -> a -> f.apply(a).apply(b);
    }
}
