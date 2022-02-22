package vevos.functjonal.category;

import java.util.function.Supplier;

/**
 * A semigroup with a neutral element w.r.t. composition.
 * @param <M> The type that forms a monoid.
 */
public interface Monoid<M> extends Semigroup<M> {
    M neutral();

    static <N> Monoid<N> From(final Supplier<N> empty, final Semigroup<N> compose) {
        return new LambdaMonoid<>(empty, compose);
    }
}
