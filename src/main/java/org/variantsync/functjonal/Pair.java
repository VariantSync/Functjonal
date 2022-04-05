package org.variantsync.functjonal;

import org.variantsync.functjonal.category.Monoid;

import java.util.function.Consumer;
import java.util.function.Function;

public record Pair<A, B>(A first, B second) {
    /**
     * Products are monoids over monoidal values.
     * Creates a Monoid for Pair<A, B> from monoids of for A and B.
     * @param ma Monoid over values of type A.
     * @param mb Monoid over values of type B.
     * @return a Monoid for Pair<A, B>.
     */
    public static <A, B> Monoid<Pair<A, B>> MONOID(final Monoid<A> ma, final Monoid<B> mb) {
        return Monoid.From(
                () -> new Pair<>(ma.neutral(), mb.neutral()),
                (a, b) -> new Pair<>(ma.append(a.first, b.first), mb.append(a.second, b.second))
        );
    }

    public <A2> Pair<A2, B> mapFirst(final Function<? super A, ? extends A2> f) {
        return new Pair<>(f.apply(first), second);
    }

    public <B2> Pair<A, B2> mapSecond(final Function<? super B, ? extends B2> f) {
        return new Pair<>(first, f.apply(second));
    }

    public <A2, B2> Pair<A2, B2> bimap(
            final Function<? super A, ? extends A2> fst,
            final Function<? super B, ? extends B2> snd) {
        return new Pair<>(fst.apply(first), snd.apply(second));
    }

    public void foreach(final Consumer<? super A> a, final Consumer<? super B> b) {
        a.accept(first);
        b.accept(second);
    }
}
