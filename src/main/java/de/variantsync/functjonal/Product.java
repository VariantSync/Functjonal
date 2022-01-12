package de.variantsync.functjonal;

import de.variantsync.functjonal.category.Monoid;

import java.util.function.Function;

public record Product<A, B>(A first, B second) {
    /**
     * Products are monoids over monoidal values.
     * Creates a Monoid for Product<A, B> from monoids of for A and B.
     * @param ma Monoid over values of type A.
     * @param mb Monoid over values of type B.
     * @return a Monoid for Pair<A, B>.
     */
    public static <A, B> Monoid<Product<A, B>> MONOID(final Monoid<A> ma, final Monoid<B> mb) {
        return Monoid.From(
                () -> new Product<>(ma.neutral(), mb.neutral()),
                (a, b) -> new Product<>(ma.append(a.first, b.first), mb.append(a.second, b.second))
        );
    }

    public <A2> Product<A2, B> mapFirst(final Function<? super A, ? extends A2> f) {
        return new Product<>(f.apply(first), second);
    }

    public <B2> Product<A, B2> mapSecond(final Function<? super B, ? extends B2> f) {
        return new Product<>(first, f.apply(second));
    }

    public <A2, B2> Product<A2, B2> bimap(
            final Function<? super A, ? extends A2> fst,
            final Function<? super B, ? extends B2> snd) {
        return new Product<>(fst.apply(first), snd.apply(second));
    }
}
