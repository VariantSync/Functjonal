package de.variantsync.functjonal;

import java.util.function.Function;

public record Product<A, B>(A first, B second) {
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
