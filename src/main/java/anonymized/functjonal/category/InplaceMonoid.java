package anonymized.functjonal.category;

import java.util.function.Supplier;

/**
 * An in-place semigroup with a neutral value.
 * @see InplaceSemigroup
 */
public interface InplaceMonoid<M> extends Monoid<M>, InplaceSemigroup<M> {
    static <N> InplaceMonoid<N> From(final Supplier<N> empty, final InplaceSemigroup<N> compose) {
        return new LambdaInplaceMonoid<>(empty, compose);
    }
}
