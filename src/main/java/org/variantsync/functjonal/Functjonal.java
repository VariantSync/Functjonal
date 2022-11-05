package org.variantsync.functjonal;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.lang3.function.FailableFunction;
import org.apache.commons.lang3.function.FailableRunnable;
import org.apache.commons.lang3.function.FailableSupplier;

/**
 * Helper class containing methods for functional programming missing in the standard library
 * (or that we could not find).
 * Contains also methods for pattern matching.
 */
public class Functjonal {
    /// Containers
    public static <K1, K2, V1, V2> Map<K2, V2> bimap(
            Map<K1, V1> m,
            Function<? super K1, ? extends K2> key,
            Function<? super V1, ? extends V2> val) {
        return bimap(m, key, val, LinkedHashMap::new);
    }

    public static <K1, K2, V1, V2, M extends Map<K2, V2>> M bimap(
            Map<K1, V1> m,
            Function<? super K1, ? extends K2> key,
            Function<? super V1, ? extends V2> val,
            Supplier<M> mapFactory) {
        final M result = mapFactory.get();
        for (final Map.Entry<K1, V1> e : m.entrySet()) {
            result.put(key.apply(e.getKey()), val.apply(e.getValue()));
        }
        return result;
    }

    public static <T, U> List<U> map(final List<? extends T> a, final Function<T, U> f) {
        return a.stream().map(f).collect(Collectors.toList());
    }

    /// Pattern matching

    public static <A, B> B match(final Optional<A> ma, final Function<A, ? extends B> just, final Supplier<? extends B> nothing) {
        final Optional<B> x = ma.map(just);
        return x.orElseGet(nothing);
    }

    /**
     * Curried version of the above.
     */
    public static <A, B> Function<Optional<A>, B> match(final Function<A, B> just, final Supplier<? extends B> nothing) {
        return ma -> match(ma, just, nothing);
    }

    public static <A, B, C> Function<Result<A, B>, C> match(final Function<A, C> success, final Function<B, C> failure) {
        return ma -> ma.match(success, failure);
    }


    public static <T> Consumer<T> when(Predicate<T> condition, Consumer<? super T> task) {
        return t -> {
            if (condition.test(t)) {
                task.accept(t);
            }
        };
    }

    /**
     * Creates a branching function for given condition, then and else case.
     * @param condition The condition choosing whether to run 'then' or 'otherwise'.
     * @param then The function to apply when the given condition is met for a given a.
     * @param otherwise The function to apply when the given condition is not met for a given a.
     * @return A function that for a given a, returns then(a) if the given condition is met, and otherwise returns otherwise(a).
     */
    public static <A, B> Function<A, B> when(final Predicate<A> condition, final Function<A, B> then, final Function<A, B> otherwise) {
        return a -> condition.test(a) ? then.apply(a) : otherwise.apply(a);
    }

    /**
     * The same as @see when but without an else case (i.e., else case function identity).
     */
    public static <A> Function<A, A> when(final Predicate<A> condition, final Function<A, A> then) {
        return when(condition, then, Function.identity());
    }

    /**
     * A variant of @see when with a boolean value instead of a predicate.
     */
    public static <B> Function<Boolean, B> when(final Supplier<B> then, final Supplier<B> otherwise) {
        return condition -> condition ? then.get() : otherwise.get();
    }

    /// Java to FP

    public static <A> Function<A, Unit> Lift(final Consumer<A> f) {
        return a -> {
            f.accept(a);
            return Unit.Instance();
        };
    }

    public static Supplier<Unit> Lift(final Runnable f) {
        return () -> {
            f.run();
            return Unit.Instance();
        };
    }

    public static <E extends Exception> FailableSupplier<Unit, E> LiftFailable(final FailableRunnable<E> f) {
        return () -> {
            f.run();
            return Unit.Instance();
        };
    }

    /**
     * Maps the given function f onto the given value a if a is not null.
     *
     * @param n A nullable value that should be converted to a value of type B via f.
     * @param f A function that should be mapped onto a. f can safely assume that any arguments passed to it are not null.
     * @param errorMessage Creates an error message in case f threw an exception of type E.
     * @param <Nullable> The type of the nullable value a.
     * @param <B> The result type.
     * @param <E> The type of an exception that might be thrown by f.
     * @return Returns the result of f(a) if a is not null and f(a) did not throw an exception of type E.
     *         Returns Optional.empty() if a is null or f(a) threw an exception of type E.
     */
    public static <Nullable, B, E extends Exception> Optional<B> mapFailable(final Nullable n, final FailableFunction<Nullable, B, E> f, final Supplier<String> errorMessage) {
        return Optional.ofNullable(n).flatMap(a ->
                Result.Try(() -> f.apply(a)).match(
                        Optional::ofNullable, // actually the returned B can also be null, thus ofNullable here
                        exception -> {
//                            Logger.error(errorMessage.get(), exception);
                            return Optional.empty();
                        })
        );
    }

    public static <A, B, E extends Exception> Lazy<Optional<B>> mapFailableLazily(final A a, final FailableFunction<A, B, E> f, final Supplier<String> errorMessage) {
        return Lazy.of(() -> mapFailable(a, f, errorMessage));
    }

    /// Utility

    public static String unwords(Object... words) {
        return intercalate(" ", words);
    }

    public static String intercalate(String separator, Object... words) {
        return Arrays.stream(words).map(Object::toString).reduce((a, b) -> a + separator + b).orElse("");
    }
}
