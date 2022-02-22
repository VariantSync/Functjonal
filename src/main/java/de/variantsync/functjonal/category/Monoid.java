package de.variantsync.functjonal.category;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * A semigroup with a neutral element w.r.t. composition.
 * @param <M> The type that forms a monoid.
 */
public interface Monoid<M> extends Semigroup<M>, Collector<M, MonoidCollector<M>, M> {
    M neutral();

    static <N> Monoid<N> From(final Supplier<N> empty, final Semigroup<N> compose) {
        return new LambdaMonoid<>(empty, compose);
    }

    @Override
    default Supplier<MonoidCollector<M>> supplier() {
        return () -> new MonoidCollector<>(this);
    }

    @Override
    default BiConsumer<MonoidCollector<M>, M> accumulator() {
        return MonoidCollector::accumulate;
    }

    @Override
    default BinaryOperator<MonoidCollector<M>> combiner() {
        return MonoidCollector::combine;
    }

    @Override
    default Function<MonoidCollector<M>, M> finisher() {
        return MonoidCollector::finish;
    }

    @Override
    default Set<Characteristics> characteristics() {
        return Set.of(
                Characteristics.UNORDERED
        );
    }
}
