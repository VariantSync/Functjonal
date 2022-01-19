package de.variantsync.functjonal;

import de.variantsync.functjonal.category.Functor;
import de.variantsync.functjonal.category.Monoid;
import de.variantsync.functjonal.category.Semigroup;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Monad for explicit lazy evaluation.
 * A Lazy<A> represents a computation returning a A.
 * Using lazy allows to compose such computations without ever running the actual computation.
 * This way, writing a program is separated from evaluating the program.
 * (This lazy monad is actually the reader monad with an empty environment + a cache.)
 * <p>
 * Use Lazy to make explicit the points in computation when we interact with the environment.
 * In particular, in that moment, when you access the lazy's content, all necessary computations will run.
 *
 * @param <A> The return type of this lazy computation.
 */
@SuppressWarnings("rawtypes")
public final class Lazy<A> implements Functor<Lazy, A>, CachedValue {
    /**
     * Lazy is a semigroup if the lazy values form a semigroup.
     *
     * @param s Semigroup over values.
     * @return A semigroup for lazy values of type A.
     */
    public static <A> Semigroup<Lazy<A>> semigroup(final Semigroup<A> s) {
        return (a, b) -> Lazy.of(() -> s.append(a.run(), b.run()));
    }

    /**
     * Lazy is a monoid if the lazy values are monoidal.
     * Creates a Monoid for Lazy<A> from the monoid of the value type A.
     *
     * @param m Monoid over values.
     * @return a Monoid for Lazy<A>.
     */
    public static <A> Monoid<Lazy<A>> monoid(final Monoid<A> m) {
        return Monoid.from(
                () -> Lazy.pure(m.neutral()),
                semigroup(m)
        );
    }

    private final Supplier<? extends A> get;
    private A val = null;

    private Lazy(final A val) {
        Objects.requireNonNull(val);
        this.val = val;
        this.get = null;
    }

    private Lazy(final Supplier<? extends A> get) {
        Objects.requireNonNull(get);
        this.get = get;
    }

    /**
     * Creates a new Lazy encapsulating the given (expensive) computation.
     *
     * @param f The computation that produces the value of the lazy when accessed.
     * @return A lazy object encapsulating the given computation.
     */
    public static <B> Lazy<B> of(final Supplier<? extends B> f) {
        return new Lazy<>(f);
    }

    /**
     * Creates a new Lazy that just wraps a value.
     * This means that the given b will be stored in the Lazy's cache right away.
     * Thus, no computation will be performed when the value of the Lazy is accessed.
     * Usually, this is not what you are looking for as a Lazy's purpose is to encapsulate
     * an expensive computation of the value (b) instead of just wrapping that value.
     * However, pure allows to lift a value to a lazy such that it can be combined with other Lazys
     * (e.g., with map, then, or bind).
     *
     * @param b The value to cache.
     * @return A lazy caching the given value.
     */
    public static <B> Lazy<B> pure(final B b) {
        return new Lazy<>(b);
    }

    /**
     * Run the lazy computation and obtain the result.
     *
     * @return The result of this lazy computation.
     */
    public A run() {
        if (val == null) {
            // We don't have to check if get != null here because we did that in the constructor.
            // If it is null, then val != null and we wouldn't enter this branch.
            val = get.get();
        }
        return val;
    }

    /**
     * Run the lazy computation, obtain the result, and immediately forget it.
     * This method first calls {@link #run()} and then {@link #forget()}.
     *
     * @return The result of this lazy computation.
     */
    public A take() {
        final A result = run();
        forget();
        return result;
    }

    /**
     * Clears the cached value, such that it has to be recomputed next time it is queried.
     */
    public void forget() {
        val = null;
    }

    /**
     * Lazy is a functor.
     *
     * @param f Function to apply to the result of this Lazy when it is computed.
     * @return Composed Lazy that applies f to the result of this Lazy after computation.
     */
    public <B> Lazy<B> map(final Function<? super A, ? extends B> f) {
        return new Lazy<>(() -> f.apply(run()));
    }

    /**
     * Returns a new lazy computation that
     * first runs this Lazy,
     * then discards the result
     * and returns s.get().
     * "l.then(s)" is equivalent to "l.map(x -> s.get())"
     *
     * @param s The new computation to run after this one.
     * @return A new Lazy that runs this Lazy but returns the result of s.
     */
    public <B> Lazy<B> then(final Supplier<? extends B> s) {
        // Inlined version of: map(a -> f.get())
        return new Lazy<>(() -> {
            this.run();
            return s.get();
        });
    }

    /**
     * Lazy is an applicative functor.
     *
     * @param lf Lazy that holds a function to apply to this Lazy's result after computation (similar to map).
     * @return Composed Lazy that applies the function computed by lf to the result of this Lazy after computation.
     */
    public <B> Lazy<B> splat(final Lazy<Function<? super A, ? extends B>> lf) {
        return new Lazy<>(() -> lf.run().apply(run()));
    }

    /**
     * Lazy is a monad.
     * Chains the given lazy computation with this one
     * (i.e., applies the given lazy computation to the result of this Lazy once its computed).
     * Another common name for bind is flatMap.
     *
     * @param f A lazy computation to chain to this one.
     * @return Returns a new lazy computation composed of this and the given Lazy.
     */
    public <B> Lazy<B> bind(final Function<A, Lazy<B>> f) {
        // This is the inlined version of `join(map(f))` for performance reasons.
        return new Lazy<>(() -> f.apply(run()).run()); // == join(map(f))
    }

    /**
     * Flattens a nested Lazy.
     *
     * @param l A nested Lazy that should be flattened to a single Lazy.
     * @return A new Lazy that returns the result of the innermost Lazy.
     */
    public static <B> Lazy<B> join(final Lazy<Lazy<B>> l) {
        return new Lazy<>(() -> l.run().run());
    }

    /**
     * Combines two lazy computation to a single one that returns both their results.
     *
     * @param other The lazy to run together with this Lazy.
     * @return A new Lazy running "this" and "other" and returning the results in a pair.
     */
    public <B> Lazy<Product<A, B>> and(final Lazy<? extends B> other) {
        return new Lazy<>(() -> new Product<>(run(), other.run()));
    }
}
